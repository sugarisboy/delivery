package dev.muskrat.delivery.product.service;

import dev.muskrat.delivery.auth.security.jwt.JwtUser;
import dev.muskrat.delivery.cities.dao.City;
import dev.muskrat.delivery.components.exception.EntityNotFoundException;
import dev.muskrat.delivery.files.components.FileFormat;
import dev.muskrat.delivery.files.components.FileFormats;
import dev.muskrat.delivery.files.dto.FileStorageUploadDTO;
import dev.muskrat.delivery.files.service.FileStorageService;
import dev.muskrat.delivery.partner.dao.Partner;
import dev.muskrat.delivery.product.converter.ProductToProductDTOConverter;
import dev.muskrat.delivery.product.dao.Category;
import dev.muskrat.delivery.product.dao.CategoryRepository;
import dev.muskrat.delivery.product.dao.Product;
import dev.muskrat.delivery.product.dao.ProductRepository;
import dev.muskrat.delivery.product.dto.*;
import dev.muskrat.delivery.shop.dao.Shop;
import dev.muskrat.delivery.shop.dao.ShopRepository;
import dev.muskrat.delivery.user.dao.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ShopRepository shopRepository;
    private final FileStorageService fileStorageService;
    private final FileFormats fileFormats;
    private final ProductToProductDTOConverter productToProductDTOConverter;

    public ProductCreateResponseDTO create(ProductCreateDTO productCreateDTO) {
        Product product = new Product();

        product.setTitle(productCreateDTO.getTitle());
        product.setDescription(productCreateDTO.getDescription());
        product.setValue(productCreateDTO.getValue());
        product.setPrice(productCreateDTO.getPrice());
        product.setAvailable(true);

        Long shopId = productCreateDTO.getShopId();
        Optional<Shop> byId = shopRepository.findById(shopId);
        if (!byId.isPresent())
            throw new RuntimeException("Shop with id " + shopId + " not found");
        Shop shop = byId.get();
        product.setShop(shop);

        Long categoryId = productCreateDTO.getCategory();
        Optional<Category> category = categoryRepository.findById(categoryId);
        if (!category.isPresent())
            throw new RuntimeException("Category is not defined");
        product.setCategory(category.get());
        Product productWithId = productRepository.save(product);

        return ProductCreateResponseDTO.builder()
            .id(productWithId.getId())
            .build();
    }

    @Override
    public void delete(Long id) {
        Optional<Product> byId = productRepository.findById(id);

        byId.ifPresent(p -> {
            p.setDeleted(true);
            productRepository.save(p);
        });
    }

    @Override
    public ProductUpdateResponseDTO update(ProductUpdateDTO productDTO) {
        Long id = productDTO.getId();

        Optional<Product> byId = productRepository.findById(id);
        if (!byId.isPresent()) {
            throw new EntityNotFoundException("Entity not found");
        }

        Product product = byId.get();

        if (productDTO.getDescription() != null)
            product.setDescription(productDTO.getDescription());

        if (productDTO.getPrice() != null)
            product.setPrice(productDTO.getPrice());

        if (productDTO.getTitle() != null)
            product.setTitle(productDTO.getTitle());

        if (productDTO.getValue() != null)
            product.setValue(productDTO.getValue());

        if (productDTO.getAvailable() != null)
            product.setAvailable(productDTO.getAvailable());

        if (productDTO.getCategory() != null && productDTO.getCategory().longValue() != product.getCategory().getId()) {
            long productCategoryId = productDTO.getCategory();
            Optional<Category> category = categoryRepository.findById(productCategoryId);
            if (category.isPresent()) {
                product.setCategory(category.get());
            } else
                throw new EntityNotFoundException("Category don't found");
        }

        productRepository.save(product);
        return ProductUpdateResponseDTO.builder()
            .id(product.getId())
            .build();
    }

    @Override
    public Optional<ProductDTO> findById(Long id) {
        Optional<Product> product = productRepository.findById(id);
        return product.map(productToProductDTOConverter::convert);
    }

    @Override
    public List<ProductDTO> findAll() {
        return productRepository.findAll()
            .stream()
            .map(productToProductDTOConverter::convert)
            .collect(Collectors.toList());
    }

    @Override
    public ProductPageDTO findAll(ProductPageRequestDTO requestDTO, Pageable pageable) {

        // todo: hibernate search for title
        String title = null;
        double minPrice = Double.MIN_VALUE;
        double maxPrice = Double.MAX_VALUE;
        Shop shop = null;
        Category category = null;

        if (requestDTO != null) {
            if (requestDTO.getTitle() != null)
                title = requestDTO.getTitle();

            if (requestDTO.getMinPrice() != null)
                minPrice = requestDTO.getMinPrice();

            if (requestDTO.getMaxPrice() != null)
                maxPrice = requestDTO.getMaxPrice();

            if (requestDTO.getShopId() != null) {
                Long shopId = requestDTO.getShopId();
                Optional<Shop> byId = shopRepository.findById(shopId);
                if (!byId.isPresent())
                    throw new EntityNotFoundException("Shop with id " + shopId + " not found");
                shop = byId.get();
            }

            if (requestDTO.getCategoryId() != null) {
                Long categoryId = requestDTO.getCategoryId();
                Optional<Category> byId = categoryRepository.findById(categoryId);
                if (!byId.isPresent())
                    throw new EntityNotFoundException("Category with id " + categoryId + " not found");
                category = byId.get();
            }
        }

        Page<Product> page = productRepository.findWithFilter(title, shop, category, minPrice, maxPrice, pageable);

        List<Product> content = page.getContent();
        List<ProductDTO> collect = content.stream()
            .map(productToProductDTOConverter::convert)
            .collect(Collectors.toList());

        return ProductPageDTO.builder()
            .products(collect)
            .currentPage(pageable.getPageNumber())
            .lastPage(page.getTotalPages())
            .build();
    }

    @Override
    public FileStorageUploadDTO updateImg(MultipartFile img, Long productId) {
        String fileName = String.format("%d.jpg", productId);
        FileFormat type = fileFormats.getProduct();

        return fileStorageService.uploadFile(type, fileName, img);
    }

    @Override
    public boolean isProductOwner(Authentication authentication, Long productId) {
        Optional<Product> byId = productRepository.findById(productId);
        if (!byId.isPresent())
            throw new EntityNotFoundException("Product with id " + productId + " not found");
        Product product = byId.get();

        Shop shop = product.getShop();
        Partner partner = shop.getPartner();
        User user = partner.getUser();
        String userEmail = user.getEmail();

        JwtUser jwtUser = (JwtUser) authentication.getPrincipal();
        String jwtUserEmail = jwtUser.getEmail();

        return userEmail.equalsIgnoreCase(jwtUserEmail);
    }

    @Override
    public List<CategoryDTO> findAllCategories() {
        return categoryRepository.findAll().stream()
            .map(category -> new CategoryDTO(category.getId(), category.getTitle()))
            .collect(Collectors.toList());
    }
}

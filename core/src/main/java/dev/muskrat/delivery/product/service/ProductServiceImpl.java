package dev.muskrat.delivery.product.service;

import dev.muskrat.delivery.product.converter.ProductToProductDTOConverter;
import dev.muskrat.delivery.product.dao.Category;
import dev.muskrat.delivery.product.dao.CategoryRepository;
import dev.muskrat.delivery.product.dao.Product;
import dev.muskrat.delivery.product.dao.ProductRepository;
import dev.muskrat.delivery.product.dto.*;
import dev.muskrat.delivery.shop.dao.Shop;
import dev.muskrat.delivery.shop.dao.ShopRepository;
import dev.muskrat.delivery.components.exception.EntityNotFoundException;
import dev.muskrat.delivery.shop.dto.ShopDTO;
import dev.muskrat.delivery.shop.dto.ShopPageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ShopRepository shopRepository;
    private final ProductToProductDTOConverter productToProductDTOConverter;

    public ProductCreateResponseDTO create(ProductCreateDTO productCreateDTO) {
        Product product = new Product();

        product.setTitle(productCreateDTO.getTitle());
        product.setDescription(productCreateDTO.getDescription());
        product.setValue(productCreateDTO.getValue());
        product.setPrice(productCreateDTO.getPrice());

        Long shopId = productCreateDTO.getShopId();
        Optional<Shop> byId = shopRepository.findById(shopId);
        if (byId.isEmpty())
            throw new RuntimeException("Shop with id " + shopId + " not found");
        Shop shop = byId.get();
        product.setShop(shop);

        Long categoryId = productCreateDTO.getCategory();
        Optional<Category> category = categoryRepository.findById(categoryId);
        if (category.isEmpty())
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

        byId.ifPresentOrElse(p -> {
            p.setDeleted(true);
            productRepository.save(p);
        }, () -> {
            throw new EntityNotFoundException("Product with id " + id + " not found");
        });
    }

    @Override
    public ProductUpdateResponseDTO update(ProductUpdateDTO productDTO) {
        Long id = productDTO.getId();

        Optional<Product> byId = productRepository.findById(id);
        if (byId.isEmpty()) {
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

        if (productDTO.getCategory() != null) {
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
    public ProductPageDTO findAll(Pageable pageable) {
        Page<Product> page = productRepository.findAll(pageable);

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
}

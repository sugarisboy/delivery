package dev.muskrat.delivery.service.product;

import dev.muskrat.delivery.converter.ObjectConverter;
import dev.muskrat.delivery.dao.product.Category;
import dev.muskrat.delivery.dao.product.CategoryRepository;
import dev.muskrat.delivery.dao.product.Product;
import dev.muskrat.delivery.dao.product.ProductRepository;
import dev.muskrat.delivery.dto.product.ProductCreateResponseDTO;
import dev.muskrat.delivery.dto.product.ProductDTO;
import dev.muskrat.delivery.dto.product.ProductDeleteResponseDTO;
import dev.muskrat.delivery.dto.product.ProductUpdateResponseDTO;
import dev.muskrat.delivery.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
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
    private final ObjectConverter<Product, ProductDTO> productToProductDTOConverter;

    public ProductCreateResponseDTO create(ProductDTO productDTO) {
        Product product = new Product();

        product.setTitle(productDTO.getTitle());
        product.setAvailable(productDTO.getAvailable());
        product.setDescription(productDTO.getDescription());
        product.setValue(productDTO.getValue());
        product.setPrice(productDTO.getPrice());
        product.setImageUrl(productDTO.getUrl());

        Long categoryId = productDTO.getCategory();
        Optional<Category> category = categoryRepository.findById(categoryId);
        category.ifPresent(c -> {
            product.setCategory(c);
            productRepository.save(product);
        });

        return ProductCreateResponseDTO.builder()
            .id(productDTO.getId())
            .build();
    }

    @Override
    public ProductDeleteResponseDTO delete(ProductDTO productDTO) {
        Long id = productDTO.getId();
        if (id == null) {
            throw new EntityNotFoundException("ProductId don't send");
        }

        Optional<Product> byId = productRepository.findById(id);
        if (byId.isEmpty()) {
            throw new EntityNotFoundException("Shop with this id don't found");
        }

        Product product = byId.get();
        productRepository.delete(product);
        return ProductDeleteResponseDTO.builder()
            .id(product.getId())
            .build();
    }


    @Override
    public ProductUpdateResponseDTO update(ProductDTO productDTO) {
        Long id = productDTO.getId();

        if (id == null)
            throw new EntityNotFoundException("Entity with this id don't found");

        Optional<Product> byId = productRepository.findById(id);
        if (byId.isEmpty()) {
            throw new EntityNotFoundException("Entity don't found");
        }

        Product product = byId.get();

        if (productDTO.getDescription() != null)
            product.setDescription(productDTO.getDescription());

        if (productDTO.getPrice() != null)
            product.setPrice(productDTO.getPrice());

        if (productDTO.getTitle() != null)
            product.setTitle(productDTO.getTitle());

        if (productDTO.getUrl() != null)
            product.setImageUrl(productDTO.getUrl());

        if (productDTO.getValue() != null)
            product.setValue(productDTO.getValue());

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
}

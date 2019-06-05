package dev.muskrat.delivery.service.product;

import dev.muskrat.delivery.converter.ObjectConverter;
import dev.muskrat.delivery.dao.product.Category;
import dev.muskrat.delivery.dao.product.CategoryRepository;
import dev.muskrat.delivery.dao.product.Product;
import dev.muskrat.delivery.dao.product.ProductRepository;
import dev.muskrat.delivery.dto.product.*;
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

    public ProductCreateResponseDTO create(ProductCreateDTO productCreateDTO) {
        Product product = new Product();

        product.setTitle(productCreateDTO.getTitle());
        product.setDescription(productCreateDTO.getDescription());
        product.setValue(productCreateDTO.getValue());
        product.setPrice(productCreateDTO.getPrice());

        Long categoryId = productCreateDTO.getCategory();
        Optional<Category> category = categoryRepository.findById(categoryId);
        if (category.isPresent()) {
            product.setCategory(category.get());
            Product productWithId = productRepository.save(product);

            return ProductCreateResponseDTO.builder()
                .id(productWithId.getId())
                .build();
        }
        throw new RuntimeException("Category is not defined");
    }

    @Override
    public void delete(Long id) {
        Optional<Product> byId = productRepository.findById(id);

        if (byId.isEmpty()) {
            throw new EntityNotFoundException("Shop with id " + id + " not found");
        }

        Product product = byId.get();
        productRepository.delete(product);
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

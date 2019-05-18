package dev.muskrat.delivery.service;

import dev.muskrat.delivery.converter.ObjectConverter;
import dev.muskrat.delivery.dao.Product;
import dev.muskrat.delivery.dao.ProductRepository;
import dev.muskrat.delivery.dto.ProductDTO;
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
    private final ObjectConverter<Product, ProductDTO> productToProductDTOConverter;


    public void create(ProductDTO productDTO) {
        Product product = new Product();
        product.setTitle(productDTO.getTitle());
        product.setPrice(productDTO.getPrice());
        productRepository.save(product);
    }

    @Override
    public void delete(ProductDTO productDTO) {
        Long id = productDTO.getId();
        if (id != null) {
            Optional<Product> byId = productRepository.findById(id);
            byId.ifPresent(productRepository::delete);
        }
    }

    @Override
    public void update(ProductDTO productDTO) {
        Long id = productDTO.getId();

        if (id == null)
            throw new RuntimeException("Entity don't found");

        Optional<Product> byId = productRepository.findById(id);
        if (byId.isEmpty())
            throw new RuntimeException("Entity don't found");

        Product product = byId.get();
        if (!byId.isPresent())
            return;

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

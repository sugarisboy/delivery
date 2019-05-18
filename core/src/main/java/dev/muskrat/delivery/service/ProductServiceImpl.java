package dev.muskrat.delivery.service;

import dev.muskrat.delivery.dao.Product;
import dev.muskrat.delivery.dao.ProductRepository;
import dev.muskrat.delivery.dto.ProductDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;

    public void create(ProductDTO productDTO) {
        Product product = new Product();
        product.setTitle(productDTO.getTitle());
        product.setPrice(productDTO.getPrice());
        repository.save(product);
    }

    @Override
    public void delete(ProductDTO productDTO) {
        Long id = productDTO.getId();
        Optional<Product> byId = repository.findById(id);
        byId.ifPresent(repository::delete);
    }

    @Override
    public void update(ProductDTO productDTO) {

    }

    @Override
    public Optional<ProductDTO> findById(Long id) {
        //return repository.findById(id).;
        return null;
    }

    @Override
    public List<ProductDTO> findAll() {
        return null;
    }

}

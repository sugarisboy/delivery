package dev.muskrat.delivery.service;

import dev.muskrat.delivery.dto.ProductDTO;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    void create(ProductDTO productDTO);

    void delete(ProductDTO productDTO);

    void update(ProductDTO productDTO);

    Optional<ProductDTO> findById(Long id);

    List<ProductDTO> findAll();

}

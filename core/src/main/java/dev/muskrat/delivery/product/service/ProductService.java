package dev.muskrat.delivery.product.service;

import dev.muskrat.delivery.product.dto.*;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    ProductCreateResponseDTO create(ProductCreateDTO productCreateDTO);

    void delete(Long id);

    ProductUpdateResponseDTO update(ProductUpdateDTO productDTO);

    Optional<ProductDTO> findById(Long id);

    List<ProductDTO> findAll();

}

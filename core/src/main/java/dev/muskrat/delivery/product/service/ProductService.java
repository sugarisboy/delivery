package dev.muskrat.delivery.product.service;

import dev.muskrat.delivery.product.dto.*;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    ProductCreateResponseDTO create(ProductCreateDTO productCreateDTO);

    ProductUpdateResponseDTO update(ProductUpdateDTO productDTO);

    Optional<ProductDTO> findById(Long id);

    ProductPageDTO findAll(ProductPageRequestDTO requestDTO, Pageable page);

    List<ProductDTO> findAll();

    void delete(Long id);
}

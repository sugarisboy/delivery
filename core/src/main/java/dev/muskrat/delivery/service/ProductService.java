package dev.muskrat.delivery.service;

import dev.muskrat.delivery.dto.product.ProductCreateResponseDTO;
import dev.muskrat.delivery.dto.product.ProductDTO;
import dev.muskrat.delivery.dto.product.ProductDeleteResponseDTO;
import dev.muskrat.delivery.dto.product.ProductUpdateResponseDTO;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    ProductCreateResponseDTO create(ProductDTO productDTO);

    ProductDeleteResponseDTO delete(ProductDTO productDTO);

    ProductUpdateResponseDTO update(ProductDTO productDTO);

    Optional<ProductDTO> findById(Long id);

    List<ProductDTO> findAll();

}

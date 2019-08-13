package dev.muskrat.delivery.product.service;

import dev.muskrat.delivery.files.dto.FileStorageUploadDTO;
import dev.muskrat.delivery.product.dto.*;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    ProductCreateResponseDTO create(ProductCreateDTO productCreateDTO);

    void delete(Long id);

    ProductUpdateResponseDTO update(ProductUpdateDTO productDTO);

    Optional<ProductDTO> findById(Long id);

    ProductPageDTO findAll(ProductPageRequestDTO requestDTO, Pageable page);

    List<ProductDTO> findAll();

    FileStorageUploadDTO updateImg(MultipartFile img, Long productId);

    boolean isProductOwner(Authentication authentication, Long productId);
}

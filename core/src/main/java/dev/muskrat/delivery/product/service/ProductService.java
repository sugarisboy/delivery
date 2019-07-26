package dev.muskrat.delivery.product.service;

import dev.muskrat.delivery.files.dto.FileStorageUploadDTO;
import dev.muskrat.delivery.product.dto.*;
import dev.muskrat.delivery.shop.dto.ShopPageDTO;
import org.springframework.data.domain.Pageable;
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

    void delete(Long id);

    FileStorageUploadDTO updateImg(MultipartFile img, Long productId);
}

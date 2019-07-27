
package dev.muskrat.delivery.shop.service;

import dev.muskrat.delivery.files.dto.FileStorageUploadDTO;
import dev.muskrat.delivery.shop.dto.*;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface ShopService {

    ShopCreateResponseDTO create(ShopCreateDTO shopDTO);

    ShopUpdateResponseDTO update(ShopUpdateDTO shopDTO);

    ShopScheduleResponseDTO updateSchedule(ShopScheduleUpdateDTO workDayDTO);

    Optional<ShopDTO> findById(Long id);

    ShopPageDTO findAll(ShopPageRequestDTO requestDTO, Pageable page);

    void delete(Long id);

    FileStorageUploadDTO updateImg(MultipartFile img, Long shopId);
}

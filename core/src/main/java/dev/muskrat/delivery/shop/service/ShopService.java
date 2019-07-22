
package dev.muskrat.delivery.shop.service;

import dev.muskrat.delivery.shop.dto.*;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ShopService {

    ShopCreateResponseDTO create(ShopCreateDTO shopDTO);

    ShopUpdateResponseDTO update(ShopUpdateDTO shopDTO);

    ShopScheduleResponseDTO updateSchedule(ShopScheduleUpdateDTO workDayDTO);

    Optional<ShopDTO> findById(Long id);

    ShopPageDTO findAll(ShopPageRequestDTO requestDTO, Pageable page);

    void delete(Long id);

}

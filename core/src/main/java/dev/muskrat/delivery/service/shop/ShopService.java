
package dev.muskrat.delivery.service.shop;

import dev.muskrat.delivery.dto.shop.*;

import java.util.List;
import java.util.Optional;

public interface ShopService {

    ShopCreateResponseDTO create(ShopCreateDTO shopDTO);

    ShopUpdateResponseDTO update(ShopUpdateDTO shopDTO);

    ShopScheduleResponseDTO updateSchedule(ShopScheduleUpdateDTO workDayDTO);

    Optional<ShopDTO> findById(Long id);

    Optional<ShopScheduleDTO> findScheduleById(Long id);

    List<ShopDTO> findAll();

    void delete(Long id);

}

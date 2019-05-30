
package dev.muskrat.delivery.service.shop;

import dev.muskrat.delivery.dto.shop.*;

import java.util.List;
import java.util.Optional;

public interface ShopService {

    ShopCreateResponseDTO create(ShopCreateDTO shopDTO);


    ShopUpdateResponseDTO update(ShopUpdateDTO shopDTO);

    Optional<ShopDTO> findById(Long id);

    List<ShopDTO> findAll();

    void delete(Long id);

}

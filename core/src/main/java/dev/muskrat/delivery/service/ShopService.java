
package dev.muskrat.delivery.service;

import dev.muskrat.delivery.dto.shop.ShopCreateResponseDTO;
import dev.muskrat.delivery.dto.shop.ShopDTO;
import dev.muskrat.delivery.dto.shop.ShopDeleteResponseDTO;
import dev.muskrat.delivery.dto.shop.ShopUpdateResponseDTO;

import java.util.List;
import java.util.Optional;

public interface ShopService {

    ShopCreateResponseDTO create(ShopDTO shopDTO);

    ShopDeleteResponseDTO delete(ShopDTO shopDTO);

    ShopUpdateResponseDTO update(ShopDTO shopDTO);

    Optional<ShopDTO> findById(Long id);

    List<ShopDTO> findAll();
}

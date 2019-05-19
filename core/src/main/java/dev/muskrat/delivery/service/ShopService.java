
package dev.muskrat.delivery.service;

import dev.muskrat.delivery.dto.ShopDTO;

import java.util.List;
import java.util.Optional;

public interface ShopService {

    void create(ShopDTO shopDTO);

    void delete(ShopDTO shopDTO);

    void update(ShopDTO shopDTO);

    Optional<ShopDTO> findById(Long id);

    List<ShopDTO> findAll();
}

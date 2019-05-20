package dev.muskrat.delivery.service.shop;

import dev.muskrat.delivery.converter.ShopToShopDTOConverter;
import dev.muskrat.delivery.dao.shop.Shop;
import dev.muskrat.delivery.dao.shop.ShopRepository;
import dev.muskrat.delivery.dto.shop.ShopCreateResponseDTO;
import dev.muskrat.delivery.dto.shop.ShopDTO;
import dev.muskrat.delivery.dto.shop.ShopDeleteResponseDTO;
import dev.muskrat.delivery.dto.shop.ShopUpdateResponseDTO;
import dev.muskrat.delivery.exception.EntityExistException;
import dev.muskrat.delivery.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ShopServiceImpl implements ShopService {

    private final ShopRepository shopRepository;
    private final ShopToShopDTOConverter shopToShopDTOConverter;

    @Override
    public ShopCreateResponseDTO create(ShopDTO shopDTO) {
        Optional<Shop> sameShopPartner = shopRepository
                .findByName(shopDTO.getName());

        if (sameShopPartner.isPresent()) {
            throw new EntityExistException("This shop name is already taken.");
        }

        Shop shop = new Shop();
        shop.setName(shopDTO.getName());
        shopRepository.save(shop);
        return ShopCreateResponseDTO.builder()
                .id(shop.getId())
                .build();
    }

    @Override
    public ShopDeleteResponseDTO delete(ShopDTO shopDTO) {
        Long id = shopDTO.getId();
        if (id == null) {
            throw new EntityNotFoundException("ShopId don't send");
        }

        Optional<Shop> byId = shopRepository.findById(id);
        if (byId.isEmpty()) {
            throw new EntityNotFoundException("Shop with this id don't found");
        }

        Shop shop = byId.get();
        shopRepository.delete(shop);
        return ShopDeleteResponseDTO.builder()
                .id(shop.getId())
                .build();
    }

    @Override
    public ShopUpdateResponseDTO update(ShopDTO shopDTO) {
        Long id = shopDTO.getId();
        if (id == null) {
            throw new EntityNotFoundException("ShopId don't send");
        }

        Optional<Shop> byId = shopRepository.findById(id);
        if (byId.isEmpty()) {
            throw new EntityNotFoundException("Shop with this id don't found");
        }

        Shop shop = byId.get();
        if (shopDTO.getName() != null)
            shop.setName(shopDTO.getName());
        shopRepository.save(shop);

        return ShopUpdateResponseDTO.builder()
                .id(shop.getId())
                .build();
    }

    @Override
    public Optional<ShopDTO> findById(Long id) {
        return shopRepository
                .findById(id)
                .map(shopToShopDTOConverter::convert);
    }

    @Override
    public List<ShopDTO> findAll() {
        return shopRepository.findAll()
                .stream()
                .map(shopToShopDTOConverter::convert)
                .collect(Collectors.toList());
    }
}

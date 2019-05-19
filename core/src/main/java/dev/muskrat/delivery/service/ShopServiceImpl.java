package dev.muskrat.delivery.service;

import dev.muskrat.delivery.EntityExistException;
import dev.muskrat.delivery.converter.ShopToShopDTOConverter;
import dev.muskrat.delivery.dao.Shop;
import dev.muskrat.delivery.dao.ShopRepository;
import dev.muskrat.delivery.dto.ShopDTO;
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
    public void create(ShopDTO shopDTO) {
        Optional<Shop> sameShoplPartner = shopRepository
                .findByName(shopDTO.getName());

        if (sameShoplPartner.isPresent()) {
            throw new EntityExistException("This shop name is already taken.");
        }

        Shop shop = new Shop();
        shop.setName(shopDTO.getName());
        shopRepository.save(shop);
    }

    @Override
    public void delete(ShopDTO shopDTO) {
        Long id = shopDTO.getId();
        if (id != null) {
            Optional<Shop> byId = shopRepository.findById(id);
            byId.ifPresent(shopRepository::delete);
        }
    }

    @Override
    public void update(ShopDTO shopDTO) {
        Long id = shopDTO.getId();
        String name = shopDTO.getName();
        if (id != null && name != null) {
            Optional<Shop> byId = shopRepository.findById(id);
            if (!byId.isPresent())
                return;

            byId.ifPresent(shop -> shop.setName(name));
            shopRepository.save(byId.get());
        }
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

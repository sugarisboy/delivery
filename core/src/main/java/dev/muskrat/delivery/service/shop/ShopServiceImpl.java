package dev.muskrat.delivery.service.shop;

import dev.muskrat.delivery.converter.ShopToShopDTOConverter;
import dev.muskrat.delivery.converter.WorkDayDTOToWorkDayConverter;
import dev.muskrat.delivery.dao.shop.Shop;
import dev.muskrat.delivery.dao.shop.ShopRepository;
import dev.muskrat.delivery.dao.shop.WorkDay;
import dev.muskrat.delivery.dto.shop.*;
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
    private final WorkDayDTOToWorkDayConverter workDayDTOToWorkDayConverter;

    @Override
    public ShopCreateResponseDTO create(ShopCreateDTO shopDTO) {
        Optional<Shop> sameShopPartner = shopRepository
            .findByName(shopDTO.getName());

        if (sameShopPartner.isPresent()) {
            throw new EntityExistException("This shop name is already taken.");
        }

        Shop shop = new Shop();
        shop.setName(shopDTO.getName());
        Shop shopWithId = shopRepository.save(shop);
        return ShopCreateResponseDTO.builder()
            .id(shopWithId.getId())
            .build();
    }

    @Override
    public void delete(Long id) {
        Optional<Shop> byId = shopRepository.findById(id);
        if (byId.isEmpty()) {
            throw new EntityNotFoundException("Shop with id " + id + " not found");
        }

        Shop shop = byId.get();
        shopRepository.delete(shop);
    }

    @Override
    public ShopUpdateResponseDTO update(ShopUpdateDTO shopUpdateDTO) {
        Long id = shopUpdateDTO.getId();
        Optional<Shop> byId = shopRepository.findById(id);
        if (byId.isEmpty()) {
            throw new EntityNotFoundException("Shop with id " + id + " not exists");
        }

        Shop shop = byId.get();
        if (shopUpdateDTO.getName() != null)
            shop.setName(shopUpdateDTO.getName());
        if (shopUpdateDTO.getDescription() != null)
            shop.setDescription(shopUpdateDTO.getDescription());
        if (shopUpdateDTO.getFreeOrder() != null)
            shop.setFreeOrder(shopUpdateDTO.getFreeOrder());
        if (shopUpdateDTO.getLogo() != null)
            shop.setLogo(shopUpdateDTO.getLogo());
        if (shopUpdateDTO.getMinOrder() != null)
            shop.setMinOrder(shopUpdateDTO.getMinOrder());
        if (shopUpdateDTO.getVisible() != null)
            shop.setVisible(shopUpdateDTO.getVisible());

        if (shopUpdateDTO.getSchedule() != null) {
            List<WorkDayDTO> scheduleDTO = shopUpdateDTO.getSchedule();
            List<WorkDay> schedule = scheduleDTO.stream()
                    .map(workDayDTOToWorkDayConverter::convert)
                    .collect(Collectors.toList());
            shop.setSchedule(schedule);
        }
        //if (shopDTO.getRegion() != null)
        //    shop.setRegion(shopDTO.getRegion());
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

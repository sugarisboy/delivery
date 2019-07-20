package dev.muskrat.delivery.shop.service;

import dev.muskrat.delivery.cities.dao.CitiesRepository;
import dev.muskrat.delivery.cities.dao.City;
import dev.muskrat.delivery.shop.converter.ShopToShopDTOConverter;
import dev.muskrat.delivery.shop.dao.Shop;
import dev.muskrat.delivery.shop.dao.ShopRepository;
import dev.muskrat.delivery.components.exception.EntityExistException;
import dev.muskrat.delivery.components.exception.EntityNotFoundException;
import dev.muskrat.delivery.shop.dto.*;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ShopServiceImpl implements ShopService {

    private final ShopRepository shopRepository;
    private final CitiesRepository citiesRepository;
    private final ShopToShopDTOConverter shopToShopDTOConverter;

    @Override
    public ShopCreateResponseDTO create(ShopCreateDTO shopDTO) {

        Optional<Shop> sameShopPartner = shopRepository
                .findByName(shopDTO.getName());
        if (sameShopPartner.isPresent())
            throw new EntityExistException("This shop name is already taken.");

        Long cityId = shopDTO.getCityId();
        Optional<City> cityById = citiesRepository.findById(cityId);
        if (cityById.isEmpty())
            throw new EntityNotFoundException("City with id " + cityId + " not found");
        City city = cityById.get();

        Shop shop = new Shop();
        shop.setName(shopDTO.getName());
        shop.setCity(city);
        Shop shopWithId = shopRepository.save(shop);

        return ShopCreateResponseDTO.builder()
                .id(shopWithId.getId())
                .build();
    }

    @Override
    public void delete(Long id) {
        Optional<Shop> byId = shopRepository.findById(id);

        byId.ifPresentOrElse(p -> {
            p.setDeleted(true);
            shopRepository.save(p);
        }, () -> {
            throw new EntityNotFoundException("Shop with id " + id + " not found");
        });
    }

    @Override
    public ShopPageDTO findAll(Pageable pageable) {
        Page<Shop> page = shopRepository.findAll(pageable);

        List<Shop> content = page.getContent();
        List<ShopDTO> collect = content.stream()
            .map(shopToShopDTOConverter::convert)
            .collect(Collectors.toList());

        return ShopPageDTO.builder()
            .shops(collect)
            .currentPage(pageable.getPageNumber())
            .lastPage(page.getTotalPages())
            .build();
    }

    @Override
    public ShopPageDTO findAll(Pageable pageable, Long cityId) {
        Optional<City> byId = citiesRepository.findById(cityId);
        if (byId.isEmpty())
            throw new EntityNotFoundException("City with id " + cityId + "not found");
        City city = byId.get();

        Page<Shop> page = shopRepository.findAllByCity(city, pageable);

        List<Shop> content = page.getContent();
        List<ShopDTO> collect = content.stream()
            .map(shopToShopDTOConverter::convert)
            .collect(Collectors.toList());

        return ShopPageDTO.builder()
            .shops(collect)
            .currentPage(pageable.getPageNumber())
            .lastPage(page.getTotalPages())
            .build();
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
        if (shopUpdateDTO.getFreeOrderPrice() != null)
            shop.setFreeOrderPrice(shopUpdateDTO.getFreeOrderPrice());
        if (shopUpdateDTO.getLogo() != null)
            shop.setLogo(shopUpdateDTO.getLogo());
        if (shopUpdateDTO.getMinOrderPrice() != null)
            shop.setMinOrderPrice(shopUpdateDTO.getMinOrderPrice());
        if (shopUpdateDTO.getCityId() != null) {
            Long cityId = shopUpdateDTO.getCityId();
            Optional<City> cityById = citiesRepository.findById(cityId);

            if (cityById.isEmpty())
                throw new EntityNotFoundException("City with id " + id + " not found");

            City city = cityById.get();
            shop.setCity(city);
        }

        shopRepository.save(shop);

        return ShopUpdateResponseDTO.builder()
                .id(shop.getId())
                .build();
    }

    @Override
    public ShopScheduleResponseDTO updateSchedule(ShopScheduleUpdateDTO updateDTO) {
        Long id = updateDTO.getId();
        Optional<Shop> byId = shopRepository.findById(id);
        if (byId.isEmpty()) {
            throw new EntityNotFoundException("Shop with id " + id + " not found");
        }

        Shop shopById = byId.get();
        shopById.setOpen(updateDTO.getOpenTimeList());
        shopById.setClose(updateDTO.getCloseTimeList());
        Shop shopWithTd = shopRepository.save(shopById);

        return ShopScheduleResponseDTO.builder()
                .id(shopWithTd.getId())
                .build();
    }

    @Override
    public Optional<ShopDTO> findById(Long id) {
        return shopRepository
                .findById(id)
                .map(shopToShopDTOConverter::convert);
    }

    @Override
    public Optional<ShopScheduleDTO> findScheduleById(Long id) {
        Optional<Shop> byId = shopRepository
                .findById(id);
        if (byId.isPresent()) {
            Shop shop = byId.get();

            Hibernate.initialize(shop.getOpen());
            List<LocalTime> open = shop.getOpen();
            Hibernate.initialize(shop.getClose());
            List<LocalTime> close = shop.getClose();

            return Optional.of(ShopScheduleDTO.builder()
                    .id(id)
                    .open(open)
                    .close(close)
                    .build()
            );
        }
        throw new EntityNotFoundException("Shop with id " + id + "don't found");
    }

    @Override
    public List<ShopDTO> findAll() {
        return shopRepository.findAll()
            .stream()
            .map(shopToShopDTOConverter::convert)
            .collect(Collectors.toList());
    }
}

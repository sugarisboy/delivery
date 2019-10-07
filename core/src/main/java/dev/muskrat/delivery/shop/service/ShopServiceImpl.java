package dev.muskrat.delivery.shop.service;

import dev.muskrat.delivery.auth.security.jwt.JwtUser;
import dev.muskrat.delivery.cities.dao.CitiesRepository;
import dev.muskrat.delivery.cities.dao.City;
import dev.muskrat.delivery.components.exception.EntityExistException;
import dev.muskrat.delivery.components.exception.EntityNotFoundException;
import dev.muskrat.delivery.files.components.FileFormat;
import dev.muskrat.delivery.files.components.FileFormats;
import dev.muskrat.delivery.files.dto.FileStorageUploadDTO;
import dev.muskrat.delivery.files.service.FileStorageService;
import dev.muskrat.delivery.map.dao.RegionDelivery;
import dev.muskrat.delivery.map.dao.RegionDeliveryRepository;
import dev.muskrat.delivery.order.dao.OrderRepository;
import dev.muskrat.delivery.partner.dao.Partner;
import dev.muskrat.delivery.shop.converter.ShopToShopDTOConverter;
import dev.muskrat.delivery.shop.dao.Shop;
import dev.muskrat.delivery.shop.dao.ShopRepository;
import dev.muskrat.delivery.shop.dto.*;
import dev.muskrat.delivery.user.dao.User;
import dev.muskrat.delivery.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ShopServiceImpl implements ShopService {

    private final FileFormats fileFormats;
    private final UserRepository userRepository;
    private final ShopRepository shopRepository;
    private final CitiesRepository citiesRepository;
    private final FileStorageService fileStorageService;
    private final ShopToShopDTOConverter shopToShopDTOConverter;
    private final RegionDeliveryRepository regionDeliveryRepository;
    private final OrderRepository orderRepository;

    @Override
    public ShopCreateResponseDTO create(ShopCreateDTO shopDTO, Partner partner) {
        Optional<Shop> byName = shopRepository.findByName(shopDTO.getName());
        if (byName.isPresent())
            throw new EntityExistException("This shop name is already taken.");

        Long cityId = shopDTO.getCityId();
        City city = citiesRepository.findById(cityId)
            .orElseThrow(() -> new EntityNotFoundException("City with id " + cityId + " not found"));

        Shop shop = new Shop();
        shop.setName(shopDTO.getName());
        shop.setCity(city);
        shop.setPartner(partner);

        RegionDelivery regionDelivery = new RegionDelivery();
        regionDelivery.setAbscissa(new ArrayList<>());
        regionDelivery.setOrdinate(new ArrayList<>());
        regionDelivery = regionDeliveryRepository.save(regionDelivery);

        shop.setRegion(regionDelivery);
        shop = shopRepository.save(shop);

        return ShopCreateResponseDTO.builder()
                .id(shop.getId())
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
    public ShopPageDTO findAll(ShopPageRequestDTO requestDTO, Pageable pageable) {

        String name = null;
        City city = null;
        Double maxMinOrderPrice = Double.MAX_VALUE;
        Double maxFreeOrderPrice = Double.MAX_VALUE;

        if (requestDTO != null) {
            if (requestDTO.getName() != null) {
                name = requestDTO.getName();
            }

            if (requestDTO.getCityId() != null) {
                Long cityId = requestDTO.getCityId();
                Optional<City> byId = citiesRepository.findById(cityId);
                if (byId.isEmpty())
                    throw new EntityNotFoundException("City with " + cityId + " not found");
                city = byId.get();
            }

            if (requestDTO.getMaxMinOrderPrice() != null) {
                maxMinOrderPrice = requestDTO.getMaxMinOrderPrice();
            }

            if (requestDTO.getMaxFreeOrderPrice() != null) {
                maxFreeOrderPrice = requestDTO.getMaxFreeOrderPrice();
            }
        }

        Page<Shop> page = shopRepository.findWithFilter(name, city, pageable);

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

        RegionDelivery region = shop.getRegion();
        if (shopUpdateDTO.getFreeDeliveryCost() != null)
            region.setFreeDeliveryCost(shopUpdateDTO.getFreeDeliveryCost());
        if (shopUpdateDTO.getMinOrderCost() != null)
            region.setMinOrderCost(shopUpdateDTO.getMinOrderCost());
        if (shopUpdateDTO.getDeliveryCost() != null)
            region.setDeliveryCost(shopUpdateDTO.getDeliveryCost());

        if (shopUpdateDTO.getName() != null)
            shop.setName(shopUpdateDTO.getName());
        if (shopUpdateDTO.getDescription() != null)
            shop.setDescription(shopUpdateDTO.getDescription());
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
    public FileStorageUploadDTO updateImg(MultipartFile img, Long shopId) {
        String fileName = String.format("%d.jpg", shopId);
        FileFormat type = fileFormats.getShop();

        return fileStorageService.uploadFile(type, fileName, img);
    }

    @Override
    public boolean isShopOwner(Authentication authentication, Long id) {
        JwtUser jwtUser = (JwtUser) authentication.getPrincipal();
        String username = jwtUser.getEmail();

        Optional<Shop> byId = shopRepository.findById(id);
        if (byId.isEmpty())
            throw new EntityNotFoundException("Shop with id not found");
        Shop shop = byId.get();

        Partner partner = shop.getPartner();
        User user = partner.getUser();
        String email = user.getEmail();

        return email.equalsIgnoreCase(username);
    }

    @Override
    public ShopStatsResponseDTO stats(ShopStatsDTO shopStatsDTO) {
        Long shopId = shopStatsDTO.getShopId();
        Shop shop = shopRepository.findById(4L).orElseThrow(
            () -> new EntityNotFoundException("Shop with id " + shopId + " not found")
        );

        Instant startDate = shopStatsDTO.getStartDate();
        Instant endDate = shopStatsDTO.getEndDate();

        Double profit = orderRepository.getProfit(startDate, endDate, shop);

        return ShopStatsResponseDTO.builder()
            .profit(profit)
            .build();
    }

    @Override
    public ShopCreateResponseDTO createWithPartner(ShopCreateDTO shopCreateDTO) {
        Long partnerId = shopCreateDTO.getPartnerId();
        if (partnerId == null)
            throw new EntityNotFoundException("Please indicate partnerId in your request");

        User user = userRepository.findById(partnerId).orElseThrow(
            () -> new EntityNotFoundException("User with id " + partnerId + " not found")
        );
        Partner partner = user.getPartner();
        if (partner == null)
            throw new EntityNotFoundException("User with id " + partnerId + " is not partner");

        return create(shopCreateDTO, partner);
    }
}

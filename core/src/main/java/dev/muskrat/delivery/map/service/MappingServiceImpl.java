package dev.muskrat.delivery.map.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.muskrat.delivery.components.exception.AddressNotFoundException;
import dev.muskrat.delivery.components.exception.EntityNotFoundException;
import dev.muskrat.delivery.components.exception.LocationParseException;
import dev.muskrat.delivery.map.dao.RegionDelivery;
import dev.muskrat.delivery.map.dao.RegionDeliveryRepository;
import dev.muskrat.delivery.map.dao.RegionPoint;
import dev.muskrat.delivery.map.dto.AutoCompleteResponseDTO;
import dev.muskrat.delivery.map.dto.RegionDTO;
import dev.muskrat.delivery.map.dto.RegionUpdateDTO;
import dev.muskrat.delivery.map.dto.RegionUpdateResponseDTO;
import dev.muskrat.delivery.shop.dao.Shop;
import dev.muskrat.delivery.shop.dao.ShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MappingServiceImpl implements MappingService {

    private final MapApi mapApi;
    private final ShopRepository shopRepository;
    private final RegionDeliveryRepository regionDeliveryRepository;

    @Override
    public AutoCompleteResponseDTO autoComplete(String label) {
        return mapApi.autoComplete(label);
    }

    @Override
    public RegionPoint getPointByAddress(String label) {
        return mapApi.getPointByAddress(label);
    }

    public RegionUpdateResponseDTO updateRegion(RegionUpdateDTO regionUpdateDTO) {
        Long shopId = regionUpdateDTO.getShopId();
        Optional<Shop> byId = shopRepository.findById(shopId);
        if (!byId.isPresent())
            throw new EntityNotFoundException("Shop with " + shopId + " not found");
        Shop shop = byId.get();

        RegionDelivery regionDelivery = shop.getRegion() == null ? new RegionDelivery() : shop.getRegion();

        List<Double> pointsDTO = regionUpdateDTO.getPoints();
        Iterator<Double> iter = pointsDTO.iterator();

        List<Double> abscissa = new ArrayList<>();
        List<Double> ordinate = new ArrayList<>();

        while (iter.hasNext()) {
            abscissa.add(iter.next());
            ordinate.add(iter.next());
            iter.next();
        }

        regionDelivery.setAbscissa(abscissa);
        regionDelivery.setOrdinate(ordinate);
        regionDelivery = regionDeliveryRepository.save(regionDelivery);

        shop.setRegion(regionDelivery);
        shopRepository.save(shop);

        return RegionUpdateResponseDTO.builder()
            .id(shopId)
            .build();
    }

    @Override
    public boolean isValidAddress(String city, String label) {
        return mapApi.isValidAddress(city, label);
    }

    @Override
    public RegionDTO findShopRegion(Long shopId) {
        Shop shop = shopRepository.findById(shopId).orElseThrow(
            () -> new EntityNotFoundException("Shop with id " + shopId + " not found")
        );

        RegionDelivery regionDelivery = regionDeliveryRepository.findByShop(shop).orElseThrow(
            () -> new EntityNotFoundException("This shop not have region delivery")
        );

        return RegionDTO.builder()
            .shopId(shopId)
            .abscissa(regionDelivery.getAbscissa())
            .ordinate(regionDelivery.getOrdinate())
            .deliveryCost(regionDelivery.getDeliveryCost())
            .freeDeliveryCost(regionDelivery.getFreeDeliveryCost())
            .minOrderCost(regionDelivery.getMinOrderCost())
            .build();
    }

    @Bean
    private RegionDelivery getEmptyRegion() {
        RegionDelivery regionDelivery = new RegionDelivery();
        regionDelivery.setAbscissa(new ArrayList<>());
        regionDelivery.setOrdinate(new ArrayList<>());
        regionDeliveryRepository.save(regionDelivery);
        return regionDelivery;
    }
}

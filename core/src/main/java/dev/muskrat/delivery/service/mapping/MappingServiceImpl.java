package dev.muskrat.delivery.service.mapping;

import dev.muskrat.delivery.dao.mapping.RegionDelivery;
import dev.muskrat.delivery.dao.mapping.RegionPoint;
import dev.muskrat.delivery.dao.shop.Shop;
import dev.muskrat.delivery.dao.shop.ShopRepository;
import dev.muskrat.delivery.dto.mapping.AutoCompleteResponseDTO;
import dev.muskrat.delivery.dto.mapping.RegionUpdateDTO;
import dev.muskrat.delivery.dto.mapping.RegionUpdateResponseDTO;
import dev.muskrat.delivery.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MappingServiceImpl implements MappingService {

    // TODO move to config file @value("{mapapi.appId}")
    private final int MAX_RESULT = 5;
    private final String APP_ID = "PwhAzeVFHuSMGdcjtFvQ";
    private final String APP_CODE = "yE6QWws10hfiJKPyLE-hIQ";
    private final String COUNTRY = "MYS";

    // TODO https://www.baeldung.com/spring-uricomponentsbuilder
    private final String URL_TEMPLATE = "http://autocomplete.mapping.api.here.com/6.2/" +
        "suggest.json?" +
        "app_id=[APP_ID]" +
        "&app_code=[APP_CODE]" +
        "&query=[FIELD]" +
        "&country=[COUNTRY]" +
        "&maxresults=[MAX_RESULT]";

    private final ShopRepository shopRepository;

    public AutoCompleteResponseDTO autoComplete(String label) {
        RestTemplate restTemplate = new RestTemplate();
        AutoCompleteResponseDTO dto = restTemplate.getForObject(url(label), AutoCompleteResponseDTO.class);
        return dto;
    }

    private String url(String label) {
        return URL_TEMPLATE
            .replace("[APP_ID]", APP_ID)
            .replace("[APP_CODE]", APP_CODE)
            .replace("[COUNTRY]", COUNTRY)
            .replace("[MAX_RESULT]", MAX_RESULT + "")
            .replace("[FIELD]", label);
    }

    public RegionUpdateResponseDTO updateRegion(RegionUpdateDTO regionUpdateDTO) {
        RegionDelivery regionDelivery = new RegionDelivery();

        List<Double> pointsDTO = regionUpdateDTO.getPoints();
        List<RegionPoint> points = new ArrayList<>();
        Iterator<Double> iter = pointsDTO.iterator();
        while (iter.hasNext()) {
            points.add(new RegionPoint(
                iter.next(),
                iter.next(),
                iter.next())
            );
        }
        regionDelivery.setPoints(points);

        Long shopId = regionUpdateDTO.getShopId();
        Optional<Shop> byId = shopRepository.findById(shopId);
        if (byId.isEmpty())
            throw new EntityNotFoundException("Shop with " + shopId + " not found");

        Shop shop = byId.get();
        shop.setRegion(regionDelivery);
        shopRepository.save(shop);

        return RegionUpdateResponseDTO.builder()
            .id(shopId)
            .build();
    }


}

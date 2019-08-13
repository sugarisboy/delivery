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
import dev.muskrat.delivery.map.dto.RegionUpdateDTO;
import dev.muskrat.delivery.map.dto.RegionUpdateResponseDTO;
import dev.muskrat.delivery.shop.dao.Shop;
import dev.muskrat.delivery.shop.dao.ShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MappingServiceImpl implements MappingService {

    private final ShopRepository shopRepository;
    private final RegionDeliveryRepository regionDeliveryRepository;

    @Value("${geocode.app.id}")
    private String APP_ID;

    @Value("${geocode.app.code}")
    private String APP_CODE;

    @Value("${geocode.country}")
    private String COUNTRY;

    @Value("${geocode.complete.maxresults}")
    private Integer MAX_RESULTS;

    public AutoCompleteResponseDTO autoComplete(String label) {
        String http = UriComponentsBuilder.newInstance()
            .scheme("http")
            .host("autocomplete.mapping.api.here.com")
            .path("/6.2/geocode.json")
            .query("app_id=" + APP_ID)
            .query("app_code=" + APP_CODE)
            .query("country" + COUNTRY)
            .query("maxresults" + MAX_RESULTS)
            .query("query" + label)
            .build().toString();

        RestTemplate restTemplate = new RestTemplate();
        AutoCompleteResponseDTO autoCompleteResponseDTO = restTemplate
            .getForObject(http, AutoCompleteResponseDTO.class);
        return autoCompleteResponseDTO;
    }

    public RegionPoint getPointByAddress(String label) {
        String http = UriComponentsBuilder.newInstance()
            .scheme("http")
            .host("geocoder.api.here.com")
            .path("/6.2/geocode.json")
            .query("app_id=" + APP_ID)
            .query("app_code=" + APP_CODE)
            .query("locationattributes=none")
            .query("country=" + COUNTRY)
            .query("maxresults=" + MAX_RESULTS)
            .query("searchtext=" + label)
            .build().toString();

        RestTemplate restTemplate = new RestTemplate();
        String json = restTemplate.getForObject(http, String.class);

        JsonNode location;

        try {
            JsonNode httpResponse = new ObjectMapper().readTree(json);
            if (!httpResponse.has("Response"))
                throw new LocationParseException("MapApi timeout");

            JsonNode view = httpResponse.get("Response").get("View");
            if (!view.has(0))
                throw new AddressNotFoundException("Address not found");

            location = view.get(0)
                .get("Result").get(0)
                .get("Location")
                .get("NavigationPosition").get(0);
        } catch (IOException ex) {
            throw new LocationParseException("Location don't parsed");
        }

        double latitude = location.get("Latitude").asDouble();
        double longitude = location.get("Longitude").asDouble();

        return new RegionPoint(latitude, longitude, 100D);
    }

    public RegionUpdateResponseDTO updateRegion(RegionUpdateDTO regionUpdateDTO) {
        RegionDelivery regionDelivery = new RegionDelivery();

        List<Double> pointsDTO = regionUpdateDTO.getPoints();
        Iterator<Double> iter = pointsDTO.iterator();

        List<Double> abscissa = new ArrayList<>();
        List<Double> ordinate = new ArrayList<>();

        while (iter.hasNext()) {
            abscissa.add(iter.next());
            ordinate.add(iter.next());
        }

        regionDelivery.setAbscissa(abscissa);
        regionDelivery.setOrdinate(ordinate);
        regionDelivery = regionDeliveryRepository.save(regionDelivery);

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

    @Bean
    private RegionDelivery getEmptyRegion() {
        RegionDelivery regionDelivery = new RegionDelivery();
        regionDelivery.setAbscissa(new ArrayList<>());
        regionDelivery.setOrdinate(new ArrayList<>());
        regionDeliveryRepository.save(regionDelivery);
        return regionDelivery;
    }
}

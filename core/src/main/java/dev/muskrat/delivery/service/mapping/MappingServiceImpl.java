package dev.muskrat.delivery.service.mapping;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.muskrat.delivery.dao.mapping.RegionDelivery;
import dev.muskrat.delivery.dao.mapping.RegionPoint;
import dev.muskrat.delivery.dao.shop.Shop;
import dev.muskrat.delivery.dao.shop.ShopRepository;
import dev.muskrat.delivery.dto.mapping.AutoCompleteResponseDTO;
import dev.muskrat.delivery.dto.mapping.RegionUpdateDTO;
import dev.muskrat.delivery.dto.mapping.RegionUpdateResponseDTO;
import dev.muskrat.delivery.exception.AddressNotFoundException;
import dev.muskrat.delivery.exception.EntityNotFoundException;
import dev.muskrat.delivery.exception.LocationParseException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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

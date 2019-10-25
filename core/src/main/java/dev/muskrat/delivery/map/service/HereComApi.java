package dev.muskrat.delivery.map.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.muskrat.delivery.components.exception.AddressNotFoundException;
import dev.muskrat.delivery.components.exception.LocationParseException;
import dev.muskrat.delivery.map.dao.RegionPoint;
import dev.muskrat.delivery.map.dto.AutoCompleteResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class HereComApi implements MapApi {

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
            .scheme("https")
            .host("autocomplete.geocoder.api.here.com")
            .path("/6.2/suggest.json")
            .query("app_id=" + APP_ID)
            .query("app_code=" + APP_CODE)
            .query("country=" + COUNTRY)
            .query("maxresults=" + MAX_RESULTS)
            .query("query=" + label)
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
    @Override
    public boolean isValidAddress(String city, String label) {
        String http = UriComponentsBuilder.newInstance()
            .scheme("http")
            .host("geocoder.api.here.com")
            .path("/6.2/geocode.json")
            .query("app_id=" + APP_ID)
            .query("app_code=" + APP_CODE)
            .query("locationattributes=address")
            .query("country=" + COUNTRY)
            .query("maxresults=" + MAX_RESULTS)
            .query("searchtext=" + city + " " + label)
            .build().toString();

        RestTemplate restTemplate = new RestTemplate();
        String json = restTemplate.getForObject(http, String.class);

        JsonNode house;

        try {
            JsonNode httpResponse = new ObjectMapper().readTree(json);
            if (!httpResponse.has("Response"))
                throw new LocationParseException("MapApi timeout");

            JsonNode view = httpResponse.get("Response").get("View");
            if (!view.has(0))
                throw new AddressNotFoundException("Address not found");

            house = view.get(0)
                .get("Result").get(0)
                .get("Location")
                .get("Address")
                .get("HouseNumber");
        } catch (IOException ex) {
            throw new LocationParseException("Location don't parsed");
        }

        return !house.isNull();
    }

}

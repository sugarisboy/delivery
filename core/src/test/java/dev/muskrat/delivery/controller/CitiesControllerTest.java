package dev.muskrat.delivery.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.muskrat.delivery.DemoData;
import dev.muskrat.delivery.cities.dao.CitiesRepository;
import dev.muskrat.delivery.cities.dao.City;
import dev.muskrat.delivery.cities.dto.*;
import dev.muskrat.delivery.components.exception.EntityNotFoundException;
import lombok.SneakyThrows;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class CitiesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CitiesRepository citiesRepository;

    @Autowired
    private DemoData demoData;

    @Test
    @SneakyThrows
    @Transactional
    public void createCityTest() {
        CityCreateDTO cityCreateDTO = CityCreateDTO.builder()
            .name("Moscow")
            .build();

        String contentAsString = mockMvc.perform(post("/cities/create")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .header("Authorization", demoData.ACCESS_ADMIN)
            .header("Key", demoData.KEY_ADMIN)
            .content(objectMapper.writeValueAsString(cityCreateDTO))
        )
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();

        CityCreateResponseDTO cityCreateResponseDTO = objectMapper
            .readValue(contentAsString, CityCreateResponseDTO.class);

        Long createdCityId = cityCreateResponseDTO.getId();
        Optional<City> byId = citiesRepository.findById(createdCityId);
        if (byId.isEmpty())
            throw new EntityNotFoundException("City with id " + createdCityId + " not found");
        City city = byId.get();

        assertEquals("Moscow", city.getName());
    }

    @Test
    @SneakyThrows
    @Transactional
    public void updateCityTest() {
        City demoCity = demoData.cities.get(0);
        Long cityId = demoCity.getId();

        CityUpdateDTO cityUpdateDTO = CityUpdateDTO.builder()
            .id(cityId)
            .name("Berlin")
            .build();

        String contentAsString = mockMvc.perform(patch("/cities/update")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .header("Authorization", demoData.ACCESS_ADMIN)
            .header("Key", demoData.KEY_ADMIN)
            .content(objectMapper.writeValueAsString(cityUpdateDTO))
        )
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();

        CityUpdateResponseDTO cityUpdateResponseDTO = objectMapper
            .readValue(contentAsString, CityUpdateResponseDTO.class);

        Optional<City> byId = citiesRepository.findById(cityId);
        if (byId.isEmpty())
            throw new EntityNotFoundException("City with id " + cityId + " not found");
        City city = byId.get();

        assertEquals(city.getName(), "Berlin");
    }

    @Test
    @SneakyThrows
    @Transactional
    public void deleteCityTest() {
        City demoCity = demoData.cities.get(0);
        Long cityId = demoCity.getId();

        String contentAsString = mockMvc.perform(delete("/cities/" + cityId)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .header("Authorization", demoData.ACCESS_ADMIN)
            .header("Key", demoData.KEY_ADMIN)
        )
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();

        Optional<City> byId = citiesRepository.findById(cityId);
        assertTrue(byId.isEmpty());
    }
}

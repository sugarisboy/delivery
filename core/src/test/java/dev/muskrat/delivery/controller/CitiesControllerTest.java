package dev.muskrat.delivery.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.muskrat.delivery.cities.dao.CitiesRepository;
import dev.muskrat.delivery.cities.dao.City;
import dev.muskrat.delivery.cities.dto.CityCreateDTO;
import dev.muskrat.delivery.cities.dto.CityCreateResponseDTO;
import dev.muskrat.delivery.cities.dto.CityUpdateDTO;
import dev.muskrat.delivery.cities.dto.CityUpdateResponseDTO;
import dev.muskrat.delivery.cities.service.CitiesService;
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

    @Test
    @SneakyThrows
    @Transactional
    public void createCityTest() {
        String dataName = "city";

        CityCreateResponseDTO cityCreateResponseDTO = createCity(dataName );

        Long id = cityCreateResponseDTO.getId();
        Optional<City> byId = citiesRepository.findById(id);
        if (byId.isEmpty())
            throw new Exception("City don't create in repository");

        City city = byId.get();
        String name = city.getName();

        assertEquals(name, dataName);

        CityCreateDTO cityCreateDTO = CityCreateDTO.builder()
            .name(dataName)
            .build();

        mockMvc.perform(post("/cities/create")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(cityCreateDTO))
        )
            .andExpect(status().isConflict());
    }

    @Test
    @SneakyThrows
    @Transactional
    public void updateCityTest() {
        CityCreateResponseDTO cityCreateResponseDTO = createCity("city");
        Long id = cityCreateResponseDTO.getId();

        CityUpdateDTO cityUpdateDTO = CityUpdateDTO.builder()
            .id(id)
            .name("new city")
            .build();

        String contentAsString = mockMvc.perform(patch("/cities/update")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(cityUpdateDTO))
        )
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();

        CityUpdateResponseDTO cityUpdateResponseDTO = objectMapper
            .readValue(contentAsString, CityUpdateResponseDTO.class);

        Optional<City> byId = citiesRepository.findById(id);
        if (byId.isEmpty())
            throw new EntityNotFoundException("City with id " + id + " don't found");

        City city = byId.get();
        assertEquals(city.getName(), "new city");
    }

    @SneakyThrows
    public CityCreateResponseDTO createCity(String name) {
        CityCreateDTO cityCreateDTO = CityCreateDTO.builder()
            .name(name)
            .build();

        String contentAsString = mockMvc.perform(post("/cities/create")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(cityCreateDTO))
        )
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();

        CityCreateResponseDTO cityCreateResponseDTO = objectMapper
            .readValue(contentAsString, CityCreateResponseDTO.class);

        return cityCreateResponseDTO;
    }

    @Test
    @SneakyThrows
    public void deleteTest() {
        CityCreateResponseDTO city = createCity("city");
        Long id = city.getId();

        String contentAsString = mockMvc.perform(delete("/cities/" + id)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();

        Optional<City> byId = citiesRepository.findById(id);

        assertTrue(byId.isEmpty());
    }
}

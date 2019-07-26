package dev.muskrat.delivery.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.muskrat.delivery.cities.dao.CitiesRepository;
import dev.muskrat.delivery.cities.dao.City;
import dev.muskrat.delivery.product.dto.*;
import dev.muskrat.delivery.shop.dto.ShopCreateDTO;
import dev.muskrat.delivery.shop.dto.ShopCreateResponseDTO;
import dev.muskrat.delivery.product.service.ProductService;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CitiesRepository citiesRepository;


    private List<City> cities;

    @Before
    @SneakyThrows
    public void before() {
        cities = new ArrayList<>();
        City city1 = new City();
        city1.setName("city1");
        cities.add(city1);
        citiesRepository.save(city1);
        City city2 = new City();
        city1.setName("city2");
        cities.add(city2);
        citiesRepository.save(city2);
    }

    private ProductCreateDTO productDTO() {
        ShopCreateResponseDTO testableShop = createTestableShop();
        Long shopId = testableShop.getId();

        return ProductCreateDTO.builder()
            .title("title")
            .category(1L)
            .price(20D)
            .shopId(shopId)
            .build();
    }

    @SneakyThrows
    private ShopCreateResponseDTO createTestableShop() {
        ShopCreateDTO createDTO = ShopCreateDTO.builder()
            .name("shopId" + ThreadLocalRandom.current().nextInt())
            .cityId(cities.get(0).getId())
            .build();

        String contentAsString = mockMvc.perform(post("/shop/create")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(createDTO))
        )
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();

        return objectMapper
            .readValue(contentAsString, ShopCreateResponseDTO.class);
    }

    @SneakyThrows
    private ProductCreateResponseDTO createTestItem() {
        String contentAsString = mockMvc.perform(post("/product/create")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(productDTO()))
        )
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();

        return objectMapper
            .readValue(contentAsString, ProductCreateResponseDTO.class);
    }

    @Test
    @SneakyThrows
    public void productCreateTest() {
        ProductCreateResponseDTO productCreateResponseDTO = createTestItem();

        Long createdProductId = productCreateResponseDTO.getId();

        ProductDTO productDTO = productService
            .findById(createdProductId).orElseThrow();

        assertEquals(productDTO.getId(), createdProductId);
        assertEquals(productDTO.getTitle(), "title");
    }

    @Test
    @SneakyThrows
    public void productUpdateTest() {
        ProductCreateResponseDTO createResponseDTO = createTestItem();

        ProductUpdateDTO updateDTO = ProductUpdateDTO.builder()
            .id(createResponseDTO.getId())
            .description("description")
            .price(100D)
            .category(2L)
            .title("new title")
            .value(0D)
            .build();

        String contentAsString = mockMvc.perform(patch("/product/update")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updateDTO))
        )
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

        ProductUpdateResponseDTO productUpdateDTO = objectMapper
            .readValue(contentAsString, ProductUpdateResponseDTO.class);

        Long updatedProductId = productUpdateDTO.getId();

        ProductDTO productDTO = productService
            .findById(updatedProductId).orElseThrow();

        assertEquals(productDTO.getId(), updatedProductId);
        assertEquals(productDTO.getTitle(), updateDTO.getTitle());
        assertEquals(productDTO.getDescription(), updateDTO.getDescription());
        assertEquals(productDTO.getPrice(), updateDTO.getPrice());
        assertEquals(productDTO.getCategory(), updateDTO.getCategory());
        assertEquals(productDTO.getValue(), updateDTO.getValue());
    }

    @Test
    @SneakyThrows
    public void productDeleteTest() {
        ProductCreateResponseDTO item = createTestItem();

        Long itemId = item.getId();

        mockMvc.perform(delete("/product/" + itemId)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();

        Optional<ProductDTO> byId = productService.findById(itemId);
        assertTrue(byId.isEmpty());
    }
}

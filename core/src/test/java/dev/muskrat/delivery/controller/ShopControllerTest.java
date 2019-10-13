package dev.muskrat.delivery.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.muskrat.delivery.DemoData;
import dev.muskrat.delivery.cities.dao.City;
import dev.muskrat.delivery.components.exception.EntityNotFoundException;
import dev.muskrat.delivery.map.dto.RegionUpdateDTO;
import dev.muskrat.delivery.map.dto.RegionUpdateResponseDTO;
import dev.muskrat.delivery.shop.dao.Shop;
import dev.muskrat.delivery.shop.dao.ShopRepository;
import dev.muskrat.delivery.shop.dto.*;
import dev.muskrat.delivery.shop.service.ShopService;
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

import java.time.Instant;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class ShopControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ShopService shopService;

    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DemoData demoData;

    @Test
    @SneakyThrows
    @Transactional
    public void shopCreateTest() {
        City city = demoData.cities.get(0);
        Long cityId = city.getId();

        ShopCreateDTO createDTO = ShopCreateDTO.builder()
            .name("shop")
            .cityId(cityId)
            .build();

        String contentAsString = mockMvc.perform(post("/shop/create")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .header("Authorization", demoData.ACCESS_PARTNER)
            .header("Key", demoData.KEY_PARTNER)
            .content(objectMapper.writeValueAsString(createDTO))
        )
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();

        ShopCreateResponseDTO shopCreateResponseDTO = objectMapper
            .readValue(contentAsString, ShopCreateResponseDTO.class);

        Long createdId = shopCreateResponseDTO.getId();
        Optional<Shop> byId = shopRepository.findById(createdId);
        if (byId.isEmpty())
            throw new EntityNotFoundException("Shop with id " + createdId + " not found");
        Shop shop = byId.get();

        assertNotNull(shop);
        assertEquals("shop", shop.getName());
    }

    @Test
    @SneakyThrows
    @Transactional
    public void shopUpdateTest() {

        Shop shop = demoData.shops.get(0);
        Long shopId = shop.getId();

        City city = demoData.cities.get(0);
        Long cityId = city.getId();

        ShopUpdateDTO updateDTO = ShopUpdateDTO.builder()
            .id(shopId)
            .description("description")
            .freeDeliveryCost(10D)
            .minOrderCost(5D)
            .deliveryCost(7D)
            .name("new name")
            .cityId(cityId)
            .build();

        String contentAsString = mockMvc.perform(patch("/shop/update")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .header("Authorization", demoData.ACCESS_PARTNER)
            .header("Key", demoData.KEY_PARTNER)
            .content(objectMapper.writeValueAsString(updateDTO))
        )
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();

        ShopUpdateResponseDTO productUpdateDTO = objectMapper
            .readValue(contentAsString, ShopUpdateResponseDTO.class);

        Long updatedProductId = productUpdateDTO.getId();

        ShopDTO updatedShopDTO = shopService
            .findById(updatedProductId).orElseThrow();

        assertEquals(updateDTO.getId(), updatedShopDTO.getId());
        assertEquals(updateDTO.getDescription(), updatedShopDTO.getDescription());
        assertEquals(updateDTO.getFreeDeliveryCost(), updatedShopDTO.getFreeDeliveryCost());
        assertEquals(updateDTO.getDeliveryCost(), updatedShopDTO.getDeliveryCost());
        assertEquals(updateDTO.getMinOrderCost(), updatedShopDTO.getMinOrderCost());
        assertEquals(updateDTO.getName(), "new name");
    }

    @Test
    @SneakyThrows
    @Transactional
    public void shopScheduleUpdateTest() {
        Shop shop = demoData.shops.get(0);
        Long createdShopId = shop.getId();

        ShopScheduleUpdateDTO updateDTO = ShopScheduleUpdateDTO.builder()
            .id(createdShopId)
            .openTimeList(Arrays.asList(
                LocalTime.of(9, 0),
                LocalTime.of(9, 0),
                LocalTime.of(9, 0),
                LocalTime.of(9, 0),
                LocalTime.of(9, 0),
                LocalTime.of(9, 0),
                LocalTime.of(9, 0))
            )
            .closeTimeList(Arrays.asList(
                LocalTime.of(22, 0),
                LocalTime.of(22, 0),
                LocalTime.of(22, 0),
                LocalTime.of(22, 0),
                LocalTime.of(22, 0),
                LocalTime.of(22, 0),
                LocalTime.of(22, 0))
            )
            .build();

        String contentAsString = mockMvc.perform(patch("/shop/schedule/update")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .header("Authorization", demoData.ACCESS_PARTNER)
            .header("Key", demoData.KEY_PARTNER)
            .content(objectMapper.writeValueAsString(updateDTO))
        )
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();

        ShopScheduleResponseDTO productUpdateDTO = objectMapper
            .readValue(contentAsString, ShopScheduleResponseDTO.class);

        Long updatedProductId = productUpdateDTO.getId();

        ShopDTO updatedShopDTO = shopService
            .findById(updatedProductId).orElseThrow();

        ShopScheduleDTO schedule = updatedShopDTO.getSchedule();

        assertEquals(updateDTO.getId(), updatedShopDTO.getId());
        assertEquals(updateDTO.getOpenTimeList(), schedule.getOpen());
        assertEquals(updateDTO.getCloseTimeList(), schedule.getClose());
    }

    @Test
    @SneakyThrows
    @Transactional
    public void shopDeleteTest() {
        Shop shop = demoData.shops.get(0);
        Long shopId = shop.getId();

        mockMvc.perform(delete("/shop/" + shopId)
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", demoData.ACCESS_PARTNER)
            .header("Key", demoData.KEY_PARTNER)
            .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();

        Optional<ShopDTO> byId = shopService.findById(shopId);
        assertTrue(byId.isEmpty());
    }

    @Test
    @SneakyThrows
    @Transactional
    public void regionUpdateTest() {
        Shop shop = demoData.shops.get(0);

        Long shopId = shop.getId();

        RegionUpdateDTO regionUpdateDTO = RegionUpdateDTO.builder()
            .shopId(shopId)
            .points(Arrays.asList(
                1D, 2D, 3D, 4D, 5D, 6D, 7D, 8D
            )).build();

        String contentAsString = mockMvc.perform(patch("/map/regionupdate")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .header("Authorization", demoData.ACCESS_PARTNER)
            .header("Key", demoData.KEY_PARTNER)
            .content(objectMapper.writeValueAsString(regionUpdateDTO))
        )
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();

        RegionUpdateResponseDTO regionUpdateResponseDTO = objectMapper
            .readValue(contentAsString, RegionUpdateResponseDTO.class);

        Long updatableId = regionUpdateResponseDTO.getId();

        assertEquals(shopId, updatableId);

        Optional<Shop> byId = shopRepository.findById(shopId);
        assertNotNull(byId.get().getRegion());
    }

    @Test
    @SneakyThrows
    @Transactional
    public void statsTest() {
        Shop shop = demoData.shops.get(0);
        ShopStatsDTO statsDTO = ShopStatsDTO.builder()
            .id(shop.getId())
            .endDate(Instant.now())
            .startDate(Instant.now().minusMillis(10_000_000L))
            .build();

        String content = mockMvc.perform(post("/shop/stats")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .header("Authorization", demoData.ACCESS_PARTNER)
            .header("Key", demoData.KEY_PARTNER)
            .content(objectMapper.writeValueAsString(statsDTO))
        )
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();

        ShopStatsResponseDTO shopStatsResponseDTO = objectMapper
            .readValue(content, ShopStatsResponseDTO.class);

        assertTrue(shopStatsResponseDTO.getProfit() > 0);
    }

    @Test
    @SneakyThrows
    @Transactional
    public void findAllByPage() {
        Long cityId = demoData.cities.get(0).getId();

        ShopPageRequestDTO requestDTO = ShopPageRequestDTO.builder()
            .cityId(cityId)
            .build();

        String thirdContent = mockMvc.perform(post("/shop/page?size=100")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestDTO))
        )
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();

        ShopPageDTO pageByCity = objectMapper
            .readValue(thirdContent, ShopPageDTO.class);
        List<ShopDTO> shops1 = pageByCity.getShops();

        assertEquals(shops1.size(), 3);
    }
}

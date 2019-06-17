package dev.muskrat.delivery.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.muskrat.delivery.dao.mapping.RegionDelivery;
import dev.muskrat.delivery.dao.shop.Shop;
import dev.muskrat.delivery.dao.shop.ShopRepository;
import dev.muskrat.delivery.dto.mapping.RegionUpdateDTO;
import dev.muskrat.delivery.dto.mapping.RegionUpdateResponseDTO;
import dev.muskrat.delivery.dto.shop.*;
import dev.muskrat.delivery.service.shop.ShopService;
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

import java.time.LocalTime;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
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

    @SneakyThrows
    private ShopCreateResponseDTO createTestItem(String shopName) {
        ShopCreateDTO createDTO = ShopCreateDTO.builder()
                .name(shopName)
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

    @Test
    @SneakyThrows
    @Transactional
    public void ShopCreateTest() {
        String shopName = "test" + ThreadLocalRandom.current().nextInt();
        ShopCreateResponseDTO item = createTestItem(shopName);

        Long createdShopId = item.getId();

        ShopDTO createdShopDTO = shopService
                .findById(createdShopId).orElseThrow();

        assertEquals(createdShopDTO.getId(), createdShopId);
        assertEquals(createdShopDTO.getName(), shopName);
    }

    @Test
    @SneakyThrows
    @Transactional
    public void shopUpdateDTO() {
        ShopCreateResponseDTO item = createTestItem("test");

        Long createdShopId = item.getId();

        ShopUpdateDTO updateDTO = ShopUpdateDTO.builder()
                .id(createdShopId)
                .description("description")
                .freeOrderPrice(10D)
                .minOrderPrice(5D)
                .logo("logo")
                .name("new name")
                .build();

        String contentAsString = mockMvc.perform(patch("/shop/update")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
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
        assertEquals(updateDTO.getFreeOrderPrice(), updatedShopDTO.getFreeOrderPrice());
        assertEquals(updateDTO.getMinOrderPrice(), updatedShopDTO.getMinOrderPrice());
        assertEquals(updateDTO.getLogo(), updatedShopDTO.getLogo());
        assertEquals(updateDTO.getName(), "new name");
    }

    @Test
    @SneakyThrows
    public void shopScheduleUpdateDTO() {
        ShopCreateResponseDTO item = createTestItem("test");

        Long createdShopId = item.getId();

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
                .content(objectMapper.writeValueAsString(updateDTO))
        )
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        ShopScheduleResponseDTO productUpdateDTO = objectMapper
                .readValue(contentAsString, ShopScheduleResponseDTO.class);

        Long updatedProductId = productUpdateDTO.getId();

        ShopScheduleDTO updatedShopDTO = shopService
                .findScheduleById(updatedProductId).orElseThrow();

        assertEquals(updateDTO.getId(), updatedShopDTO.getId());
        assertEquals(updateDTO.getOpenTimeList(), updatedShopDTO.getOpen());
        assertEquals(updateDTO.getCloseTimeList(), updatedShopDTO.getClose());
    }

    @Test
    @SneakyThrows
    public void shopDeleteTest() {
        ShopCreateResponseDTO item = createTestItem("test");

        Long itemId = item.getId();

        mockMvc.perform(delete("/shop/" + itemId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Optional<ShopDTO> byId = shopService.findById(itemId);
        assertTrue(byId.isEmpty());
    }

    @Test
    @SneakyThrows
    @Transactional
    public void regionUpdateDTO() {
        ShopCreateResponseDTO item = createTestItem("test");

        Long itemId = item.getId();

        RegionUpdateDTO regionUpdateDTO = RegionUpdateDTO.builder()
            .shopId(itemId)
            .points(Arrays.asList(
                1D, 2D, 3D, 4D, 5D, 6D, 7D, 8D, 9D
            )).build();

        String contentAsString = mockMvc.perform(patch("/map/regionupdate")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(regionUpdateDTO))
        )
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();

        RegionUpdateResponseDTO regionUpdateResponseDTO = objectMapper
            .readValue(contentAsString, RegionUpdateResponseDTO.class);

        Long updatableId = regionUpdateResponseDTO.getId();

        assertEquals(itemId, updatableId);

        Optional<Shop> byId = shopRepository.findById(itemId);
        Shop shop = byId.get();
        RegionDelivery region = shop.getRegion();

        assertNotNull(region);
    }
}

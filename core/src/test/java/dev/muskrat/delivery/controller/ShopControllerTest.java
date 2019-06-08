package dev.muskrat.delivery.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    private ObjectMapper objectMapper;

    @SneakyThrows
    private ShopCreateResponseDTO createTestItem() {
        ShopCreateDTO createDTO = ShopCreateDTO.builder()
                .name("test")
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
    public void ShopCreateTest() {
        ShopCreateResponseDTO item = createTestItem();

        Long createdShopId = item.getId();

        ShopDTO createdShopDTO = shopService
                .findById(createdShopId).orElseThrow();

        assertEquals(createdShopDTO.getId(), createdShopId);
        assertEquals(createdShopDTO.getName(), "test");
    }

    @Test
    @SneakyThrows
    public void shopUpdateDTO() {
        ShopCreateResponseDTO item = createTestItem();

        Long createdShopId = item.getId();

        List<WorkDayDTO> workDayDTO = Arrays.asList(
                new WorkDayDTO(LocalTime.of(9, 0), LocalTime.of(22, 0)),
                new WorkDayDTO(LocalTime.of(9, 0), LocalTime.of(22, 0)),
                new WorkDayDTO(LocalTime.of(9, 0), LocalTime.of(22, 0)),
                //new WorkDayDTO(LocalTime.of(9, 0), LocalTime.of(22, 0)),
                new WorkDayDTO(LocalTime.of(9, 0), LocalTime.of(22, 0)),
                new WorkDayDTO(LocalTime.of(9, 0), LocalTime.of(22, 0)),
                new WorkDayDTO(LocalTime.of(9, 0), LocalTime.of(22, 0))
        );

        ShopUpdateDTO updateDTO = ShopUpdateDTO.builder()
                .id(createdShopId)
                .description("description")
                .freeOrder(10F)
                .logo("logo")
                .minOrder(5F)
                // TODO .region()
                .schedule(workDayDTO)
                .visible(true)
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
        assertEquals(updateDTO.getFreeOrder(), updatedShopDTO.getFreeOrder());
        assertEquals(updateDTO.getMinOrder(), updatedShopDTO.getMinOrder());
        assertEquals(updateDTO.getLogo(), updatedShopDTO.getLogo());
        assertEquals(updateDTO.getVisible(), updatedShopDTO.getVisible());
        assertEquals(updateDTO.getSchedule(), updateDTO.getSchedule());
        assertEquals(updateDTO.getRegion(), updateDTO.getRegion());
        assertEquals(updateDTO.getName(), "new name");
    }
}

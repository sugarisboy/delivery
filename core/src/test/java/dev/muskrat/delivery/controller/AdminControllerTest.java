package dev.muskrat.delivery.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.muskrat.delivery.DemoData;
import dev.muskrat.delivery.admin.dto.AdminStatsDTO;
import dev.muskrat.delivery.admin.dto.AdminStatsResponseDTO;
import dev.muskrat.delivery.shop.dao.Shop;
import dev.muskrat.delivery.shop.dto.ShopStatsDTO;
import dev.muskrat.delivery.shop.dto.ShopStatsResponseDTO;
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

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DemoData demoData;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @SneakyThrows
    @Transactional
    public void statsTest() {
        AdminStatsDTO statsDTO = AdminStatsDTO.builder()
            .endDate(Instant.now())
            .startDate(Instant.now().minusMillis(10_000_000L))
            .build();

        String content = mockMvc.perform(post("/admin/stats")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .header("Authorization", demoData.ACCESS_ADMIN)
            .header("Key", demoData.KEY_ADMIN)
            .content(objectMapper.writeValueAsString(statsDTO))
        )
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();

        AdminStatsResponseDTO shopStatsResponseDTO = objectMapper
            .readValue(content, AdminStatsResponseDTO.class);

        assertTrue(shopStatsResponseDTO.getProfit() > 0);
    }
}

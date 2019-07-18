package dev.muskrat.delivery.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import dev.muskrat.delivery.partner.dto.PartnerDTO;
import dev.muskrat.delivery.partner.dto.PartnerRegisterDTO;
import dev.muskrat.delivery.partner.dto.PartnerRegisterResponseDTO;
import dev.muskrat.delivery.partner.service.PartnerService;
import lombok.SneakyThrows;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@Transactional
public class PartnerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PartnerService partnerService;

    @Autowired
    private ObjectMapper objectMapper;

    private PartnerRegisterDTO registerDTO() {
        return PartnerRegisterDTO.builder()
            .name("test")
            .email("test@test.te")
            .password("123")
            .passwordRepeat("123")
            .phone("123")
            .build();
    }

    @Test
    @SneakyThrows
    public void partnerRegistrationTest() {
        String contentAsString = mockMvc.perform(post("/partner/register")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(registerDTO()))
        )
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();

        PartnerRegisterResponseDTO partnerRegisterResponseDTO = objectMapper
            .readValue(contentAsString, PartnerRegisterResponseDTO.class);

        Long registeredPartnerId = partnerRegisterResponseDTO.getId();

        PartnerDTO partnerDTO = partnerService
            .findById(registeredPartnerId).orElseThrow();

        assertEquals(partnerDTO.getId(), registeredPartnerId);
        assertEquals(partnerDTO.getEmail(), "test@test.te");
    }

    /*@Test
    @SneakyThrows
    public void partnerDeleteTest() {
        String contentAsString = mockMvc.perform(post("/partner/register")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerDTO()))
        )
                .andExpect(status().isOk())
                .andReturn().getRequest().getContentAsString();

        PartnerRegisterResponseDTO partnerRegisterResponseDTO = objectMapper
                .readValue(contentAsString, PartnerRegisterResponseDTO.class);

        Long itemId = partnerRegisterResponseDTO.getId();

        mockMvc.perform(delete("/partner/" + itemId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Optional<PartnerDTO> byId = partnerService.findById(itemId);
        assertTrue(byId.isEmpty());
    }*/
}

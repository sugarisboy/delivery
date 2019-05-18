package dev.muskrat.delivery.controller;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
//@ExtendWith(SpringExtension.class)
class PartnerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @SneakyThrows
    void testControllerReturnsResponse() {
        mockMvc.perform(post("/partner/register"));
//      .andExpect()
//      .andReturn();
    }
}
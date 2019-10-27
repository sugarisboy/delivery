package dev.muskrat.delivery.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.muskrat.delivery.DemoData;
import dev.muskrat.delivery.payment.PaymentSystem;
import dev.muskrat.delivery.payment.dto.PaymentSystemCreateDTO;
import dev.muskrat.delivery.payment.dto.PaymentSystemDTO;
import dev.muskrat.delivery.payment.dto.PaymentSystemResponseDTO;
import dev.muskrat.delivery.payment.service.PaymentsServiceImpl;
import dev.muskrat.delivery.product.dao.Category;
import dev.muskrat.delivery.product.dao.Product;
import dev.muskrat.delivery.product.dto.*;
import dev.muskrat.delivery.product.service.ProductService;
import dev.muskrat.delivery.shop.dao.Shop;
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
import java.util.Optional;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class PaymentSystemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PaymentsServiceImpl paymentsService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DemoData demoData;

    @Test
    @SneakyThrows
    @Transactional
    public void paymentSystemCreateTest() {
        PaymentSystemCreateDTO paymentSystemCreateDTO = PaymentSystemCreateDTO.builder()
            .name("cash")
            .active(true)
            .online(false)
            .build();

        String contentAsString = mockMvc.perform(post("/payment/create")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .header("Authorization", demoData.ACCESS_ADMIN)
            .header("Key", demoData.KEY_ADMIN)
            .content(objectMapper.writeValueAsString(paymentSystemCreateDTO))
        )
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();

        PaymentSystemResponseDTO productCreateResponseDTO = objectMapper
            .readValue(contentAsString, PaymentSystemResponseDTO.class);

        Long createdPaymentId = productCreateResponseDTO.getId();

        PaymentSystemDTO paymentSystemDTO = paymentsService
            .findById(createdPaymentId);

        assertNotNull(paymentSystemDTO.getOnline());
        assertNotNull(paymentSystemDTO.getActive());
        assertEquals("cash", paymentSystemDTO.getName());
    }

    @Test
    @SneakyThrows
    @Transactional
    public void paymentSystemGetTest() {
        Long systemId = demoData.paymentSystemCash.getId();

        String contentAsString = mockMvc.perform(get("/payment/get/" + systemId)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .header("Authorization", demoData.ACCESS_ADMIN)
            .header("Key", demoData.KEY_ADMIN)
        )
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();

        PaymentSystemDTO productCreateResponseDTO = objectMapper
            .readValue(contentAsString, PaymentSystemDTO.class);

        assertNotNull(productCreateResponseDTO.getOnline());
        assertNotNull(productCreateResponseDTO.getActive());
        assertNotNull(productCreateResponseDTO.getName());
    }
}

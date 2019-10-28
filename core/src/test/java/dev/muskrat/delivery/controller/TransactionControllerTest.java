package dev.muskrat.delivery.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.muskrat.delivery.DemoData;
import dev.muskrat.delivery.order.dao.Order;
import dev.muskrat.delivery.payment.dao.PaymentSystemEntity;
import dev.muskrat.delivery.payment.dao.Transaction;
import dev.muskrat.delivery.payment.dto.*;
import dev.muskrat.delivery.payment.service.PaymentsServiceImpl;
import dev.muskrat.delivery.payment.service.TransactionService;
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
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DemoData demoData;

    @Test
    @SneakyThrows
    @Transactional
    public void transactionCreateTest() {
        PaymentSystemEntity paymentSystemCash = demoData.paymentSystemCash;
        Order order = demoData.orders.get(0);

        TransactionCreateDTO createDTO = TransactionCreateDTO.builder()
            .orderId(order.getId())
            .paymentsSystemId(paymentSystemCash.getId())
            .price(100D)
            .build();

        String contentAsString = mockMvc.perform(post("/transaction/create")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .header("Authorization", demoData.ACCESS_ADMIN)
            .header("Key", demoData.KEY_ADMIN)
            .content(objectMapper.writeValueAsString(createDTO))
        )
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();

        TransactionResponseDTO responseDTO = objectMapper
            .readValue(contentAsString, TransactionResponseDTO.class);

        Long createTransactionId = responseDTO.getId();

        TransactionDTO transactionDTO = transactionService
            .findById(createTransactionId);

        assertNotNull(transactionDTO.getIsPaid());
        assertNotNull(transactionDTO.getOrderId());
        assertNotNull(transactionDTO.getPaymentsSystemId());
        assertNotNull(transactionDTO.getPrice());
    }

    @Test
    @SneakyThrows
    @Transactional
    public void paymentSystemGetTest() {
        Transaction transaction = demoData.transaction;
        Long transactionId = transaction.getId();
        Long orderId = demoData.orders.get(0).getId();

        String contentAsString = mockMvc.perform(get("/transaction/get/" + transactionId)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .header("Authorization", demoData.ACCESS_ADMIN)
            .header("Key", demoData.KEY_ADMIN)
        )
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();

        TransactionDTO transactionDTO = objectMapper
            .readValue(contentAsString, TransactionDTO.class);

        assertNotNull(transactionDTO.getPrice());
        assertNotNull(transactionDTO.getPaymentsSystemId());
        assertNotNull(transactionDTO.getOrderId());
        assertNotNull(transactionDTO.getIsPaid());


        contentAsString = mockMvc.perform(get("/transaction/get/order/" + orderId)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .header("Authorization", demoData.ACCESS_ADMIN)
            .header("Key", demoData.KEY_ADMIN)
        )
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();

        transactionDTO = objectMapper
            .readValue(contentAsString, TransactionDTO.class);

        assertNotNull(transactionDTO.getPrice());
        assertNotNull(transactionDTO.getPaymentsSystemId());
        assertNotNull(transactionDTO.getOrderId());
        assertNotNull(transactionDTO.getIsPaid());
    }
}

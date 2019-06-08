package dev.muskrat.delivery.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.muskrat.delivery.dto.order.OrderCreateDTO;
import dev.muskrat.delivery.dto.order.OrderDTO;
import dev.muskrat.delivery.dto.order.OrderProductDTO;
import dev.muskrat.delivery.dto.product.ProductCreateDTO;
import dev.muskrat.delivery.dto.product.ProductCreateResponseDTO;
import dev.muskrat.delivery.dto.shop.ShopCreateResponseDTO;
import dev.muskrat.delivery.service.order.OrderService;
import lombok.SneakyThrows;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ObjectMapper objectMapper;

    @SneakyThrows
    private ProductCreateResponseDTO createTestableProduct(String title) {
        ProductCreateDTO productCreateDTO = ProductCreateDTO.builder()
            .title(title)
            .category(1L)
            .price(20D)
            .build();

        String contentAsString = mockMvc.perform(post("/product/create")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(productCreateDTO))
        )
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();

        return objectMapper
            .readValue(contentAsString, ProductCreateResponseDTO.class);
    }

    @SneakyThrows
    private OrderCreateDTO createDTO() {
        ProductCreateResponseDTO first = createTestableProduct("first");
        ProductCreateResponseDTO second = createTestableProduct("second");

        List<OrderProductDTO> products = Arrays.asList(
            OrderProductDTO.builder().productId(first.getId()).count(1).build(),
            OrderProductDTO.builder().productId(second.getId()).count(2).build()
        );

        return OrderCreateDTO.builder()
            .name("Ivan Ivanov")
            .address("street")
            .comment("no comments")
            .email("sugarisboy@outlook.com")
            .phone("79201213333")
            .shopId(2L)
            .products(products)
            .build();
    }

    @SneakyThrows
    private MockHttpServletResponse createTestableOrder(OrderCreateDTO orderCreateDTO) {
        return mockMvc.perform(post("/order/create")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(orderCreateDTO))
        )
            .andExpect(status().isOk())
            .andReturn().getResponse();
    }

    @Test
    @SneakyThrows
    public void OrderCreateSuccessfulTest() {
        MockHttpServletResponse response = createTestableOrder(createDTO());

        ShopCreateResponseDTO item = objectMapper
            .readValue(response.getContentAsString(), ShopCreateResponseDTO.class);

        Long createdOrderId = item.getId();

        OrderDTO createdItem = orderService
            .findById(createdOrderId).orElseThrow();

        assertEquals(createdItem.getId(), createdOrderId);
        assertTrue(createdItem.getStatus() == 0);
    }

    @Test
    @SneakyThrows
    public void OrderCreateTest() {
        OrderCreateDTO dto = createDTO();
        dto.setAddress(null);
        System.out.println(createTestableOrder(dto).getContentAsString());
    }

    @Test
    @SneakyThrows
    public void UpdateStatusTest() {
        // TODO
    }


}

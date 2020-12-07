package dev.muskrat.delivery.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.muskrat.delivery.DemoData;
import dev.muskrat.delivery.cities.dao.City;
import dev.muskrat.delivery.order.dao.Order;
import dev.muskrat.delivery.order.dao.OrderRepository;
import dev.muskrat.delivery.order.dao.OrderStatusRepository;
import dev.muskrat.delivery.order.dto.*;
import dev.muskrat.delivery.order.service.OrderService;
import dev.muskrat.delivery.shop.dao.Shop;
import dev.muskrat.delivery.shop.dto.ShopCreateResponseDTO;
import dev.muskrat.delivery.validations.dto.ValidationExceptionDTO;
import lombok.SneakyThrows;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    @Autowired
    private DemoData demoData;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderStatusRepository orderStatusRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @SneakyThrows
    @Transactional
    public void orderGetPageTest() {
        City city = demoData.cities.get(0);
        Long cityId = city.getId();

        String email = demoData.orders.get(0).getEmail();

        OrderPageRequestDTO requestDTO = OrderPageRequestDTO.builder()
            .cityId(cityId)
            .email(email)
            .build();

        MockHttpServletResponse response = mockMvc.perform(post("/order/page?size=1000")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestDTO))
            .header("Authorization", demoData.ACCESS_ADMIN)
            .header("Key", demoData.KEY_ADMIN)
        )
            .andExpect(status().isOk())
            .andReturn().getResponse();

        OrderPageDTO pageDTO = objectMapper
            .readValue(response.getContentAsString(), OrderPageDTO.class);

        assertEquals(9, pageDTO.getOrders().size());
    }

    @Test
    @SneakyThrows
    @Transactional
    public void cancelOrder() {
        Order order = demoData.orders.get(0);
        Long orderId = order.getId();

        MockHttpServletResponse response = mockMvc.perform(get("/order/cancel/" + orderId)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .header("Authorization", demoData.ACCESS_USER)
            .header("Key", demoData.KEY_USER)
        )
            .andExpect(status().isOk())
            .andReturn().getResponse();

        OrderDTO orderDTO = objectMapper
            .readValue(response.getContentAsString(), OrderDTO.class);

        Order updated = orderRepository.findById(orderId).get();

        assertEquals(11, updated.getStatus().intValue());
    }

    @Test
    @SneakyThrows
    public void orderCreateSuccessfulTest() {
        Shop shop = demoData.shops.get(0);
        Long shopId = shop.getId();

        List<OrderProductDTO> products = shop.getProducts().stream()
            .map(dto -> OrderProductDTO.builder()
                .productId(dto.getId())
                .count(1)
                .build())
            .collect(Collectors.toList());

        OrderCreateDTO orderCreateDTO = OrderCreateDTO.builder()
            .name("Ivan Ivanov")
            .address("Jalan Teoh Kim Swee, 4")
            .comment("no comments")
            .email("sugarisboy@outlook.com")
            .phone("7920121333" + ThreadLocalRandom.current().nextInt(2))
            .shopId(shopId)
            .products(products)
            .build();

        MockHttpServletResponse response = mockMvc.perform(post("/order/create")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(orderCreateDTO))
        )
            .andExpect(status().isOk())
            .andReturn().getResponse();

        ShopCreateResponseDTO item = objectMapper
            .readValue(response.getContentAsString(), ShopCreateResponseDTO.class);

        Long createdOrderId = item.getId();

        OrderDTO createdItem = orderService
            .findById(createdOrderId).orElseThrow(NullPointerException::new);

        assertEquals(createdItem.getId(), createdOrderId);
    }

    @Test
    @SneakyThrows
    @Transactional
    public void orderCreateWithBadEmailTest() {
        Shop shop = demoData.shops.get(0);
        Long shopId = shop.getId();

        List<OrderProductDTO> products = shop.getProducts().stream()
            .map(dto -> OrderProductDTO.builder()
                .productId(dto.getId())
                .count(1)
                .build())
            .collect(Collectors.toList());

        OrderCreateDTO orderCreateDTO = OrderCreateDTO.builder()
            .name("Ivan Ivanov")
            .address("Jalan Teoh Kim Swee, 4")
            .comment("no comments")
            .email("notemail")
            .phone("7920121333" + ThreadLocalRandom.current().nextInt(2))
            .shopId(shopId)
            .products(products)
            .build();


        MockHttpServletResponse response = mockMvc.perform(post("/order/create")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(orderCreateDTO))
        )
            .andExpect(status().isBadRequest())
            .andReturn().getResponse();

        ValidationExceptionDTO validationExceptionDTO = objectMapper
            .readValue(response.getContentAsString(), ValidationExceptionDTO.class);

        assertEquals(validationExceptionDTO.getField(), "email");
    }

    @Test
    @SneakyThrows
    @Transactional
    public void updateStatusTest() {
        Shop shop = demoData.shops.get(1);
        Long shopId = shop.getId();

        OrderUpdateDTO updateDTO = OrderUpdateDTO.builder()
            .id(shopId)
            .status(10)
            .build();

        MockHttpServletResponse response = mockMvc.perform(patch("/order/update")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .header("Authorization", demoData.ACCESS_ADMIN)
            .header("Key", demoData.KEY_ADMIN)
            .content(objectMapper.writeValueAsString(updateDTO))
        )
            .andExpect(status().isOk())
            .andReturn().getResponse();

        OrderDTO updatedItem = objectMapper
            .readValue(response.getContentAsString(), OrderDTO.class);

        boolean flag = updatedItem.getStatus().stream()
            .anyMatch(i -> i.getStatus() == 10L);
        assertTrue(flag);
    }

    @Test
    @SneakyThrows
    @Transactional
    public void orderGetTest() {
        Date startTime = new Date();

        Order order = demoData.orders.get(0);
        Long orderId = order.getId();

        MockHttpServletResponse responseById = mockMvc
            .perform(get("/order/" + orderId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", demoData.ACCESS_ADMIN)
                .header("Key", demoData.KEY_ADMIN)
            )
            .andExpect(status().isOk())
            .andReturn().getResponse();
        OrderDTO responseDTO = objectMapper
            .readValue(responseById.getContentAsString(), OrderDTO.class);

        Date createdTime = responseDTO.getCreatedTime();

        assertTrue(startTime.getTime() - createdTime.getTime() < 10_000);
        assertNotNull(responseDTO.getCost());
        assertNotNull(responseDTO.getCostAndDelivery());
        assertEquals(responseDTO.getId(), orderId);
    }
}

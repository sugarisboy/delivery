package dev.muskrat.delivery.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.muskrat.delivery.DemoData;
import dev.muskrat.delivery.cities.dao.CitiesRepository;
import dev.muskrat.delivery.cities.dao.City;
import dev.muskrat.delivery.map.dto.RegionUpdateDTO;
import dev.muskrat.delivery.order.dao.Order;
import dev.muskrat.delivery.order.dao.OrderRepository;
import dev.muskrat.delivery.order.dto.*;
import dev.muskrat.delivery.order.service.OrderService;
import dev.muskrat.delivery.product.dto.ProductCreateDTO;
import dev.muskrat.delivery.product.dto.ProductCreateResponseDTO;
import dev.muskrat.delivery.shop.dto.ShopCreateDTO;
import dev.muskrat.delivery.shop.dto.ShopCreateResponseDTO;
import dev.muskrat.delivery.validations.dto.ValidationExceptionDTO;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
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

        MockHttpServletResponse response = mockMvc.perform(get("/order/page?size=1000")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestDTO))
        )
            .andExpect(status().isOk())
            .andReturn().getResponse();

        OrderPageDTO pageDTO = objectMapper
            .readValue(response.getContentAsString(), OrderPageDTO.class);

        assertEquals(6, pageDTO.getOrders().size());
    }

    @Test
    @SneakyThrows
    @Transactional
    public void orderCreateSuccessfulTest() {
        MockHttpServletResponse response = mockMvc.perform(post("/order/create")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(createDTO()))
        )
            .andExpect(status().isOk())
            .andReturn().getResponse();

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
    @Transactional
    public void orderCreateWithBadEmailTest() {
        OrderCreateDTO dto = createDTO();
        dto.setEmail("notemail");

        MockHttpServletResponse response = mockMvc.perform(post("/order/create")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dto))
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
    public void createWithoutDataTest() {
        String[] badFields = {"products", "shopId", "email", "phone", "name", "address"};

        for (int i = 0; i < badFields.length; i++) {
            OrderCreateDTO dto = createDTO();

            if (i == 0) dto.setProducts(null);
            else if (i == 1) dto.setShopId(null);
            else if (i == 2) dto.setEmail(null);
            else if (i == 3) dto.setPhone(null);
            else if (i == 4) dto.setName(null);
            else if (i == 5) dto.setAddress(null);

            MockHttpServletResponse response = mockMvc.perform(post("/order/create")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
            )
                .andExpect(status().isBadRequest())
                .andReturn().getResponse();

            ValidationExceptionDTO validationExceptionDTO = objectMapper
                .readValue(response.getContentAsString(), ValidationExceptionDTO.class);

            assertEquals(validationExceptionDTO.getField(), badFields[i]);
        }
    }

    @Test
    @SneakyThrows
    @Transactional
    public void updateStatusTest() {
        MockHttpServletResponse response = mockMvc.perform(post("/order/create")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(createDTO()))
        )
            .andExpect(status().isOk())
            .andReturn().getResponse();

        ShopCreateResponseDTO item = objectMapper
            .readValue(response.getContentAsString(), ShopCreateResponseDTO.class);

        Long createdOrderId = item.getId();

        OrderUpdateDTO updateDTO = OrderUpdateDTO.builder()
            .id(createdOrderId)
            .status(10)
            .build();

        response = mockMvc.perform(patch("/order/update")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updateDTO))
        )
            .andExpect(status().isOk())
            .andReturn().getResponse();

        OrderDTO updatedItem = objectMapper
            .readValue(response.getContentAsString(), OrderDTO.class);

        assertTrue(updatedItem.getStatus() == 10L);
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
            )
            .andExpect(status().isOk())
            .andReturn().getResponse();
        OrderDTO responseDTO = objectMapper
            .readValue(responseById.getContentAsString(), OrderDTO.class);

        Date createdTime = responseDTO.getCreatedTime();

        assertTrue(startTime.getTime() - createdTime.getTime() < 10_000);
        assertEquals(responseDTO.getId(), orderId);
    }

    @SneakyThrows
    private void shopRegionSet(ShopCreateResponseDTO item) {
        Long itemId = item.getId();

        RegionUpdateDTO regionUpdateDTO = RegionUpdateDTO.builder()
            .shopId(itemId)
            .points(Arrays.asList(
                4.5272D, 101.1638D, 100D,
                4.6335D, 101.1250D, 100D,
                4.5452D, 101.0834D, 100D,
                4.5272D, 101.1638D, 100D
            )).build();

        mockMvc.perform(patch("/map/regionupdate")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(regionUpdateDTO))
        )
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();
    }

    @SneakyThrows
    private ProductCreateResponseDTO createTestableProduct(String title, Long shopId) {
        ProductCreateDTO productCreateDTO = ProductCreateDTO.builder()
            .title(title)
            .category(demoData.categories.get(0).getId())
            .price(20D)
            .shopId(shopId)
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
    private ShopCreateResponseDTO createTestableShop() {
        ShopCreateDTO createDTO = ShopCreateDTO.builder()
            .name("shop" + ThreadLocalRandom.current().nextInt())
            .cityId(demoData.cities.get(0).getId())
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

    @SneakyThrows
    private OrderCreateDTO createDTO() {
        ShopCreateResponseDTO testableShop = createTestableShop();
        shopRegionSet(testableShop);
        Long shopId = testableShop.getId();

        ProductCreateResponseDTO first = createTestableProduct("first", shopId);
        ProductCreateResponseDTO second = createTestableProduct("second", shopId);

        List<OrderProductDTO> products = Arrays.asList(
            OrderProductDTO.builder().productId(first.getId()).count(1).build(),
            OrderProductDTO.builder().productId(second.getId()).count(2).build()
        );

        return OrderCreateDTO.builder()
            .name("Ivan Ivanov")
            .address("Jalan Teoh Kim Swee, 4")
            .comment("no comments")
            .email("sugarisboy@outlook.com")
            .phone("7920121333" + ThreadLocalRandom.current().nextInt(2))
            .shopId(shopId)
            .products(products)
            .build();
    }
}

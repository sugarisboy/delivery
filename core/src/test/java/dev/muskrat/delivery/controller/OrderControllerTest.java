package dev.muskrat.delivery.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.muskrat.delivery.dto.ValidationExceptionDTO;
import dev.muskrat.delivery.dto.mapping.RegionUpdateDTO;
import dev.muskrat.delivery.dto.order.OrderCreateDTO;
import dev.muskrat.delivery.dto.order.OrderDTO;
import dev.muskrat.delivery.dto.order.OrderProductDTO;
import dev.muskrat.delivery.dto.order.OrderUpdateDTO;
import dev.muskrat.delivery.dto.product.ProductCreateDTO;
import dev.muskrat.delivery.dto.product.ProductCreateResponseDTO;
import dev.muskrat.delivery.dto.shop.ShopCreateDTO;
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
import org.springframework.util.DigestUtils;

import javax.transaction.Transactional;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
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
            .category(1L)
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
            .phone("79201213333")
            .shopId(shopId)
            .products(products)
            .build();
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

        response = mockMvc.perform(patch("/order/update/status")
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
    public void OrderCreateGetTest() {
        MockHttpServletResponse response = mockMvc.perform(post("/order/create")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(createDTO()))
        )
            .andExpect(status().isOk())
            .andReturn().getResponse();
        ShopCreateResponseDTO item = objectMapper
            .readValue(response.getContentAsString(), ShopCreateResponseDTO.class);
        Long orderId = item.getId();

        MockHttpServletResponse responseById = mockMvc
            .perform(get("/order/" + orderId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andReturn().getResponse();
        OrderDTO responseDTO = objectMapper
            .readValue(responseById.getContentAsString(), OrderDTO.class);
        assertEquals(responseDTO.getId(), orderId);

        MockHttpServletResponse responseByEmail = mockMvc
            .perform(get("/order/email/" + "sugarisboy@outlook.com")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andReturn().getResponse();
        List<OrderDTO> responseList = objectMapper
            .readValue(
                responseByEmail.getContentAsString(),
                objectMapper.getTypeFactory().constructCollectionType(List.class, OrderDTO.class)
            );
        assertEquals(responseList.get(0).getEmail(), "sugarisboy@outlook.com");

        MockHttpServletResponse responseByShop = mockMvc
            .perform(get("/order/shop/" + responseDTO.getShop())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andReturn().getResponse();

        responseList = objectMapper
            .readValue(
                responseByShop.getContentAsString(),
                objectMapper.getTypeFactory().constructCollectionType(List.class, OrderDTO.class)
            );
        assertEquals(responseList.get(0).getShop(), responseDTO.getShop());
    }
}

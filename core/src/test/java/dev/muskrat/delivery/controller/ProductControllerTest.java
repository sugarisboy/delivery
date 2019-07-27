package dev.muskrat.delivery.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.muskrat.delivery.DemoData;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DemoData demoData;

    @Test
    @SneakyThrows
    @Transactional
    public void productCreateTest() {
        Shop shop = demoData.shops.get(0);
        Category category = demoData.categories.get(0);

        ProductCreateDTO productCreateDTO = ProductCreateDTO.builder()
            .title("title")
            .price(20D)
            .shopId(shop.getId())
            .category(category.getId())
            .build();

        String contentAsString = mockMvc.perform(post("/product/create")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(productCreateDTO))
        )
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();

        ProductCreateResponseDTO productCreateResponseDTO = objectMapper
            .readValue(contentAsString, ProductCreateResponseDTO.class);

        Long createdProductId = productCreateResponseDTO.getId();

        ProductDTO productDTO = productService
            .findById(createdProductId).orElseThrow();

        assertEquals(productDTO.getId(), createdProductId);
        assertEquals(productDTO.getTitle(), "title");
    }

    @Test
    @SneakyThrows
    @Transactional
    public void productUpdateTest() {
        Product product = demoData.products.get(0);
        Category category = demoData.categories.get(1);
        Long productId = product.getId();

        ProductUpdateDTO updateDTO = ProductUpdateDTO.builder()
            .id(productId)
            .description("description")
            .price(100D)
            .category(category.getId())
            .title("new title")
            .value(0D)
            .build();

        String contentAsString = mockMvc.perform(patch("/product/update")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updateDTO))
        )
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

        ProductUpdateResponseDTO productUpdateDTO = objectMapper
            .readValue(contentAsString, ProductUpdateResponseDTO.class);

        Long updatedProductId = productUpdateDTO.getId();
        ProductDTO productDTO = productService
            .findById(updatedProductId).orElseThrow();

        assertEquals(productDTO.getId(), updatedProductId);
        assertEquals(productDTO.getTitle(), updateDTO.getTitle());
        assertEquals(productDTO.getDescription(), updateDTO.getDescription());
        assertEquals(productDTO.getPrice(), updateDTO.getPrice());
        assertEquals(productDTO.getCategory(), updateDTO.getCategory());
        assertEquals(productDTO.getValue(), updateDTO.getValue());
    }

    @Test
    @SneakyThrows
    @Transactional
    public void productDeleteTest() {
        Product product = demoData.products.get(0);
        Long productId = product.getId();

        mockMvc.perform(delete("/product/" + productId)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();

        Optional<ProductDTO> byId = productService.findById(productId);
        assertTrue(byId.isEmpty());
    }
}

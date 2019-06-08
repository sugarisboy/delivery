package dev.muskrat.delivery.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.muskrat.delivery.dto.product.*;
import dev.muskrat.delivery.service.product.ProductService;
import lombok.SneakyThrows;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

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

    private ProductCreateDTO productDTO() {
        return ProductCreateDTO.builder()
                .title("title")
                .category(1L)
                .price(20D)
                .build();
    }

    @SneakyThrows
    private ProductCreateResponseDTO createTestItem() {
        String contentAsString = mockMvc.perform(post("/product/create")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productDTO()))
        )
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        return objectMapper
                .readValue(contentAsString, ProductCreateResponseDTO.class);
    }

    @Test
    @SneakyThrows
    public void productCreateTest() {
        ProductCreateResponseDTO productCreateResponseDTO = createTestItem();

        Long createdProductId = productCreateResponseDTO.getId();

        ProductDTO productDTO = productService
                .findById(createdProductId).orElseThrow();

        assertEquals(productDTO.getId(), createdProductId);
        assertEquals(productDTO.getTitle(), "title");
    }

    @Test
    @SneakyThrows
    public void productUpdateTest() {
        ProductCreateResponseDTO createResponseDTO = createTestItem();

        ProductUpdateDTO updateDTO = ProductUpdateDTO.builder()
                .id(createResponseDTO.getId())
                .description("description")
                .price(100D)
                .category(2L)
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
    public void productDeleteTest() {
        ProductCreateResponseDTO item = createTestItem();

        Long itemId = item.getId();

        mockMvc.perform(delete("/product/" + itemId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Optional<ProductDTO> byId = productService.findById(itemId);
        assertTrue(byId.isEmpty());
    }
}

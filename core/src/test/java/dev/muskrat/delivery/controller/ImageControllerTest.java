package dev.muskrat.delivery.controller;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.muskrat.delivery.DemoData;
import dev.muskrat.delivery.files.dto.FileStorageUploadDTO;
import dev.muskrat.delivery.product.dao.Product;
import dev.muskrat.delivery.shop.dao.Shop;
import lombok.SneakyThrows;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;
import java.io.File;
import java.io.FileInputStream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class ImageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DemoData demoData;

    @Test
    @SneakyThrows
    @Transactional
    public void productUpdateImgTest() {
        objectMapper.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, true);

        Product product = demoData.products.get(0);
        Long productId = product.getId();

        File img = new File(
            getClass().getClassLoader().getResource("img.jpg").getFile()
        );

        FileInputStream imageInputStream = new FileInputStream(img);

        MockMultipartFile file = new MockMultipartFile
            ("img", "anyname", MediaType.IMAGE_JPEG.getType(), imageInputStream);

        String contentAsString = mockMvc.perform(multipart("/image/product/" + productId)
            .file(file)
            .contentType(MediaType.IMAGE_JPEG)
            .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();

        FileStorageUploadDTO uploadDTO = objectMapper
            .readValue(contentAsString, FileStorageUploadDTO.class);

        mockMvc.perform(multipart("/image/product/" + productId + ".jpg")
            .file(file)
            .contentType(MediaType.IMAGE_JPEG)
        )
            .andExpect(status().isOk());
    }


    @Test
    @SneakyThrows
    @Transactional
    public void shopUpdateImgTest() {
        objectMapper.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, true);

        Shop shop = demoData.shops.get(0);
        Long shopId = shop.getId();

        File img = new File(
            getClass().getClassLoader().getResource("shop.jpg").getFile()
        );

        FileInputStream imageInputStream = new FileInputStream(img);

        MockMultipartFile file = new MockMultipartFile
            ("img", "anyname", MediaType.IMAGE_JPEG.getType(), imageInputStream);

        String contentAsString = mockMvc.perform(multipart("/image/shop/" + shopId)
            .file(file)
            .contentType(MediaType.IMAGE_JPEG)
            .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();

        FileStorageUploadDTO uploadDTO = objectMapper
            .readValue(contentAsString, FileStorageUploadDTO.class);

        mockMvc.perform(multipart("/image/shop/" + shopId + ".jpg")
            .file(file)
            .contentType(MediaType.IMAGE_JPEG)
        )
            .andExpect(status().isOk());
    }
}

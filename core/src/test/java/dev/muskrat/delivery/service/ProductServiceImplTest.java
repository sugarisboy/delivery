package dev.muskrat.delivery.service;

import dev.muskrat.delivery.dao.Product;
import dev.muskrat.delivery.dao.ProductRepository;
import dev.muskrat.delivery.dto.ProductDTO;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Transactional
@RunWith(SpringRunner.class)
public class ProductServiceImplTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    @Test
    public void createProductTest() {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setTitle("test");

        productService.create(productDTO);

        Product next = productRepository.findAll().iterator().next();
        assertNotNull(next);
        assertEquals(next.getTitle(), "test");
    }
}
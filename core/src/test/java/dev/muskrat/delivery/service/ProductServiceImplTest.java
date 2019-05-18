package dev.muskrat.delivery.service;

import dev.muskrat.delivery.dao.Product;
import dev.muskrat.delivery.dao.ProductRepository;
import dev.muskrat.delivery.dto.ProductDTO;
import org.junit.After;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
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
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProductServiceImplTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    @Test
    public void createProductTest() {
        ProductDTO productDTO = ProductDTO.builder()
                .title("test")
                .price(20D)
                .build();
        productService.create(productDTO);

        Product next = productRepository.findAll().iterator().next();
        assertNotNull(next);
        assertEquals(next.getTitle(), "test");
    }

    @Test
    public void updateProductDto() {
        ProductDTO productDTO = ProductDTO.builder()
                .title("test")
                .price(20D)
                .build();
        productService.create(productDTO);

        Product product = productRepository.findAll().get(0);
        Long id = product.getId();
        productDTO = ProductDTO.builder()
                .id(id)
                .title("testo")
                .description("fcku")
                .build();
        productService.update(productDTO);
        product = productRepository.findById(id).get();

        assertNotNull(product);
        assertEquals(product.getTitle(), "testo");
        assertEquals(product.getDescription(), "fcku");
    }

    @Test
    @After
    public void deleteProductDto() {
        ProductDTO productDTO = ProductDTO.builder()
                .title("test")
                .price(20D)
                .build();
        productService.create(productDTO);

        Product product = productRepository.findAll().get(0);
        Long id = product.getId();
        Long count = productRepository.count();
        productDTO = ProductDTO.builder()
                .id(id)
                .build();
        productService.delete(productDTO);

        assertEquals(count - 1, productRepository.count());
    }
}
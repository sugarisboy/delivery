package dev.muskrat.delivery.service;

import dev.muskrat.delivery.dao.product.Category;
import dev.muskrat.delivery.dao.product.CategoryRepository;
import dev.muskrat.delivery.dao.product.Product;
import dev.muskrat.delivery.dao.product.ProductRepository;
import dev.muskrat.delivery.dto.product.ProductDTO;
import dev.muskrat.delivery.service.product.ProductService;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.List;

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

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void createProductTest() {
        ProductDTO productDTO = ProductDTO.builder()
                .title("test")
                .category(1L)
                .price(20D)
                .build();
        productService.create(productDTO);

        Product next = productRepository.findAll().get(0);
        assertNotNull(next);
        assertEquals(next.getTitle(), "test");
    }

    @Test
    public void updateProductDto() {
        ProductDTO productDTO = ProductDTO.builder()
                .id(1L)
                .title("test")
                .price(20D)
                .category(1L)
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
    public void deleteProductDto() {
        ProductDTO productDTO = ProductDTO.builder()
                .title("test")
                .category(1L)
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

    @Test
    public void getProductByCategoriesDto() {
        Category category = categoryRepository.findById(1L).orElseThrow();
        assertEquals(category.getTitle(), "Other");

        Double[] prices = {1D, 2D, 3D};
        String[] titles = {"item1", "item2", "item3"};
        Long[] categories = {1L, 2L, 1L};

        for (int i = 0; i < 3; i++) {
            ProductDTO dto = ProductDTO.builder()
                    .price(prices[i])
                    .category(categories[i])
                    .title(titles[i])
                    .build();
            productService.create(dto);
        }

        List<Product> byCategory = productRepository.findByCategory(category);
        assertEquals(byCategory.size(), 2);
        assertEquals(byCategory.get(0).getTitle(), "item1");
        assertEquals(byCategory.get(1).getTitle(), "item3");
    }
}
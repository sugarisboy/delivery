/*
package dev.muskrat.delivery.service;

import dev.muskrat.delivery.dao.order.Order;
import dev.muskrat.delivery.dao.order.OrderRepository;
import dev.muskrat.delivery.dao.shop.ShopRepository;
import dev.muskrat.delivery.dto.order.OrderCreateDTO;
import dev.muskrat.delivery.dto.order.OrderProductDTO;
import dev.muskrat.delivery.service.order.OrderService;
import dev.muskrat.delivery.service.shop.ShopService;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Transactional
@RunWith(SpringRunner.class)
public class OrderServiceImplTest {

    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private ShopService shopService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    public void createOrderTest() {
        List<OrderProductDTO> products = Arrays.asList(
                OrderProductDTO.builder().productId(1L).count(1).build(),
                OrderProductDTO.builder().productId(2L).count(2).build()
        );

        OrderCreateDTO orderDTO = OrderCreateDTO.builder()
                .phone("+79999999999")
                .address("The best address")
                .email("best@gmail.com")
                .name("order")
                .products(products)
                .build();

        orderService.create(orderDTO);
        Order order = orderRepository.findAll().get(0);

        assertNotNull(order);
        assertEquals(order.getName(), "order");
    }
}
*/

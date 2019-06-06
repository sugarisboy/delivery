package dev.muskrat.delivery.service.order;

import dev.muskrat.delivery.converter.OrderProductDTOTOOrderProductConverter;
import dev.muskrat.delivery.dao.order.Order;
import dev.muskrat.delivery.dao.order.OrderProduct;
import dev.muskrat.delivery.dao.order.OrderRepository;
import dev.muskrat.delivery.dto.order.OrderCreateDTO;
import dev.muskrat.delivery.dto.order.OrderDTO;
import dev.muskrat.delivery.dto.order.OrderProductDTO;
import dev.muskrat.delivery.exception.OrderException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderProductDTOTOOrderProductConverter orderProductDTOTOOrderProductConverter;

    @Override
    public OrderCreateDTO create(OrderDTO orderDTO) {
        if (orderDTO.getToken() == null && (orderDTO.getPhone() == null || orderDTO.getAddress() == null))
            throw new OrderException("Wrong data");
        if (orderDTO.getProducts().size() == 0)
            throw new OrderException("Cart is clear");

        Order order = new Order();
        if (orderDTO.getToken() != null) {
            // TODO create realization for authentic user order
        } else {
            order.setAddress(orderDTO.getAddress());
            order.setPhone(orderDTO.getPhone());
            order.setComments(orderDTO.getComments());
            order.setName(orderDTO.getName());
        }

        List<OrderProductDTO> productsDTO = orderDTO.getProducts();
        List<OrderProduct> products = productsDTO.stream()
                .map(orderProductDTOTOOrderProductConverter::convert)
                .collect(Collectors.toList());
        //order.setProducts(products);

        orderRepository.save(order);

        return OrderCreateDTO.builder()
                .id(order.getId())
                .build();
    }

    @Override
    public Order findByPhone(String phone) {
        return null;
    }
}

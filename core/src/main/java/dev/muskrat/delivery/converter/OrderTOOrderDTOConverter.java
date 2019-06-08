package dev.muskrat.delivery.converter;

import dev.muskrat.delivery.dao.order.Order;
import dev.muskrat.delivery.dto.order.OrderDTO;
import dev.muskrat.delivery.dto.order.OrderProductDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrderTOOrderDTOConverter implements ObjectConverter<Order, OrderDTO> {

    private final OrderProductTOOrderProductDTOConverter orderProductTOOrderProductDTOConverter;

    @Override
    public OrderDTO convert(Order order) {
        List<OrderProductDTO> collect = order.getProducts().stream()
                .map(orderProductTOOrderProductDTOConverter::convert)
                .collect(Collectors.toList());

        return OrderDTO.builder()
                .status(order.getStatus())
                .address(order.getAddress())
                .comments(order.getComments())
                .email(order.getEmail())
                .id(order.getId())
                // TODO .shop(order.getShop().getId())
                .name(order.getName())
                .phone(order.getPhone())
                .products(collect)
                .build();
    }
}

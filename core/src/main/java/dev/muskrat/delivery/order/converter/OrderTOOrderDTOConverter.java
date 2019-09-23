package dev.muskrat.delivery.order.converter;

import dev.muskrat.delivery.components.converter.ObjectConverter;
import dev.muskrat.delivery.order.dao.Order;
import dev.muskrat.delivery.order.dto.OrderDTO;
import dev.muskrat.delivery.order.dto.OrderProductDTO;
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
            .status(order.getOrderStatus())
            .address(order.getAddress())
            .comments(order.getComments())
            .email(order.getEmail())
            .id(order.getId())
            .shopId(order.getShop().getId())
            .name(order.getName())
            .phone(order.getPhone())
            .products(collect)
            .createdTime(order.getCreated())
            .lastUpdateTime(order.getUpdated())
            .price(order.getPrice())
            .build();
    }
}

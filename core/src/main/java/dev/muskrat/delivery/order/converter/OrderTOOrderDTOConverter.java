package dev.muskrat.delivery.order.converter;

import dev.muskrat.delivery.components.converter.ObjectConverter;
import dev.muskrat.delivery.order.dao.Order;
import dev.muskrat.delivery.order.dto.OrderDTO;
import dev.muskrat.delivery.order.dto.OrderProductDTO;
import dev.muskrat.delivery.order.dto.OrderStatusEntryDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrderTOOrderDTOConverter implements ObjectConverter<Order, OrderDTO> {

    private final OrderStatusTOOrderStatusDTOConverter orderStatusTOOrderStatusDTOConverter;
    private final OrderProductTOOrderProductDTOConverter orderProductTOOrderProductDTOConverter;

    @Override
    public OrderDTO convert(Order order) {
        List<OrderProductDTO> collect = order.getProducts().stream()
            .map(orderProductTOOrderProductDTOConverter::convert)
            .collect(Collectors.toList());

        List<OrderStatusEntryDTO> orderStatusLog = order.getOrderStatusLog().stream()
            .map(orderStatusTOOrderStatusDTOConverter::convert)
            .collect(Collectors.toList());

        return OrderDTO.builder()
            .id(order.getId())
            .products(collect)
            .status(orderStatusLog)
            .createdTime(Date.from(order.getCreated()))
            .shopId(order.getShop().getId())
            .name(order.getName())
            .email(order.getEmail())
            .phone(order.getPhone())
            .price(order.getPrice())
            .address(order.getAddress())
            .comments(order.getComments())
            .build();
    }
}

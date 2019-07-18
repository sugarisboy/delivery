package dev.muskrat.delivery.order.converter;

import dev.muskrat.delivery.components.converter.ObjectConverter;
import dev.muskrat.delivery.order.dao.OrderProduct;
import dev.muskrat.delivery.order.dto.OrderProductDTO;
import org.springframework.stereotype.Component;

@Component
public class OrderProductTOOrderProductDTOConverter implements ObjectConverter<OrderProduct, OrderProductDTO> {

    @Override
    public OrderProductDTO convert(OrderProduct orderProduct) {
        return OrderProductDTO.builder()
                .count(orderProduct.getCount())
                .productId(orderProduct.getProductId())
                .build();
    }
}

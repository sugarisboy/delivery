package dev.muskrat.delivery.converter;

import dev.muskrat.delivery.dao.order.OrderProduct;
import dev.muskrat.delivery.dto.order.OrderProductDTO;
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

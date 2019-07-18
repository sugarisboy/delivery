package dev.muskrat.delivery.order.converter;

import dev.muskrat.delivery.components.converter.ObjectConverter;
import dev.muskrat.delivery.order.dao.OrderProduct;
import dev.muskrat.delivery.order.dto.OrderProductDTO;
import org.springframework.stereotype.Component;

@Component
public class OrderProductDTOTOOrderProductConverter implements ObjectConverter<OrderProductDTO, OrderProduct> {

    @Override
    public OrderProduct convert(OrderProductDTO dto) {
        OrderProduct product = new OrderProduct();
        product.setProductId(dto.getProductId());
        product.setCount(dto.getCount());
        return product;
    }
}

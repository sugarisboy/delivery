package dev.muskrat.delivery.converter;

import dev.muskrat.delivery.dao.order.OrderProduct;
import dev.muskrat.delivery.dto.order.OrderProductDTO;
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

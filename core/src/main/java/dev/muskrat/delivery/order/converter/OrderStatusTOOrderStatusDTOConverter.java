package dev.muskrat.delivery.order.converter;

import dev.muskrat.delivery.components.converter.ObjectConverter;
import dev.muskrat.delivery.order.dao.OrderStatusEntry;
import dev.muskrat.delivery.order.dto.OrderStatusEntryDTO;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class OrderStatusTOOrderStatusDTOConverter implements ObjectConverter<OrderStatusEntry, OrderStatusEntryDTO> {

    @Override
    public OrderStatusEntryDTO convert(OrderStatusEntry orderStatusEntry) {
        return OrderStatusEntryDTO.builder()
            .date(Date.from(orderStatusEntry.getUpdatedTime()))
            .status(orderStatusEntry.getStatus())
            .build();
    }
}

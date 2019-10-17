package dev.muskrat.delivery.components.events.order;

import dev.muskrat.delivery.order.dao.Order;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

public class OrderCreateEvent extends ApplicationEvent {

    @Getter
    private final Order order;

    public OrderCreateEvent(Object source, Order order) {
        super(source);
        this.order = order;
    }
}

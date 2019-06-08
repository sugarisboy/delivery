package dev.muskrat.delivery.service.order;

import dev.muskrat.delivery.dao.order.Order;
import dev.muskrat.delivery.dto.order.OrderCreateDTO;
import dev.muskrat.delivery.dto.order.OrderDTO;
import org.springframework.stereotype.Service;

@Service
public interface OrderService {

    OrderCreateDTO create(OrderDTO orderDTO);

    Order findByPhone(String phone);
}

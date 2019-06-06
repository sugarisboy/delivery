package dev.muskrat.delivery.service.order;

import dev.muskrat.delivery.dto.order.OrderCreateDTO;
import dev.muskrat.delivery.dto.order.OrderDTO;
import dev.muskrat.delivery.dto.order.OrderUpdateDTO;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface OrderService {

    OrderDTO create(OrderCreateDTO orderDTO);

    OrderDTO updateStatus(OrderUpdateDTO orderDTO);

    Optional<OrderDTO> findById(Long id);
}

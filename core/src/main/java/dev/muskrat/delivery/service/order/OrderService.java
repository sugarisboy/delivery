package dev.muskrat.delivery.service.order;

import dev.muskrat.delivery.dto.order.OrderCreateDTO;
import dev.muskrat.delivery.dto.order.OrderDTO;
import dev.muskrat.delivery.dto.order.OrderUpdateDTO;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface OrderService {

    OrderDTO create(OrderCreateDTO orderDTO);

    /**
     * @param orderDTO contains data: id and status
     * @return OrderDTO with id and new status
     *
     * 0 - Processing
     * 1 - Assembly
     * 2 - Delivery
     *
     * 10 - Done
     * 11 - Cancel
     */
    OrderDTO updateStatus(OrderUpdateDTO orderDTO);

    Optional<OrderDTO> findById(Long id);
}

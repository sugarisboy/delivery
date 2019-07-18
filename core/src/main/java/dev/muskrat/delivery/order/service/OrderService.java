package dev.muskrat.delivery.order.service;

import dev.muskrat.delivery.order.dto.OrderCreateDTO;
import dev.muskrat.delivery.order.dto.OrderDTO;
import dev.muskrat.delivery.order.dto.OrderUpdateDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface OrderService {

    OrderDTO create(OrderCreateDTO orderDTO);

    /**
     * @param orderDTO contains data: id and status
     * @return OrderDTO with id and new status
     * <p>
     * 0 - Processing
     * 1 - Assembly
     * 2 - Delivery
     * <p>
     * 10 - Done
     * 11 - Cancel
     */
    OrderDTO updateStatus(OrderUpdateDTO orderDTO);

    Optional<OrderDTO> findById(Long id);

    Optional<List<OrderDTO>> findByEmail(String email);

    Optional<List<OrderDTO>> findOrdersByShop(Long shopId);
}

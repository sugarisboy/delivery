package dev.muskrat.delivery.order.service;

import dev.muskrat.delivery.order.dto.*;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface OrderService {

    OrderDTO create(OrderCreateDTO orderDTO);

    /**
     * @param orderDTO contains data: id and orderStatus
     * @return OrderDTO with id and new orderStatus
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

    OrderPageDTO findAll(OrderPageRequestDTO orderPageRequestDTO, Pageable pageable);

    boolean isOwnerByOrder(Authentication authentication, Long orderId);

    boolean isOwnerByShop(Authentication authentication, Long shopId);

    boolean isClientByOrder(Authentication authentication, Long orderId);

    boolean isClientByUser(Authentication authentication, Long userId);
}

package dev.muskrat.delivery.service.order;

import dev.muskrat.delivery.converter.OrderCreateDTOTOOrderConverter;
import dev.muskrat.delivery.converter.OrderTOOrderDTOConverter;
import dev.muskrat.delivery.dao.order.Order;
import dev.muskrat.delivery.dao.order.OrderRepository;
import dev.muskrat.delivery.dto.order.OrderCreateDTO;
import dev.muskrat.delivery.dto.order.OrderDTO;
import dev.muskrat.delivery.dto.order.OrderUpdateDTO;
import dev.muskrat.delivery.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderCreateDTOTOOrderConverter orderCreateDTOTOOrderConverter;
    private final OrderTOOrderDTOConverter orderTOOrderDTOConverter;

    @Override
    public OrderDTO create(OrderCreateDTO orderDTO) {
        Order order = orderCreateDTOTOOrderConverter.convert(orderDTO);

        orderRepository.save(order);

        //TODO trigger event

        return OrderDTO.builder()
            .id(order.getId())
            .status(order.getStatus())
            .build();
    }

    @Override
    public OrderDTO updateStatus(OrderUpdateDTO orderDTO) {
        Long id = orderDTO.getId();
        Optional<Order> byId = orderRepository.findById(id);
        if (byId.isEmpty())
            throw new EntityNotFoundException("Order with id " + id + " not found");

        Order order = byId.get();
        order.setStatus(orderDTO.getStatus());
        orderRepository.save(order);

        //TODO: trigger event

        return OrderDTO.builder()
                .id(order.getId())
                .status(order.getStatus())
                .build();
    }

    @Override
    public Optional<OrderDTO> findById(Long id) {
        Optional<Order> byId = orderRepository.findById(id);
        return byId.map(orderTOOrderDTOConverter::convert);
    }

}

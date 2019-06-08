package dev.muskrat.delivery.controller;

import dev.muskrat.delivery.dto.order.OrderCreateDTO;
import dev.muskrat.delivery.dto.order.OrderDTO;
import dev.muskrat.delivery.dto.order.OrderUpdateDTO;
import dev.muskrat.delivery.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/create")
    public OrderDTO orderCreate(
        @Valid @RequestBody OrderCreateDTO orderDTO
    ) {
        return orderService.create(orderDTO);
    }

    @PatchMapping("/update/status")
    public OrderDTO orderStatusUpdate(
        @Valid @RequestBody OrderUpdateDTO orderDTO
    ) {
        return orderService.updateStatus(orderDTO);
    }
}

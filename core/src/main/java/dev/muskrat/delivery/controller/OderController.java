package dev.muskrat.delivery.controller;

import dev.muskrat.delivery.dto.order.OrderCreateDTO;
import dev.muskrat.delivery.dto.order.OrderDTO;
import dev.muskrat.delivery.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/order")
@RequiredArgsConstructor
public class OderController {

    private final OrderService orderService;

    @PostMapping("/order/")
    public OrderCreateDTO orderCreate(
            @RequestBody OrderDTO orderDTO
    ) {
        return orderService.create(orderDTO);
    }
}

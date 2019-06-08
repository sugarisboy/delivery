package dev.muskrat.delivery.controller;

import dev.muskrat.delivery.dto.order.OrderCreateDTO;
import dev.muskrat.delivery.dto.order.OrderDTO;
import dev.muskrat.delivery.dto.order.OrderUpdateDTO;
import dev.muskrat.delivery.exception.EntityNotFoundException;
import dev.muskrat.delivery.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

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

    @GetMapping("/{id}")
    public OrderDTO findById(
        @NotNull @PathVariable Long id
    ) {
        return orderService.findById(id).orElseThrow(() ->
            new EntityNotFoundException("Order not found")
        );
    }

    @GetMapping(value = "/email/{email:.+}", produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public OrderDTO findByEmail(
        @Email @PathVariable String email
    ) {
        return orderService.findByEmail(email).orElseThrow(() ->
            new EntityNotFoundException("Order not found")
        );
    }

    @GetMapping("/shop/{shopId}")
    public List<OrderDTO> findOrdersByShop(
        @NotNull @PathVariable Long shopId
    ) {
        return orderService.findOrdersByShop(shopId)
            .orElse(new ArrayList<>());
    }
}

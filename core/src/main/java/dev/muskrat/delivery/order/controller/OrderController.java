package dev.muskrat.delivery.order.controller;

import dev.muskrat.delivery.order.dto.*;
import dev.muskrat.delivery.components.exception.EntityNotFoundException;
import dev.muskrat.delivery.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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

    @GetMapping("/get/{id}")
    public OrderDTO findById(
        @NotNull @PathVariable Long id
    ) {
        return orderService.findById(id).orElseThrow(() ->
            new EntityNotFoundException("Order not found")
        );
    }

    @GetMapping("/email/{email:.+}")
    public List<OrderDTO> findByEmail(
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

    @GetMapping("/page")
    public OrderPageDTO page(
        @Valid @RequestBody OrderPageRequestDTO orderPageRequestDTO,
        @PageableDefault(size = 3, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return orderService.findAll(orderPageRequestDTO, pageable);
    }
}

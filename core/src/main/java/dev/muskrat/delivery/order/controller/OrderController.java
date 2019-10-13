package dev.muskrat.delivery.order.controller;

import dev.muskrat.delivery.components.exception.EntityNotFoundException;
import dev.muskrat.delivery.order.dto.*;
import dev.muskrat.delivery.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

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

    @PatchMapping("/update")
    @PreAuthorize("hasAuthority('ADMIN') or" +
        "(hasAuthority('PARTNER') and @orderServiceImpl.isOwnerByOrder(authentication, #orderDTO.id))")
    public OrderDTO orderStatusUpdate(
        @Valid @RequestBody OrderUpdateDTO orderDTO
    ) {
        return orderService.updateStatus(orderDTO);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or" +
        "(hasAuthority('PARTNER') and @orderServiceImpl.isOwnerByOrder(authentication, #id)) or" +
        "(hasAuthority('USER') and @orderServiceImpl.isClientByOrder(authentication, #id))")
    public OrderDTO findById(
        @NotNull @PathVariable Long id
    ) {
        return orderService.findById(id).orElseThrow(() ->
            new EntityNotFoundException("Order not found")
        );
    }

    // For partner need indicate your shopId in OrderPageRequestDTO
    // For user need indicate your userId in OrderPageRequestDTO
    @PostMapping("/page")
    @PreAuthorize("hasAuthority('ADMIN') or" +
        "(hasAuthority('PARTNER') and @orderServiceImpl.isOwnerByShop(authentication, #orderPageRequestDTO.shopId)) or" +
        "(hasAuthority('USER') and @orderServiceImpl.isClientByUser(authentication, #orderPageRequestDTO.userId))")
    public OrderPageDTO page(
        @Valid @RequestBody(required = false) OrderPageRequestDTO orderPageRequestDTO,
        @PageableDefault(size = 3, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return orderService.findAll(orderPageRequestDTO, pageable);
    }
}

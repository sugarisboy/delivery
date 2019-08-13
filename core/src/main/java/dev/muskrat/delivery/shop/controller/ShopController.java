package dev.muskrat.delivery.shop.controller;

import dev.muskrat.delivery.auth.converter.JwtAuthorizationToUserConverter;
import dev.muskrat.delivery.auth.dao.AuthorizedUser;
import dev.muskrat.delivery.components.exception.EntityNotFoundException;
import dev.muskrat.delivery.shop.dto.*;
import dev.muskrat.delivery.shop.service.ShopService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Before;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/shop")
@RequiredArgsConstructor
public class ShopController {

    private final JwtAuthorizationToUserConverter jwtAuthorizationToUserConverter;
    private final ShopService shopService;


    @PostMapping("/create")
    public ShopCreateResponseDTO create(
        @Valid @RequestBody ShopCreateDTO shopCreateDTO
    ) {
        return shopService.create(shopCreateDTO);
    }

    @PatchMapping("/update")
    @PreAuthorize("@shopServiceImpl.isOwner(authentication, #shopUpdateDTO.id)")
    public ShopUpdateResponseDTO update(
        @Valid @RequestBody ShopUpdateDTO shopUpdateDTO
        //@RequestHeader(value = "Authorization") String authorization
    ) {
        //AuthorizedUser convert = jwtAuthorizationToUserConverter.convert(authorization);
        return shopService.update(shopUpdateDTO);
    }

    @PatchMapping("/schedule/update")
    public ShopScheduleResponseDTO updateSchedule(
        @Valid @RequestBody ShopScheduleUpdateDTO shopScheduleUpdateDTO
    ) {
        return shopService.updateSchedule(shopScheduleUpdateDTO);
    }

    @GetMapping("/{id}")
    public ShopDTO findById(@NotNull @PathVariable Long id) {
        return shopService.findById(id).orElseThrow(() ->
            new EntityNotFoundException("Shop not found")
        );
    }

    @GetMapping("/page")
    public ShopPageDTO page(
        @Valid @RequestBody(required = false) ShopPageRequestDTO shopPageRequestDTO,
        @PageableDefault(size = 3, sort = {"id"}, direction = Sort.Direction.DESC) Pageable page
    ) {
        return shopService.findAll(shopPageRequestDTO, page);
    }

    @DeleteMapping("/{id}")
    public void delete(@NotNull @PathVariable Long id) {
        shopService.delete(id);
    }

}

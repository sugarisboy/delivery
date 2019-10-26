package dev.muskrat.delivery.shop.controller;

import dev.muskrat.delivery.components.exception.EntityNotFoundException;
import dev.muskrat.delivery.partner.dao.Partner;
import dev.muskrat.delivery.shop.dto.*;
import dev.muskrat.delivery.shop.service.ShopService;
import dev.muskrat.delivery.user.converter.JwtAuthorizationToUserConverter;
import dev.muskrat.delivery.user.dao.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/shop")
@RequiredArgsConstructor
public class ShopController {

    private final ShopService shopService;
    private final JwtAuthorizationToUserConverter jwtAuthorizationToUserConverter;

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('PARTNER')")
    public ShopCreateResponseDTO create(
        @Valid @RequestBody ShopCreateDTO shopCreateDTO,
        @RequestHeader("Key") String key,
        @RequestHeader("Authorization") String authorization
    ) {
        User user = jwtAuthorizationToUserConverter.convert(key, authorization);
        Partner partner = user.getPartner();
        return shopService.create(shopCreateDTO, partner);
    }

    @PostMapping("/admin/create")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ShopCreateResponseDTO createWithPartner(
        @Valid @RequestBody ShopCreateDTO shopCreateDTO
    ) {
        return shopService.createWithPartner(shopCreateDTO);
    }

    @PatchMapping("/update")
    @PreAuthorize("hasAuthority('ADMIN') or (hasAuthority('PARTNER') and @shopServiceImpl.isShopOwner(authentication, #shopUpdateDTO.id))")
    public ShopUpdateResponseDTO update(
        @Valid @RequestBody ShopUpdateDTO shopUpdateDTO
    ) {
        return shopService.update(shopUpdateDTO);
    }

    @PatchMapping("/schedule/update")
    @PreAuthorize("hasAuthority('ADMIN') or (hasAuthority('PARTNER') and @shopServiceImpl.isShopOwner(authentication, #shopScheduleUpdateDTO.id))")
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

    @PostMapping("/stats")
    @PreAuthorize("hasAuthority('ADMIN') or (hasAuthority('PARTNER') and @shopServiceImpl.isShopOwner(authentication, #shopStatsDTO.id))")
    public ShopStatsResponseDTO stats(
        @Valid @RequestBody ShopStatsDTO shopStatsDTO
    ) {
        return shopService.stats(shopStatsDTO);
    }

    @PostMapping("/page")
    public ShopPageDTO page(
        @Valid @RequestBody(required = false) ShopPageRequestDTO shopPageRequestDTO,
        @PageableDefault(size = 20, sort = {"id"}, direction = Sort.Direction.DESC) Pageable page
    ) {
        return shopService.findAll(shopPageRequestDTO, page);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or (hasAuthority('PARTNER') and @shopServiceImpl.isShopOwner(authentication, #id))")
    public void delete(@NotNull @PathVariable Long id) {
        shopService.delete(id);
    }

}

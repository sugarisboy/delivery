package dev.muskrat.delivery.map.controller;

import dev.muskrat.delivery.map.dto.AutoCompleteResponseDTO;
import dev.muskrat.delivery.map.dto.RegionDTO;
import dev.muskrat.delivery.map.dto.RegionUpdateDTO;
import dev.muskrat.delivery.map.dto.RegionUpdateResponseDTO;
import dev.muskrat.delivery.map.service.MappingService;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequiredArgsConstructor
@RequestMapping("/map")
public class MapController {

    private final MappingService mappingService;

    @GetMapping("/ac/{label:.+}")
    public AutoCompleteResponseDTO autoComplete(
        @NotNull @PathVariable @Length(min = 3, max = 100) String label
    ) {
        return mappingService.autoComplete(label);
    }

    @PatchMapping("/regionupdate")
    //@PreAuthorize("hasAuthority('ADMIN') or (hasAuthority('PARTNER') and @shopServiceImpl.isShopOwner(authentication, #regionUpdateDTO.shopId))")
    public RegionUpdateResponseDTO updateRegion(
        @Valid @RequestBody RegionUpdateDTO regionUpdateDTO
    ) {
       return mappingService.updateRegion(regionUpdateDTO);
    }

    @GetMapping("/region/shop/{shopId}")
    @PreAuthorize("hasAuthority('ADMIN') or (hasAuthority('PARTNER') and @shopServiceImpl.isShopOwner(authentication, shopId))")
    public RegionDTO findRegion(
        @PathVariable @NotNull Long shopId
    ) {
        return mappingService.findShopRegion(shopId);
    }
}

package dev.muskrat.delivery.controller;

import dev.muskrat.delivery.dto.mapping.RegionUpdateDTO;
import dev.muskrat.delivery.dto.mapping.RegionUpdateResponseDTO;
import dev.muskrat.delivery.service.mapping.MappingService;
import dev.muskrat.delivery.dto.mapping.AutoCompleteResponseDTO;
import lombok.RequiredArgsConstructor;
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
        @NotNull @PathVariable String label
    ) {
        return mappingService.autoComplete(label);
    }

    @PatchMapping("/regionupdate")
    public RegionUpdateResponseDTO updateRegion(
        @Valid @RequestBody RegionUpdateDTO regionUpdateDTO
    ) {
       return mappingService.updateRegion(regionUpdateDTO);
    }
}

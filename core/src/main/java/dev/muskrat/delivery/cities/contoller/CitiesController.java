package dev.muskrat.delivery.cities.contoller;

import dev.muskrat.delivery.cities.dto.*;
import dev.muskrat.delivery.cities.service.CitiesService;
import dev.muskrat.delivery.components.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cities")
public class CitiesController {

    private final CitiesService citiesService;

    @PostMapping("/create")
    public CityCreateResponseDTO create(
        @Valid @RequestBody CityCreateDTO cityCreateDTO
    ) {
        return citiesService.create(cityCreateDTO);
    }

    @PatchMapping("/update")
    public CityUpdateResponseDTO update(
        @Valid @RequestBody CityUpdateDTO cityUpdateDTO
    ) {
        return citiesService.update(cityUpdateDTO);
    }

    @DeleteMapping("/{id}")
    public void delete(@NotNull @PathVariable Long id) {
        citiesService.delete(id);
    }

    @GetMapping("/")
    public List<CityDTO> findAll() {
        return citiesService.findAll();
    }

    @GetMapping("/{id}")
    public CityDTO findById(
        @NotNull @PathVariable Long id
    ) {
        return citiesService.findById(id).orElseThrow(() ->
            new EntityNotFoundException("City not found")
        );
    }
}

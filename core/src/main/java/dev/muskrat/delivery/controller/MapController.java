package dev.muskrat.delivery.controller;

import dev.muskrat.delivery.geocoder.AutoComplete;
import dev.muskrat.delivery.dto.mapping.AutoCompleteDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

@RestController
@RequiredArgsConstructor
@RequestMapping("/map")
public class MapController {

    private final AutoComplete autoComplete;

    @GetMapping("/ac/{label:.+}")
    public AutoCompleteDTO autoComplete(
        @NotNull @PathVariable String label
    ) {
        System.out.println(label);
        return autoComplete.complete(label);
    }

}

package dev.muskrat.delivery.service.mapping;

import dev.muskrat.delivery.dto.mapping.AutoCompleteResponseDTO;
import dev.muskrat.delivery.dto.mapping.RegionUpdateDTO;
import dev.muskrat.delivery.dto.mapping.RegionUpdateResponseDTO;

public interface MappingService {

    AutoCompleteResponseDTO autoComplete(String label);

    RegionUpdateResponseDTO updateRegion(RegionUpdateDTO regionUpdateDTO);
}

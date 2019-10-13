package dev.muskrat.delivery.map.service;

import dev.muskrat.delivery.map.dao.RegionPoint;
import dev.muskrat.delivery.map.dto.AutoCompleteResponseDTO;
import dev.muskrat.delivery.map.dto.RegionUpdateDTO;
import dev.muskrat.delivery.map.dto.RegionUpdateResponseDTO;

public interface MappingService {

    AutoCompleteResponseDTO autoComplete(String label);

    RegionPoint getPointByAddress(String label);

    RegionUpdateResponseDTO updateRegion(RegionUpdateDTO regionUpdateDTO);

    boolean isValidAddress(String city, String label);
}

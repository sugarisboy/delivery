package dev.muskrat.delivery.map.service;

import dev.muskrat.delivery.map.dao.RegionPoint;
import dev.muskrat.delivery.map.dto.AutoCompleteResponseDTO;

public interface MapApi {

    boolean isValidAddress(String city, String label);

    AutoCompleteResponseDTO autoComplete(String label);

    RegionPoint getPointByAddress(String label);
}

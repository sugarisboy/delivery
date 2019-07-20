package dev.muskrat.delivery.cities.service;

import dev.muskrat.delivery.cities.dto.*;

import java.util.List;
import java.util.Optional;

public interface CitiesService {

    CityCreateResponseDTO create(CityCreateDTO cityCreateDTO);

    CityUpdateResponseDTO update(CityUpdateDTO cityUpdateDTO);

    List<CityDTO> findAll();

    Optional<CityDTO> findById(Long id);

    void delete(Long id);
}

package dev.muskrat.delivery.cities.service;

import dev.muskrat.delivery.cities.dao.CitiesRepository;
import dev.muskrat.delivery.cities.dao.City;
import dev.muskrat.delivery.cities.dto.*;
import dev.muskrat.delivery.components.exception.EntityExistException;
import dev.muskrat.delivery.components.exception.EntityNotFoundException;
import dev.muskrat.delivery.product.dao.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CitiesServiceImpl implements CitiesService {

    private final CitiesRepository citiesRepository;

    @Override
    public CityCreateResponseDTO create(CityCreateDTO cityCreateDTO) {
        String name = cityCreateDTO.getName();

        Optional<City> byName = citiesRepository.findByName(name);
        if (byName.isPresent())
            throw new EntityExistException("City with name " + name + " already created");

        City city = new City();
        city.setName(name);
        City cityWithId = citiesRepository.save(city);

        Long id = cityWithId.getId();

        return CityCreateResponseDTO.builder()
            .id(id)
            .build();
    }

    @Override
    public CityUpdateResponseDTO update(CityUpdateDTO cityUpdateDTO) {
        Long id = cityUpdateDTO.getId();
        String name = cityUpdateDTO.getName();

        Optional<City> byId = citiesRepository.findById(id);
        if (byId.isEmpty())
            throw new EntityNotFoundException("City with id " + id + " don't found");

        Optional<City> byName = citiesRepository.findByName(name);
        if (byName.isPresent())
            throw new EntityExistException("City with name " + name + " already exists");

        City city = byId.get();

        city.setName(name);
        citiesRepository.save(city);

        return CityUpdateResponseDTO.builder()
            .id(id)
            .build();
    }

    @Override
    public List<CityDTO> findAll() {
        return null;
    }

    @Override
    public Optional<CityDTO> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public void delete(Long id) {
        Optional<City> byId = citiesRepository.findById(id);

        byId.ifPresentOrElse(p -> {
            p.setDeleted(true);
            citiesRepository.save(p);
        }, () -> {
            throw new EntityNotFoundException("City with id " + id + " not found");
        });
    }
}

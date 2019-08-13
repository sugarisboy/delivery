package dev.muskrat.delivery.partner.service;

import dev.muskrat.delivery.auth.dao.AuthorizedUser;
import dev.muskrat.delivery.components.exception.EntityNotFoundException;
import dev.muskrat.delivery.partner.converter.PartnerToPartnerDTOConverter;
import dev.muskrat.delivery.partner.dao.Partner;
import dev.muskrat.delivery.partner.dao.PartnerRepository;
import dev.muskrat.delivery.partner.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PartnerServiceImpl implements PartnerService {

    private final PartnerRepository partnerRepository;
    private final PartnerToPartnerDTOConverter partnerToPartnerDTOConverter;

    @Override
    public PartnerRegisterResponseDTO create(AuthorizedUser executor, PartnerRegisterDTO partnerRegisterDTO) {
        Partner partner = new Partner();
        executor.setPartner(partner);
        Partner saved = partnerRepository.save(partner);

        return PartnerRegisterResponseDTO.builder()
            .id(executor.getId())
            .build();
    }

    /*@Override
    public PartnerRegisterResponseDTO createByOrder(Order order) {
        PartnerRegisterDTO partnerRegisterDTO = PartnerRegisterDTO.builder()
            .email(order.getEmail())
            .name(order.getName())
            .phone(order.getPhone())
            // TODO: Fix it. How here generate password?
            .password("password")
            .passwordRepeat("password")
            .build();
        return create(partnerRegisterDTO);
    }*/

    @Override
    public PartnerUpdateResponseDTO update(PartnerUpdateDTO partnerDTO) {
        Long id = partnerDTO.getId();
        Optional<Partner> byId = partnerRepository.findById(id);
        if (byId.isEmpty())
            throw new EntityNotFoundException("Partner with id " + id + " don't found");

        Partner partner = byId.get();
        // TODO: function for change password

        partnerRepository.save(partner);

        return PartnerUpdateResponseDTO.builder()
            .id(id)
            .build();
    }

    @Override
    public List<PartnerDTO> findAll() {
        return partnerRepository.findAll()
            .stream()
            .map(partnerToPartnerDTOConverter::convert)
            .collect(Collectors.toList());
    }
}
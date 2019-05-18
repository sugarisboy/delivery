package dev.muskrat.delivery.service;

import dev.muskrat.delivery.EntityExistException;
import dev.muskrat.delivery.converter.ObjectConverter;
import dev.muskrat.delivery.dao.Partner;
import dev.muskrat.delivery.dao.PartnerRepository;
import dev.muskrat.delivery.dto.PartnerDTO;
import dev.muskrat.delivery.dto.PartnerRegisterDTO;
import dev.muskrat.delivery.dto.PartnerRegisterResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PartnerServiceImpl implements PartnerService {

    private final PartnerRepository partnerRepository;
    private final ObjectConverter<Partner, PartnerDTO> partnerToPartnerDTOConverter;

    @Override
    public PartnerRegisterResponseDTO create(PartnerRegisterDTO partnerRegisterDTO) {
        Optional<Partner> sameEmailPartner = partnerRepository
            .findByEmail(partnerRegisterDTO.getEmail());

        if (sameEmailPartner.isPresent()) {
            throw new EntityExistException("This email is already taken.");
        }

        if (!partnerRegisterDTO.getPassword()
            .equals(partnerRegisterDTO.getPasswordRepeat())) {
            throw new RuntimeException("Passwords aren't equal");
        }

        Partner partner = new Partner();
        partner.setEmail(partnerRegisterDTO.getEmail());
        partner.setName(partnerRegisterDTO.getName());
        partner.setPassword(partnerRegisterDTO.getPassword());
        partner.setPhone(partnerRegisterDTO.getPhone());
        Partner saved = partnerRepository.save(partner);

        PartnerRegisterResponseDTO partnerRegisterResponseDTO = new PartnerRegisterResponseDTO();
        partnerRegisterResponseDTO.setId(saved.getId());
        return partnerRegisterResponseDTO;
    }

    @Override
    public void update(PartnerDTO partnerDTO) {

    }

    @Override
    public void delete(PartnerDTO partnerDTO) {
        Long id = partnerDTO.getId();
        if (id != null) {
            Optional<Partner> partner = partnerRepository.findById(id);
            partner.ifPresent(p -> {
                p.setBanned(true);
                partnerRepository.save(p);
            });
        }
    }

    @Override
    public Optional<PartnerDTO> findById(long id) {
        Optional<Partner> partner = partnerRepository.findById(id);
        return partner.map(partnerToPartnerDTOConverter::convert);
    }

    @Override
    public List<PartnerDTO> findAll() {
        return partnerRepository.findAll()
            .stream()
            .map(partnerToPartnerDTOConverter::convert)
            .collect(Collectors.toList());
    }

}

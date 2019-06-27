package dev.muskrat.delivery.service.partner;

import dev.muskrat.delivery.converter.PartnerToPartnerDTOConverter;
import dev.muskrat.delivery.dao.order.Order;
import dev.muskrat.delivery.dao.partner.Partner;
import dev.muskrat.delivery.dao.partner.PartnerRepository;
import dev.muskrat.delivery.dto.partner.*;
import dev.muskrat.delivery.exception.EntityExistException;
import dev.muskrat.delivery.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PartnerServiceImpl implements PartnerService {

    private final PartnerRepository partnerRepository;
    private final PartnerToPartnerDTOConverter partnerToPartnerDTOConverter;
    private final PasswordEncoder passwordEncoder;

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

        String encodedPassword = passwordEncoder.encode(partnerRegisterDTO.getPassword());
        partner.setPassword(encodedPassword);
        partner.setPhone(partnerRegisterDTO.getPhone());
        Partner saved = partnerRepository.save(partner);

        PartnerRegisterResponseDTO partnerRegisterResponseDTO = new PartnerRegisterResponseDTO();
        partnerRegisterResponseDTO.setId(saved.getId());
        return partnerRegisterResponseDTO;
    }

    @Override
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
    }

    @Override
    public PartnerUpdateResponseDTO update(PartnerUpdateDTO partnerDTO) {
        Long id = partnerDTO.getId();
        Optional<Partner> byId = partnerRepository.findById(id);
        if (byId.isEmpty())
            throw new EntityNotFoundException("Partner with id " + id + " don't found");

        Partner partner = byId.get();
        if (partnerDTO.getEmail() != null)
            partner.setEmail(partnerDTO.getEmail());
        if (partnerDTO.getName() != null)
            partner.setName(partnerDTO.getName());
        if (partnerDTO.getId() != null)
            partner.setPhone(partnerDTO.getPhone());

        // TODO: function for change password

        partnerRepository.save(partner);

        return PartnerUpdateResponseDTO.builder()
            .id(id)
            .build();
    }

    @Override
    public void delete(Long id) {
        Optional<Partner> partner = partnerRepository.findById(id);
        partner.ifPresent(p -> {
            p.setBanned(true);
            partnerRepository.save(p);
        });
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

    @Override
    public Optional<PartnerDTO> findByEmail(String email) {
        return partnerRepository.findByEmail(email)
            .map(partnerToPartnerDTOConverter::convert);
    }
}

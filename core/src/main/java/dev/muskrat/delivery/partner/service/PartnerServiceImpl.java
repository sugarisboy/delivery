package dev.muskrat.delivery.partner.service;

import dev.muskrat.delivery.auth.dao.AuthorizedUser;
import dev.muskrat.delivery.auth.dao.Role;
import dev.muskrat.delivery.auth.repository.AuthorizedUserRepository;
import dev.muskrat.delivery.auth.repository.RoleRepository;
import dev.muskrat.delivery.auth.security.jwt.JwtUser;
import dev.muskrat.delivery.components.exception.EntityNotFoundException;
import dev.muskrat.delivery.partner.dao.Partner;
import dev.muskrat.delivery.partner.dao.PartnerRepository;
import dev.muskrat.delivery.partner.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PartnerServiceImpl implements PartnerService {

    private final RoleRepository roleRepository;
    private final PartnerRepository partnerRepository;
    private final AuthorizedUserRepository authorizedUserRepository;

    @Override
    public PartnerRegisterResponseDTO create(AuthorizedUser executor) {
        if (executor.getPartner() != null)
            throw new RuntimeException("User already partner");

        Partner partner = new Partner();
        partner.setUser(executor);
        partnerRepository.save(partner);

        Role rolePartner = roleRepository.findByName(Role.Name.PARTNER.getName()).get();
        ArrayList<Role> roles = new ArrayList<>(executor.getRoles());
        roles.add(rolePartner);
        executor.setRoles(roles);
        executor.setPartner(partner);

        authorizedUserRepository.save(executor);

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
    public boolean isCurrentPartner(Authentication authentication, Long id) {
        Optional<AuthorizedUser> byId = authorizedUserRepository.findById(id);
        if (byId.isEmpty())
            throw new EntityNotFoundException("AuthorizedUser with id " + id + " not found");
        AuthorizedUser authorizedUser = byId.get();

        String authorizedUserEmail = authorizedUser.getEmail();
        JwtUser jwtUser = (JwtUser) authentication.getPrincipal();
        String jwtUserEmail = jwtUser.getEmail();

        return authorizedUserEmail.equalsIgnoreCase(jwtUserEmail);
    }
}
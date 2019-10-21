package dev.muskrat.delivery.partner.service;

import dev.muskrat.delivery.auth.dao.Role;
import dev.muskrat.delivery.auth.repository.RoleRepository;
import dev.muskrat.delivery.auth.security.jwt.JwtUser;
import dev.muskrat.delivery.components.exception.EntityNotFoundException;
import dev.muskrat.delivery.partner.dao.Partner;
import dev.muskrat.delivery.partner.dao.PartnerRepository;
import dev.muskrat.delivery.partner.dto.PartnerRegisterResponseDTO;
import dev.muskrat.delivery.user.dao.User;
import dev.muskrat.delivery.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PartnerServiceImpl implements PartnerService {

    private final RoleRepository roleRepository;
    private final PartnerRepository partnerRepository;
    private final UserRepository userRepository;

    @Override
    public PartnerRegisterResponseDTO create(User user) {
        if (user.getPartner() != null)
            throw new RuntimeException("User already partner");

        Partner partner = new Partner();
        partner.setUser(user);
        partnerRepository.save(partner);

        Role rolePartner = roleRepository.findByName(Role.Name.PARTNER.getName()).get();
        ArrayList<Role> roles = new ArrayList<>(user.getRoles());
        roles.add(rolePartner);
        user.setRoles(roles);
        user.setPartner(partner);
        userRepository.save(user);

        return PartnerRegisterResponseDTO.builder()
            .id(user.getId())
            .build();
    }

    @Override
    public boolean isCurrentPartner(Authentication authentication, Long id) {
        Optional<User> byId = userRepository.findById(id);
        if (byId.isEmpty())
            throw new EntityNotFoundException("User with id " + id + " not found");
        User user = byId.get();

        String authorizedUserEmail = user.getEmail();
        JwtUser jwtUser = (JwtUser) authentication.getPrincipal();
        String jwtUserEmail = jwtUser.getEmail();

        return authorizedUserEmail.equalsIgnoreCase(jwtUserEmail);
    }
}
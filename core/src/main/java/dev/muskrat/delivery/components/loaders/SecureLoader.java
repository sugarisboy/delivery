package dev.muskrat.delivery.components.loaders;

import dev.muskrat.delivery.auth.dao.Role;
import dev.muskrat.delivery.auth.dto.UserLoginDTO;
import dev.muskrat.delivery.auth.dto.UserLoginResponseDTO;
import dev.muskrat.delivery.auth.repository.RoleRepository;
import dev.muskrat.delivery.auth.service.AuthorizationService;
import dev.muskrat.delivery.partner.service.PartnerService;
import dev.muskrat.delivery.user.dao.User;
import dev.muskrat.delivery.user.repository.UserRepository;
import dev.muskrat.delivery.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SecureLoader {

    private final UserService userService;
    private final PartnerService partnerService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AuthorizationService authorizationService;

    private UserLoginResponseDTO loginAdminDTO;

    public void load() {
        initRoles();
        initDefaultAdmin();
    }

    private void initRoles() {
        Arrays.stream(Role.Name.values())
            .map(Role.Name::getName)
            .forEach(
                roleName -> {
                    Role role = new Role();
                    role.setName(roleName);
                    roleRepository.save(role);
                }
            );
    }

    private void initDefaultAdmin() {
        Role adminRole = roleRepository.findByName("ADMIN").get();

        if (userRepository.countByRolesLike(adminRole) == 0) {
            List<Role> roles = roleRepository.findAll();

            User user = new User();
            user.setEmail("admin");
            user.setPassword("admin");
            user = userService.register(user);

            user.setRoles(roles);
            user = userRepository.save(user);

            partnerService.create(user);

            loginAdminDTO = authorizationService.login(UserLoginDTO.builder().username("admin").password("admin").build());
        }

        System.out.println(userRepository.countByRolesLike(adminRole));
    }

    public UserLoginResponseDTO getLoginAdminDTO() {
        return loginAdminDTO;
    }
}

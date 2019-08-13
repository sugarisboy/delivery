package dev.muskrat.delivery.auth.service;

import dev.muskrat.delivery.auth.dao.AuthorizedUser;
import dev.muskrat.delivery.auth.dao.Role;
import dev.muskrat.delivery.auth.dao.Status;
import dev.muskrat.delivery.auth.repository.AuthorizedUserRepository;
import dev.muskrat.delivery.auth.repository.RoleRepository;
import dev.muskrat.delivery.components.exception.EntityNotFoundException;
import dev.muskrat.delivery.partner.dao.Partner;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthorizedUserServiceImpl implements AuthorizedUserService {

    private final AuthorizedUserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public AuthorizedUser register(AuthorizedUser user) {

        String userRoleName = Role.Name.USER.getName();
        Optional<Role> byName = roleRepository.findByName(Role.Name.USER.getName());
        if (byName.isEmpty())
            throw new EntityNotFoundException("Role with name " + userRoleName + " not found");
        Role role = byName.get();

        ArrayList<Role> roles = new ArrayList<>();
        roles.add(role);

        user.setRoles(roles);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setStatus(Status.ACTIVE);
        user.setUsername(user.getEmail());

        return userRepository.save(user);
    }

    @Override
    public List<AuthorizedUser> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<AuthorizedUser> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public AuthorizedUser findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public void updateRefresh(AuthorizedUser user, String refresh) {
        user.setRefresh(refresh);
        userRepository.save(user);
    }

    @Override
    public void delete(Long id) {
        // todo: need safe delete
        userRepository.deleteById(id);
    }
}

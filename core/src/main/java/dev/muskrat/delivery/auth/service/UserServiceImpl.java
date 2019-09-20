package dev.muskrat.delivery.auth.service;

import dev.muskrat.delivery.auth.converter.JwtAuthorizationToUserConverter;
import dev.muskrat.delivery.auth.dao.User;
import dev.muskrat.delivery.auth.dao.Role;
import dev.muskrat.delivery.auth.dao.Status;
import dev.muskrat.delivery.auth.dto.UserDTO;
import dev.muskrat.delivery.auth.dto.UserUpdateDTO;
import dev.muskrat.delivery.auth.dto.UserUpdateResponseDTO;
import dev.muskrat.delivery.auth.repository.UserRepository;
import dev.muskrat.delivery.auth.repository.RoleRepository;
import dev.muskrat.delivery.components.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtAuthorizationToUserConverter jwtAuthorizationToUserConverter;

    @Override
    public User register(User user) {
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
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public UserDTO findById(Long id, String key, String authorization) {
        User executor = jwtAuthorizationToUserConverter.convert(key, authorization);

        Optional<User> byId = userRepository.findById(id);
        if (byId.isEmpty())
            throw new EntityNotFoundException("User with id " + id + " not found");
        User user = byId.get();

        if (executor.getId() == user.getId()) {
            return UserDTO.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .build();
        } else {
            return UserDTO.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
        }
    }

    @Override
    public UserUpdateResponseDTO update(UserUpdateDTO userUpdateDTO) {
        Long userId = userUpdateDTO.getId();
        Optional<User> byId = userRepository.findById(userId);
        if (byId.isEmpty())
            throw new EntityNotFoundException("User with id " + userId + " not found");
        User user = byId.get();

        if (userUpdateDTO.getEmail() != null)
            user.setEmail(userUpdateDTO.getEmail());

        if (userUpdateDTO.getFirstName() != null)
            user.setFirstName(userUpdateDTO.getFirstName());

        if (userUpdateDTO.getLastName() != null)
            user.setLastName(userUpdateDTO.getLastName());

        if (userUpdateDTO.getPhone() != null)
            user.setPhone(userUpdateDTO.getPhone());

        userRepository.save(user);

        return UserUpdateResponseDTO.builder()
            .id(user.getId())
            .build();

    }
}

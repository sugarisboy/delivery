package dev.muskrat.delivery.user.service;

import dev.muskrat.delivery.auth.dao.Role;
import dev.muskrat.delivery.auth.dao.Status;
import dev.muskrat.delivery.auth.repository.RoleRepository;
import dev.muskrat.delivery.components.exception.EntityNotFoundException;
import dev.muskrat.delivery.user.dao.User;
import dev.muskrat.delivery.user.dto.UserDTO;
import dev.muskrat.delivery.user.dto.UserUpdateDTO;
import dev.muskrat.delivery.user.dto.UserUpdateResponseDTO;
import dev.muskrat.delivery.user.repository.UserRepository;
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
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

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
    public UserDTO findById(Long id) {
        Optional<User> byId = userRepository.findById(id);
        if (byId.isEmpty())
            throw new EntityNotFoundException("User with id " + id + " not found");
        User user = byId.get();

            return UserDTO.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .build();
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

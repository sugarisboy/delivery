package dev.muskrat.delivery.user.service;

import dev.muskrat.delivery.auth.dao.Role;
import dev.muskrat.delivery.auth.dao.Status;
import dev.muskrat.delivery.auth.repository.RoleRepository;
import dev.muskrat.delivery.cities.dao.CitiesRepository;
import dev.muskrat.delivery.cities.dao.City;
import dev.muskrat.delivery.components.exception.EntityNotFoundException;
import dev.muskrat.delivery.user.converter.UserToUserDTOConverter;
import dev.muskrat.delivery.user.dao.User;
import dev.muskrat.delivery.user.dto.*;
import dev.muskrat.delivery.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final CitiesRepository citiesRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserToUserDTOConverter userToUserDTOConverter;

    @Override
    public User register(User user) {
        String userRoleName = Role.Name.USER.getName();
        Role.Name roleUser = Role.Name.USER;
        Role role = roleRepository
            .findByName(roleUser.getName())
            .orElseThrow(()-> new EntityNotFoundException("Role with name " + userRoleName + " not found"));

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
    public UserDTO findByEmail(String email) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new EntityNotFoundException("User with email " + email + " not found"));
        return userToUserDTOConverter.convert(user);
    }

    @Override
    public UserDTO findById(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("User with id " + id + " not found"));
        return userToUserDTOConverter.convert(user);
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

        if (userUpdateDTO.getName() != null)
            user.setName(userUpdateDTO.getName());

        if (userUpdateDTO.getPhone() != null)
            user.setPhone(userUpdateDTO.getPhone());

        if (userUpdateDTO.getCityId() != null) {
            Long cityId = userUpdateDTO.getCityId();
            City city = citiesRepository.findById(cityId)
                .orElseThrow(() -> new EntityNotFoundException("City with id " + cityId + " not found"));
            user.setCity(city);
        }

        userRepository.save(user);

        return UserUpdateResponseDTO.builder()
            .id(user.getId())
            .build();
    }

    @Override
    public UserPageDTO page(UserPageRequestDTO requestDTO, Pageable pageable) {
        String name = null;
        String phone = null;
        City city = null;

        if (requestDTO != null) {
            name = requestDTO.getName();
            phone = requestDTO.getPhone();

            if (requestDTO.getCityId() != null) {
                Long cityId = requestDTO.getCityId();
                city = citiesRepository.findById(cityId).orElseThrow(
                    () -> new EntityNotFoundException("City with id " + cityId + " not found")
                );
            }
        }

        Page<User> query = userRepository.findWithFilter(city, name, phone, pageable);
        List<UserDTO> collect = query.get()
            .map(userToUserDTOConverter::convert)
            .collect(Collectors.toList());

        return UserPageDTO.builder()
            .users(collect)
            .currentPage(pageable.getPageNumber())
            .currentPage(query.getTotalPages())
            .build();
    }
}

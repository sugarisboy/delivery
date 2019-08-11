package dev.muskrat.delivery.user.service;

import dev.muskrat.delivery.auth.dao.AuthorizedUser;
import dev.muskrat.delivery.user.dao.User;
import dev.muskrat.delivery.user.dto.UserDTO;
import dev.muskrat.delivery.user.dto.UserUpdateDTO;
import dev.muskrat.delivery.user.dto.UserUpdateResponseDTO;
import dev.muskrat.delivery.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public void create(AuthorizedUser authorizedUser) {
        User user = new User();
        user.setAuthorizedUser(authorizedUser);
    }

    @Override
    public UserUpdateResponseDTO update(AuthorizedUser executor, UserUpdateDTO userUpdateDTO) {
        User user = executor.getUser();

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
            .id(executor.getId())
            .build();
    }

    @Override
    public UserDTO get(AuthorizedUser authorizedUser) {
        User user = authorizedUser.getUser();

        return UserDTO.builder()
            .email(user.getEmail())
            .firstName(user.getFirstName())
            .lastName(user.getLastName())
            .phone(user.getPhone())
            .build();
    }
}

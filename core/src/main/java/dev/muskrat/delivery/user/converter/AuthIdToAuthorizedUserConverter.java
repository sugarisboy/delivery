package dev.muskrat.delivery.user.converter;

import dev.muskrat.delivery.components.converter.ObjectConverter;
import dev.muskrat.delivery.components.exception.EntityNotFoundException;
import dev.muskrat.delivery.user.dao.User;
import dev.muskrat.delivery.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuthIdToAuthorizedUserConverter implements ObjectConverter<Long, User> {

    private final UserRepository userRepository;

    @Override
    public User convert(Long id) {
        Optional<User> byId = userRepository.findById(id);
        if (byId.isEmpty())
            throw new EntityNotFoundException("User with id " + id + " not found");

        User user = byId.get();
        return user;
    }
}

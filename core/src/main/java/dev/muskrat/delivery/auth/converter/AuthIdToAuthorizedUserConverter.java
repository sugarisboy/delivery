package dev.muskrat.delivery.auth.converter;

import dev.muskrat.delivery.auth.dao.User;
import dev.muskrat.delivery.auth.repository.UserRepository;
import dev.muskrat.delivery.components.converter.ObjectConverter;
import dev.muskrat.delivery.components.exception.EntityNotFoundException;
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

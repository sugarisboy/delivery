package dev.muskrat.delivery.auth.converter;

import dev.muskrat.delivery.auth.dao.AuthorizedUser;
import dev.muskrat.delivery.auth.repository.AuthorizedUserRepository;
import dev.muskrat.delivery.components.converter.ObjectConverter;
import dev.muskrat.delivery.components.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuthIdToAuthorizedUserConverter implements ObjectConverter<Long, AuthorizedUser> {

    private final AuthorizedUserRepository authorizedUserRepository;

    @Override
    public AuthorizedUser convert(Long id) {
        Optional<AuthorizedUser> byId = authorizedUserRepository.findById(id);
        if (byId.isEmpty())
            throw new EntityNotFoundException("User with id " + id + " not found");

        AuthorizedUser authorizedUser = byId.get();
        return authorizedUser;
    }
}

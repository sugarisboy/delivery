/*
package dev.muskrat.delivery.components.service;

import dev.muskrat.delivery.partner.dto.PartnerDTO;
import dev.muskrat.delivery.partner.service.PartnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class DeliveryUserDetailsServiceImpl implements UserDetailsService {

    private final PartnerService partnerService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<PartnerDTO> optionalPartnerDTO = partnerService.findByEmail(username);

        if (optionalPartnerDTO.isPresent()) {
            PartnerDTO partnerDTO = optionalPartnerDTO.get();

            return new User(
                partnerDTO.getEmail(),
                partnerDTO.getPassword(),
                Set.of(new SimpleGrantedAuthority("partner"))
            );
        }
        throw new UsernameNotFoundException("No such user");
    }

}
*/

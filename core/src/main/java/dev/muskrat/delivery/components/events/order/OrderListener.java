package dev.muskrat.delivery.components.events.order;

import dev.muskrat.delivery.auth.dao.Role;
import dev.muskrat.delivery.auth.dao.Status;
import dev.muskrat.delivery.auth.repository.RoleRepository;
import dev.muskrat.delivery.order.dao.Order;
import dev.muskrat.delivery.user.dao.User;
import dev.muskrat.delivery.user.repository.UserRepository;
import dev.muskrat.delivery.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
@RequiredArgsConstructor
public class OrderListener {

    private final UserService userService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @EventListener
    public void autoCreateUser(OrderCreateEvent event) {
        Order order = event.getOrder();
        User user = order.getUser();
        if (user == null) {
            user = userRepository
                .findByEmail(order.getEmail())
                .orElse(registerUser(order));
        }
        order.setUser(user);
    }

    private User registerUser(Order order) {
        User user = new User();

        Role role = roleRepository.findByName("USER").orElseThrow();

        user.setEmail(order.getEmail());
        user.setPhone(order.getPhone());
        user.setStatus(Status.NOT_ACTIVE);
        user.setUsername(order.getEmail());
        user.setFirstName(order.getName());
        user.setPassword("");
        user.setRoles(Collections.singletonList(role));

        return userService.register(user);
    }
}

package dev.muskrat.delivery.auth;

import dev.muskrat.delivery.auth.forms.LoginForm;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/auth")
public class AuthContoller {

    @PostMapping("/auth/login")
    public String auth(
            @RequestBody LoginForm form
    ) {
        return null;
    }

    @PostMapping("/auth/register")
    public String register(
            @RequestBody LoginForm form
    ) {
        return null;
    }

}

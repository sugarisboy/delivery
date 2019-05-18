package dev.muskrat.delivery.auth;

import dev.muskrat.delivery.auth.forms.Partner;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/auth")
public class AuthController {

    @PostMapping("/auth/login")
    public String auth(
            @RequestBody Partner partner
    ) {
        System.out.println(partner.getEmail());
        return null;
    }

    @PostMapping("/auth/register")
    public String register(
            @RequestBody Partner partner
    ) {
        System.out.println(partner.getCompany());
        return null;
    }
}

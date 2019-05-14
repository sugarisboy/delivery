package dev.muskrat.delivery.auth.forms;

import lombok.Getter;
import lombok.Setter;

public class LoginForm {

    @Setter
    @Getter
    private String email;

    @Setter
    @Getter
    private String password;


}

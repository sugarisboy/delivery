package dev.muskrat.delivery.auth.forms;

import lombok.Getter;
import lombok.Setter;

public class Partner {

    @Getter
    @Setter
    private String email;

    @Getter
    @Setter
    private String company;

    @Getter
    @Setter
    private String phone;

    @Getter
    @Setter
    private String password;

    @Getter
    private String repeatPassword;
}

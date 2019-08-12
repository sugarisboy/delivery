package dev.muskrat.delivery.user.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Data
@Builder
public class UserUpdateDTO {

    @NotNull
    @Positive
    private Long id;

    @Email
    private String email;

    @Size(min = 3, max = 15)
    private String phone;

    private String lastName;
    private String firstName;
}

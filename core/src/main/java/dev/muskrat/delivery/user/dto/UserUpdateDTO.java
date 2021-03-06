package dev.muskrat.delivery.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateDTO {

    @NotNull
    @Positive
    private Long id;

    @Email
    private String email;

    @Size(min = 5, max = 15)
    private String phone;

    @Size(min = 3)
    private String name;

    @Positive
    private Long cityId;
}

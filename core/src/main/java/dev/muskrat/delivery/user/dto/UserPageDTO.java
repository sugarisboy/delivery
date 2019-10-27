package dev.muskrat.delivery.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPageDTO {

    private List<UserDTO> users;
    private Integer currentPage;
    private Integer lastPage;
}

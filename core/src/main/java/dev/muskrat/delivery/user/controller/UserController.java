package dev.muskrat.delivery.user.controller;

import dev.muskrat.delivery.user.dto.*;
import dev.muskrat.delivery.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('PARTNER') or @authorizationServiceImpl.isEquals(authentication, #id)")
    public UserDTO findById(
        @PathVariable Long id
    ) {
        return userService.findById(id);
    }

    @PatchMapping("/update")
    @PreAuthorize("hasAuthority('ADMIN') or @authorizationServiceImpl.isEquals(authentication, #userUpdateDTO.id)")
    public UserUpdateResponseDTO update(
        @Valid @RequestBody UserUpdateDTO userUpdateDTO
    ) {
        return userService.update(userUpdateDTO);
    }

    @GetMapping("/email/{email:.+}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or authentication.principal == #email")
    public UserDTO findByEmail(
        @NotNull @PathVariable String email
    ) {
        return userService.findByEmail(email);
    }

    @PostMapping("/page")
    @PreAuthorize("hasAuthority('ADMIN')")
    public UserPageDTO page(
        @Valid @RequestBody(required = false) UserPageRequestDTO userPageRequestDTO,
        @PageableDefault(size = 20, sort = {"id"}, direction = Sort.Direction.DESC) Pageable page
    ) {
        return userService.page(userPageRequestDTO, page);
    }
}

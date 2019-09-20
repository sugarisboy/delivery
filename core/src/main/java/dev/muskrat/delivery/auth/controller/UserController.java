package dev.muskrat.delivery.auth.controller;

import dev.muskrat.delivery.auth.dto.UserDTO;
import dev.muskrat.delivery.auth.dto.UserUpdateDTO;
import dev.muskrat.delivery.auth.dto.UserUpdateResponseDTO;
import dev.muskrat.delivery.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public UserDTO findById(
        @PathVariable Long id,
        @RequestHeader("Key") String key,
        @RequestHeader("Authorization") String authorization
    ) {
        return userService.findById(id, key, authorization);
    }

    @PatchMapping("/update")
    @PreAuthorize("hasAuthority('ADMIN') or @authorizationServiceImpl.isEquals(authentication, #userUpdateDTO.id)")
    public UserUpdateResponseDTO update(
        @Valid @RequestBody UserUpdateDTO userUpdateDTO
    ) {
        return userService.update(userUpdateDTO);
    }
}

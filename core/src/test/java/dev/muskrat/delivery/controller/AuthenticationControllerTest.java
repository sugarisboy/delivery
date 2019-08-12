package dev.muskrat.delivery.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import dev.muskrat.delivery.auth.dao.AuthorizedUser;
import dev.muskrat.delivery.auth.dto.UserLoginDTO;
import dev.muskrat.delivery.auth.dto.UserLoginResponseDTO;
import dev.muskrat.delivery.auth.dto.UserRegisterDTO;
import dev.muskrat.delivery.auth.dto.UserRegisterResponseDTO;
import dev.muskrat.delivery.auth.repository.AuthorizedUserRepository;
import dev.muskrat.delivery.auth.security.jwt.JwtTokenProvider;
import dev.muskrat.delivery.auth.service.AuthorizationService;
import lombok.SneakyThrows;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@Transactional
public class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AuthorizedUserRepository authorizedUserRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private AuthorizationService authorizationService;

    @Test
    @SneakyThrows
    @Transactional
    public void register() {
        UserRegisterDTO userRegisterDTO = UserRegisterDTO.builder()
            .email("sugarisboy@muskrat.dev")
            .firstName("Nikita")
            .lastName("Smirnov")
            .password("test")
            .repeatPassword("test")
            .build();

        String contentAsString = mockMvc.perform(post("/auth/register")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(userRegisterDTO))
        )
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();

        UserRegisterResponseDTO userRegisterResponseDTO = objectMapper
            .readValue(contentAsString, UserRegisterResponseDTO.class);

        Long registeredPartnerId = userRegisterResponseDTO.getId();

        AuthorizedUser authorizedUser = authorizedUserRepository
            .findById(registeredPartnerId).orElseThrow();

        assertEquals(authorizedUser.getUsername(), userRegisterResponseDTO.getUsername());
        assertNotNull(userRegisterResponseDTO.getAccess());
    }

    @Test
    @SneakyThrows
    @Transactional
    public void login() {
        // Generated in DemoData
        UserLoginDTO userLoginDTO = UserLoginDTO.builder()
            .username("user@gmail.com")
            .password("test")
            .build();

        String contentAsString = mockMvc.perform(post("/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(userLoginDTO))
        )
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();

        UserLoginResponseDTO userLoginResponseDTO = objectMapper
            .readValue(contentAsString, UserLoginResponseDTO.class);

        String access = userLoginResponseDTO.getAccess();

        assertTrue(jwtTokenProvider.validateToken(access));
    }

    @Test
    @SneakyThrows
    @Transactional
    public void refresh() {
        UserLoginDTO userLoginDTO = UserLoginDTO.builder()
            .username("user@gmail.com")
            .password("test")
            .build();

        UserLoginResponseDTO oldUserLoginDTO = authorizationService.login(userLoginDTO);

        String access = oldUserLoginDTO.getAccess();

        // First refresh
        String contentAsString = mockMvc.perform(post("/auth/refresh")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer_" + access)
        )
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();


        UserLoginResponseDTO newUserLoginDTO = objectMapper
            .readValue(contentAsString, UserLoginResponseDTO.class);

        assertTrue(jwtTokenProvider.validateToken(newUserLoginDTO.getAccess()));

    }
}

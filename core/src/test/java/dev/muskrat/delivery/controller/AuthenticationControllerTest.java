package dev.muskrat.delivery.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import dev.muskrat.delivery.DemoData;
import dev.muskrat.delivery.auth.dto.UserLoginDTO;
import dev.muskrat.delivery.auth.dto.UserLoginResponseDTO;
import dev.muskrat.delivery.auth.dto.UserRegisterDTO;
import dev.muskrat.delivery.auth.dto.UserRegisterResponseDTO;
import dev.muskrat.delivery.auth.security.jwt.JwtToken;
import dev.muskrat.delivery.auth.security.jwt.JwtTokenProvider;
import dev.muskrat.delivery.auth.security.jwt.TokenStore;
import dev.muskrat.delivery.auth.service.AuthorizationService;
import dev.muskrat.delivery.user.dao.User;
import dev.muskrat.delivery.user.repository.UserRepository;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
    private UserRepository userRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private AuthorizationService authorizationService;

    @Autowired
    private DemoData demoData;

    @Autowired
    private TokenStore tokenStore;


    public List<JwtToken> demo() {
        List<JwtToken> jwts = new ArrayList<>();

        UserLoginDTO loginDTO = UserLoginDTO.builder()
            .username("user@gmail.com")
            .password("test")
            .build();

        for (int i = 0; i < 3; i++) {
            UserLoginResponseDTO responseDTO = authorizationService.login(loginDTO);
            jwts.add(
                new JwtToken(
                    responseDTO.getKey(),
                    responseDTO.getAccess(),
                    responseDTO.getRefresh()
                )
            );
        }
        return jwts;
    }

    @Test
    @SneakyThrows
    @Transactional
    public void registerUserTest() {
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

        User user = userRepository
            .findById(registeredPartnerId).orElseThrow();

        assertEquals(user.getUsername(), userRegisterResponseDTO.getUsername());
        assertNotNull(userRegisterResponseDTO.getAccess());
    }

    @Test
    @SneakyThrows
    @Transactional
    public void loginUserTest() {
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

        String key = userLoginResponseDTO.getKey();
        String access = userLoginResponseDTO.getAccess();

        assertTrue(jwtTokenProvider.validateAccessToken(key, access));
    }

    @Test
    @SneakyThrows
    @Transactional
    public void refreshTokenTest() {
        UserLoginDTO userLoginDTO = UserLoginDTO.builder()
            .username("user@gmail.com")
            .password("test")
            .build();

        UserLoginResponseDTO oldUserLoginDTO = authorizationService.login(userLoginDTO);

        String access = oldUserLoginDTO.getAccess();

        String contentAsString = mockMvc.perform(post("/auth/refresh")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer_" + oldUserLoginDTO.getRefresh())
            .header("Key", oldUserLoginDTO.getKey())
        )
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();


        UserLoginResponseDTO newUserLoginDTO = objectMapper
            .readValue(contentAsString, UserLoginResponseDTO.class);

        String key = newUserLoginDTO.getKey();
        access = newUserLoginDTO.getAccess();

        assertTrue(jwtTokenProvider.validateAccessToken(key, access));

        demoData.KEY_USER = key;
        demoData.ACCESS_USER = "Bearer_" + access;
    }

    @Test
    @SneakyThrows
    @Transactional
    public void logout() {
        List<JwtToken> jwts = demo();
        JwtToken token = jwts.get(2);

        User user = demoData.users.get(0);
        Long userId = user.getId();

        String key = token.getKey();
        String access = token.getAccess();

        mockMvc.perform(get("/auth/logout")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer_" + access)
            .header("Key", key)
        )
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();

        assertFalse(tokenStore.containsKey(userId, key));
    }

    @Test
    @SneakyThrows
    @Transactional
    public void logoutAll() {
        List<JwtToken> jwts = demo();
        JwtToken token = jwts.get(2);

        User user = demoData.users.get(1);
        Long userId = user.getId();

        String key = demoData.KEY_USER;
        String access = demoData.ACCESS_USER;

        JwtToken demoToken = tokenStore.readTokenByKey(userId, key).get();

        mockMvc.perform(get("/auth/logout?type=all")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .header("Authorization", access)
            .header("Key", key)
        )
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();

        assertTrue(tokenStore.findTokensByUserId(userId).size() == 0);

        //tokenStore.saveToken(userId, demoToken);
    }

    @Test
    @SneakyThrows
    @Transactional
    public void logoutSecure() {
        List<JwtToken> jwts = demo();
        JwtToken token = jwts.get(2);

        User user = demoData.users.get(1);
        Long userId = user.getId();

        String key = demoData.KEY_USER;
        String access = demoData.ACCESS_USER;

        mockMvc.perform(get("/auth/logout?type=secure")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .header("Authorization", access)
            .header("Key", key)
        )
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();

        Set<JwtToken> tokensByUserId = tokenStore.findTokensByUserId(userId);

        /*tokenStore.findTokensByUserId(userId)
            .stream()
            .mapToInt(Object::hashCode)
            .forEach(System.out::println);*/

        assertTrue(tokenStore.findTokensByUserId(userId).size() == 1);

        JwtToken checkable = tokensByUserId.stream().findFirst().get();
        assertEquals(key, checkable.getKey());
    }
}

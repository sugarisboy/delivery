package dev.muskrat.delivery.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.muskrat.delivery.DemoData;
import dev.muskrat.delivery.user.dao.User;
import dev.muskrat.delivery.user.dto.UserPageDTO;
import dev.muskrat.delivery.user.dto.UserUpdateDTO;
import dev.muskrat.delivery.user.dto.UserUpdateResponseDTO;
import dev.muskrat.delivery.user.repository.UserRepository;
import dev.muskrat.delivery.cities.dao.City;
import dev.muskrat.delivery.partner.dao.Partner;
import dev.muskrat.delivery.user.dto.UserDTO;
import lombok.SneakyThrows;
import org.junit.Assert;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DemoData demoData;

    @Autowired
    private UserRepository userRepository;

    @Test
    @SneakyThrows
    public void userFindById() {
        Partner partner = demoData.partner;
        User user = partner.getUser();
        Long userId = user.getId();

        String contentAsString = mockMvc.perform(get("/user/" + userId)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .header("Authorization", demoData.ACCESS_PARTNER)
            .header("Key", demoData.KEY_PARTNER)
        )
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();

        UserDTO userDTO = objectMapper
            .readValue(contentAsString, UserDTO.class);

        assertEquals(userDTO.getEmail(), user.getEmail());
    }

    @Test
    @SneakyThrows
    public void pageTest() {

        String contentAsString = mockMvc.perform(post("/user/page")
            //.contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .header("Authorization", demoData.ACCESS_ADMIN)
            .header("Key", demoData.KEY_ADMIN)
        )
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();

        UserPageDTO userPageDTO = objectMapper
            .readValue(contentAsString, UserPageDTO.class);

        System.out.println();
    }

    @Test
    @SneakyThrows
    @Transactional
    public void userUpdateTest() {
        User user = demoData.users.get(1);
        Long userId = user.getId();

        Long cityId = demoData.cities.get(0).getId();

        UserUpdateDTO userUpdateDTO = UserUpdateDTO.builder()
            .name("new1")
            .phone("1234567890")
            .email("sugarisboy@muskrat.dev")
            .cityId(cityId)
            .id(userId)
            .build();

        String contentAsString = mockMvc.perform(patch("/user/update")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(userUpdateDTO))
            .header("Authorization", demoData.ACCESS_USER)
            .header("Key", demoData.KEY_USER)
        )
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();

        UserUpdateResponseDTO responseDTO = objectMapper
            .readValue(contentAsString, UserUpdateResponseDTO.class);
        userId = responseDTO.getId();
        user = userRepository.findById(userId).get();

        assertEquals(   userUpdateDTO.getEmail(),      user.getEmail()      );
        assertEquals(   userUpdateDTO.getName(),       user.getName()       );
        assertEquals(   userUpdateDTO.getPhone(),      user.getPhone()      );
    }

    @Test
    @SneakyThrows
    @Transactional
    public void changeUserCityTest() {
        User user = demoData.users.get(1);
        Long userId = user.getId();
        assertTrue(user.getCity() != null);

        City city = demoData.cities.get(0);
        Long cityId = city.getId();

        UserUpdateDTO userUpdateDTO = UserUpdateDTO.builder()
            .id(userId)
            .cityId(cityId)
            .build();

        String contentAsString = mockMvc.perform(patch("/user/update")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(userUpdateDTO))
            .header("Authorization", demoData.ACCESS_USER)
            .header("Key", demoData.KEY_USER)
        )
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();

        UserUpdateResponseDTO responseDTO = objectMapper
            .readValue(contentAsString, UserUpdateResponseDTO.class);
        userId = responseDTO.getId();
        user = userRepository.findById(userId).get();

        assertEquals(cityId, user.getCity().getId());
    }
}

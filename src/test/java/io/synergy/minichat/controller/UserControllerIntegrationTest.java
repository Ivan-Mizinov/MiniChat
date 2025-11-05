package io.synergy.minichat.controller;

import io.synergy.minichat.dto.NameDto;
import io.synergy.minichat.dto.UserDto;
import io.synergy.minichat.dto.UserResponseDto;
import io.synergy.minichat.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("Интеграционные тесты контроллера пользователей")
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserService userService;

    @Test
    @DisplayName("Проверка успешного получения случайного пользователя")
    void testGenerateRandomUser_Success() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setGender("male");
        userDto.setName(new NameDto("John", "Doe", "Mr"));

        List<UserDto> userList = List.of(userDto);
        UserResponseDto responseDto = new UserResponseDto();
        responseDto.setResults(userList);

        when(userService.findAll()).thenReturn(responseDto);

        ResultActions result = mvc.perform(get("/api/service/user"));

        result.andExpect(status().isOk());

        result.andExpect(jsonPath("$.results").isArray());
        result.andExpect(jsonPath("$.results[0].gender").value("male"));
        result.andExpect(jsonPath("$.results[0].name.first").value("John"));
        result.andExpect(jsonPath("$.results[0].name.last").value("Doe"));
    }

    @Test
    @DisplayName("Проверка получения пустого ответа при отсутствии пользователей")
    void testGenerateRandomUser_EmptyResponse() throws Exception {
        UserResponseDto emptyResponse = new UserResponseDto();
        emptyResponse.setResults(List.of());

        when(userService.findAll()).thenReturn(emptyResponse);

        ResultActions result = mvc.perform(get("/api/service/user"));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.results").isArray());
        result.andExpect(jsonPath("$.results.length()").value(0));
    }
}
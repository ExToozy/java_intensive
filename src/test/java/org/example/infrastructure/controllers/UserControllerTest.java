package org.example.infrastructure.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.core.dtos.user_dtos.ChangeAdminStatusDto;
import org.example.core.dtos.user_dtos.UpdateUserDto;
import org.example.core.dtos.user_dtos.UserDto;
import org.example.core.models.User;
import org.example.core.services.UserService;
import org.example.infrastructure.data.mappers.UserMapper;
import org.example.infrastructure.exception_handlers.UserExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserController authController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController)
                .setControllerAdvice(new UserExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("Get all users successfully when user is admin")
    void testGetUsers_whenUserIsAdmin_thenReturnUsers() throws Exception {
        List<User> users = List.of(
                new User(1, "test1", "password1", true),
                new User(2, "test2", "password2", false)
        );
        List<UserDto> userDtos = List.of(
                new UserDto(1, "test1"),
                new UserDto(2, "test2")
        );
        String expectedJson = """
                [
                  {
                    "userId": 1,
                    "email": "test1"
                  },
                  {
                    "userId": 2,
                    "email": "test2"
                  }
                ]
                """;
        when(userService.isUserAdmin(1)).thenReturn(true);
        when(userService.getAll()).thenReturn(users);
        when(userMapper.toUserDtoList(users)).thenReturn(userDtos);

        mockMvc.perform(get("/api/v1/users")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("Authorization", "Bearer 1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));

        verify(userService).getAll();
        verify(userService).isUserAdmin(1);
        verify(userMapper).toUserDtoList(users);
    }

    @Test
    @DisplayName("Get user by ID successfully when user is not admin")
    void testGetUser_whenUserIsNotAdmin_thenReturnUser() throws Exception {
        User user = new User(1, "test1", "password1", false);
        UserDto userDto = new UserDto(1, "test1");
        String expectedJson = """
                {
                  "userId": 1,
                  "email": "test1"
                }
                """;
        when(userService.isUserAdmin(1)).thenReturn(false);
        when(userService.getById(1)).thenReturn(user);
        when(userMapper.toUserDto(user)).thenReturn(userDto);

        mockMvc.perform(get("/api/v1/users/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("Authorization", "Bearer 1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));

        verify(userService).getById(1);
        verify(userService).isUserAdmin(1);
        verify(userMapper).toUserDto(user);
    }

    @Test
    @DisplayName("Delete user successfully when user is not admin")
    void testDeleteUser_whenUserIsHimself_thenUserIsDeleted() throws Exception {
        when(userService.isUserAdmin(1)).thenReturn(false);

        mockMvc.perform(delete("/api/v1/users/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("Authorization", "Bearer 1"))
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(userService).remove(1);
        verify(userService).isUserAdmin(1);
    }

    @Test
    @DisplayName("Update user successfully when user is not admin")
    void testUpdateUser_whenUserIsHimself_thenUserIsUpdated() throws Exception {
        UpdateUserDto updateUserDto = new UpdateUserDto("test", "test");
        when(userService.isUserAdmin(1)).thenReturn(false);

        mockMvc.perform(put("/api/v1/users/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer 1")
                        .content(objectMapper.writeValueAsString(updateUserDto)))
                .andDo(print())
                .andExpect(status().isOk());

        verify(userService).update(1, updateUserDto);
        verify(userService).isUserAdmin(1);
    }

    @Test
    @DisplayName("Change user admin status successfully when user is admin")
    void testUpdateUserStatus_whenUserIsAdmin_thenAdminStatusIsChanged() throws Exception {
        ChangeAdminStatusDto changeAdminStatusDto = new ChangeAdminStatusDto(true);
        when(userService.isUserAdmin(1)).thenReturn(true);

        mockMvc.perform(post("/api/v1/users/1/change-admin-status")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer 1")
                        .content(objectMapper.writeValueAsString(changeAdminStatusDto)))
                .andDo(print())
                .andExpect(status().isOk());

        verify(userService).changeUserAdminStatus(1, changeAdminStatusDto);
        verify(userService).isUserAdmin(1);
    }
}

package org.example.infrastructure.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.example.core.dtos.auth_dtos.AuthDto;
import org.example.core.dtos.user_dtos.AuthUserDto;
import org.example.core.services.AuthService;
import org.example.exceptions.InvalidEmailException;
import org.example.exceptions.UserAlreadyExistException;
import org.example.exceptions.UserNotFoundException;
import org.example.infrastructure.exception_handlers.GlobalExceptionHandler;
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
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;

import java.nio.charset.StandardCharsets;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private MockMvc mockMvc;

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator javaxValidator = factory.getValidator();
        SpringValidatorAdapter validator = new SpringValidatorAdapter(javaxValidator);
        mockMvc = MockMvcBuilders.standaloneSetup(authController)
                .setValidator(validator)
                .setControllerAdvice(new UserExceptionHandler(), new GlobalExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("When user login with valid credentials then return AuthDto")
    void login_whenValidCredentials_thenReturnAuthDto() throws Exception {
        AuthUserDto authUserDto = new AuthUserDto("test@mail.ru", "password");
        AuthDto authDto = new AuthDto("1");

        when(authService.login(authUserDto)).thenReturn(authDto);

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authUserDto))
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("1"));

        verify(authService, times(1)).login(authUserDto);
    }

    @Test
    @DisplayName("When user login with invalid credentials then return UserNotFoundException")
    void login_whenUserNotFound_thenReturnUserNotFound() throws Exception {
        AuthUserDto authUserDto = new AuthUserDto("test@mail.ru", "password");

        when(authService.login(authUserDto)).thenThrow(UserNotFoundException.class);

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authUserDto))
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("User not found"));

        verify(authService, times(1)).login(authUserDto);
    }

    @Test
    @DisplayName("When user register with valid data then return Auth Dto")
    void register_whenValidData_thenReturnAuthDto() throws Exception {
        AuthUserDto authUserDto = new AuthUserDto("test@mail.ru", "password");
        AuthDto authDto = new AuthDto("1");

        when(authService.register(authUserDto)).thenReturn(authDto);

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authUserDto))
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token").value("1"));

        verify(authService, times(1)).register(authUserDto);
    }

    @Test
    @DisplayName("When user register with invalid email then return InvalidEmailException")
    void register_whenInvalidEmail_thenReturnInvalidEmail() throws Exception {
        AuthUserDto authUserDto = new AuthUserDto("test@mail.ru", "password");

        when(authService.register(authUserDto)).thenThrow(InvalidEmailException.class);

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authUserDto))
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Email is invalid"));

        verify(authService, times(1)).register(authUserDto);
    }

    @Test
    @DisplayName("When user already exists then return UserAlreadyExistsException")
    void register_whenUserAlreadyExists_thenReturnUserAlreadyExists() throws Exception {
        AuthUserDto authUserDto = new AuthUserDto("test@mail.ru", "password");

        when(authService.register(authUserDto)).thenThrow(UserAlreadyExistException.class);

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authUserDto))
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("User already exist"));

        verify(authService, times(1)).register(authUserDto);
    }

    @Test
    @DisplayName("When user register with field errors then return Field Errors")
    void register_whenFieldErrors_thenReturnFieldErrors() throws Exception {
        AuthUserDto authUserDto = new AuthUserDto(null, "");
        String expectedJson = """
                {
                  "password": [
                    "Password must not be blank",
                    "Password must be between 3 and 255 characters"
                  ],
                  "email": [
                    "Email must not be blank",
                    "Email must not be null"
                  ]
                }
                """;
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authUserDto))
                        .characterEncoding(StandardCharsets.UTF_8))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().json(expectedJson));
    }
}

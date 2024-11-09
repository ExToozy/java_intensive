package org.example.infrastructure.controllers;

import com.example.logging_aspect_starter.annotations.Loggable;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.core.dtos.auth_dtos.AuthDto;
import org.example.core.dtos.user_dtos.AuthUserDto;
import org.example.core.services.AuthService;
import org.example.exceptions.InvalidEmailException;
import org.example.exceptions.UserAlreadyExistException;
import org.example.exceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


@Loggable
@RestController
@RequestMapping(value = "/api/v1/auth", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Operations for user authentication")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "User login", description = "Authenticates a user with email and password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User authenticated successfully"),
            @ApiResponse(responseCode = "400", description = "User not found or validation error or missing fields in JSON request"),
    })
    @PostMapping(value = "/login")
    public AuthDto login(@RequestBody @Valid AuthUserDto authUserDto) throws UserNotFoundException {
        return authService.login(authUserDto);
    }

    @Operation(summary = "User registration", description = "Registers a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid email or errors in json request"),
            @ApiResponse(responseCode = "409", description = "User already exists")
    })
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthDto register(@RequestBody @Valid AuthUserDto authUserDto) throws UserAlreadyExistException, InvalidEmailException {
        return authService.register(authUserDto);
    }
}

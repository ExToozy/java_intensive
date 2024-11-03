package org.example.infrastructure.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.example.annotations.Loggable;
import org.example.core.dtos.auth_dtos.AuthDto;
import org.example.core.dtos.user_dtos.AuthUserDto;
import org.example.core.models.User;
import org.example.core.services.AuthService;
import org.example.exceptions.InvalidEmailException;
import org.example.exceptions.UserAlreadyExistException;
import org.example.exceptions.UserNotFoundException;
import org.example.infrastructure.data.mappers.AuthMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@Loggable
@RestController
@RequestMapping("/api/v2/auth")
@RequiredArgsConstructor
@Api(tags = "Authentication", description = "Operations for user authentication")
public class AuthController {

    private final AuthService authService;
    private final AuthMapper authMapper;

    @ApiOperation(value = "User login", notes = "Authenticates a user with email and password")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User authenticated successfully"),
            @ApiResponse(code = 400, message = "User not found or validation error or missing fields in JSON request"),
    })
    @PostMapping("/login")
    public AuthDto login(@RequestBody @Valid AuthUserDto authUserDto) throws UserNotFoundException {
        User user = authService.login(authUserDto);
        return authMapper.toAuthDtoMap(user);
    }

    @ApiOperation(value = "User registration", notes = "Registers a new user")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "User registered successfully"),
            @ApiResponse(code = 400, message = "Invalid email or errors in json request"),
            @ApiResponse(code = 409, message = "User already exists")
    })
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthDto register(@RequestBody @Valid AuthUserDto authUserDto) throws UserAlreadyExistException, InvalidEmailException {
        User user = authService.register(authUserDto);
        return authMapper.toAuthDtoMap(user);
    }
}

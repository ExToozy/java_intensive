package org.example.infrastructure.controllers;

import com.example.audit_aspect_starter.annotations.Auditable;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.core.dtos.user_dtos.ChangeAdminStatusDto;
import org.example.core.dtos.user_dtos.UpdateUserDto;
import org.example.core.dtos.user_dtos.UserDto;
import org.example.core.services.UserService;
import org.example.exceptions.InvalidEmailException;
import org.example.exceptions.InvalidTokenException;
import org.example.exceptions.UserNotFoundException;
import org.example.infrastructure.constants.ErrorMessageConstants;
import org.example.infrastructure.data.mappers.UserMapper;
import org.example.infrastructure.util.JwtProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/users", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = "User Management", description = "Operations for managing users")
public class UserController {
    private final UserMapper userMapper;
    private final UserService userService;
    private final JwtProvider jwtProvider;

    @Operation(summary = "Retrieve all users", description = "Fetches a list of all users (admin access required)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved users"),
            @ApiResponse(responseCode = "401", description = "Invalid token or user unauthorized"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @Auditable
    @GetMapping
    public List<UserDto> getUsers(
            @RequestHeader("Authorization") String token
    ) throws InvalidTokenException, AccessDeniedException {
        int userIdFromToken = jwtProvider.getUserIdFromToken(token);
        throwAccessDeniedIfUserNotAdmin(userIdFromToken);
        return userMapper.toUserDtoList(userService.getAll());
    }

    @Operation(summary = "Delete a user", description = "Deletes a user by ID (admin or self-access required)")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Successfully deleted user"),
            @ApiResponse(responseCode = "401", description = "Invalid token or user unauthorized"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @Auditable
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(
            @RequestHeader("Authorization") String token,
            @PathVariable("id") int userId
    ) throws InvalidTokenException, AccessDeniedException {
        int userIdFromToken = jwtProvider.getUserIdFromToken(token);
        throwAccessDeniedIfUserNotAdminOrSelfAccess(userId, userIdFromToken);
        userService.remove(userId);
    }

    @Operation(summary = "Retrieve a user by ID", description = "Fetches a user by ID (admin or self-access required)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved user"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "401", description = "Invalid token or user unauthorized"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @Auditable
    @GetMapping("/{id}")
    public UserDto getUser(
            @RequestHeader("Authorization") String token,
            @PathVariable("id") int userId
    ) throws UserNotFoundException, InvalidTokenException, AccessDeniedException {
        int userIdFromToken = jwtProvider.getUserIdFromToken(token);
        throwAccessDeniedIfUserNotAdminOrSelfAccess(userId, userIdFromToken);
        return userMapper.toUserDto(userService.getById(userId));
    }

    @Operation(summary = "Update a user", description = "Updates user information (admin or self-access required)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully updated user"),
            @ApiResponse(responseCode = "400", description = "Validation error"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "401", description = "Invalid token or user unauthorized"),
    })
    @Auditable
    @PutMapping("/{id}")
    public void updateUser(
            @RequestHeader("Authorization") String token,
            @PathVariable("id") int userId,
            @RequestBody @Valid UpdateUserDto updateUserDto
    ) throws UserNotFoundException, InvalidEmailException, InvalidTokenException, AccessDeniedException {
        int userIdFromToken = jwtProvider.getUserIdFromToken(token);
        throwAccessDeniedIfUserNotAdminOrSelfAccess(userId, userIdFromToken);
        userService.update(userId, updateUserDto);
    }

    @Operation(summary = "Change user admin status", description = "Changes the admin status of a user (admin access required)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully changed user admin status"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "401", description = "Invalid token or user unauthorized"),
    })
    @Auditable
    @PostMapping("/{id}/change-admin-status")
    public void updateUserStatus(
            @RequestHeader("Authorization") String token,
            @PathVariable("id") int userId,
            @RequestBody @Valid ChangeAdminStatusDto changeAdminStatusDto
    ) throws UserNotFoundException, InvalidTokenException, AccessDeniedException {
        int userIdFromToken = jwtProvider.getUserIdFromToken(token);
        throwAccessDeniedIfUserNotAdmin(userIdFromToken);
        userService.changeUserAdminStatus(userId, changeAdminStatusDto);
    }

    private void throwAccessDeniedIfUserNotAdmin(int userIdFromToken) throws AccessDeniedException {
        throwAccessDeniedIfUserNotHasAccess(!userService.isUserAdmin(userIdFromToken));
    }

    private void throwAccessDeniedIfUserNotAdminOrSelfAccess(int userId, int userIdFromToken) throws AccessDeniedException {
        throwAccessDeniedIfUserNotHasAccess(
                !userService.isUserAdmin(userIdFromToken) &&
                        userIdFromToken != userId
        );
    }

    private void throwAccessDeniedIfUserNotHasAccess(boolean accessExp) throws AccessDeniedException {
        if (accessExp) {
            throw new AccessDeniedException(ErrorMessageConstants.ACCESS_DENIED);
        }
    }
}

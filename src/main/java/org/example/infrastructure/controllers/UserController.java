package org.example.infrastructure.controllers;

import com.example.audit_aspect_starter.annotations.Auditable;
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
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
//@Api(tags = "User Management", description = "Operations for managing users")
public class UserController {
    private final UserMapper userMapper;
    private final UserService userService;
    private final JwtProvider jwtProvider;

    //    @ApiOperation(value = "Retrieve all users", notes = "Fetches a list of all users (admin access required)")
//    @ApiResponses({
//            @ApiResponse(code = 200, message = "Successfully retrieved users"),
//            @ApiResponse(code = 401, message = "Invalid token or user unauthorized"),
//            @ApiResponse(code = 403, message = "Access denied")
//    })
    @Auditable
    @GetMapping
    public List<UserDto> getUsers(
            @RequestHeader("Authorization") String token
    ) throws InvalidTokenException, AccessDeniedException {
        int userIdFromToken = jwtProvider.getUserIdFromToken(token);
        throwAccessDeniedIfUserNotAdmin(userIdFromToken);
        return userMapper.toUserDtoList(userService.getAll());
    }

    //    @ApiOperation(value = "Delete a user", notes = "Deletes a user by ID (admin or self-access required)")
//    @ApiResponses({
//            @ApiResponse(code = 204, message = "Successfully deleted user"),
//            @ApiResponse(code = 401, message = "Invalid token or user unauthorized"),
//            @ApiResponse(code = 403, message = "Access denied")
//    })
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

    //    @ApiOperation(value = "Retrieve a user by ID", notes = "Fetches a user by ID (admin or self-access required)")
//    @ApiResponses({
//            @ApiResponse(code = 200, message = "Successfully retrieved user"),
//            @ApiResponse(code = 400, message = "User not found"),
//            @ApiResponse(code = 401, message = "Invalid token or user unauthorized"),
//            @ApiResponse(code = 403, message = "Access denied")
//    })
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

    //    @ApiOperation(value = "Update a user", notes = "Updates user information (admin or self-access required)")
//    @ApiResponses({
//            @ApiResponse(code = 200, message = "Successfully updated user"),
//            @ApiResponse(code = 400, message = "Validation error"),
//            @ApiResponse(code = 400, message = "User not found"),
//            @ApiResponse(code = 403, message = "Access denied"),
//            @ApiResponse(code = 401, message = "Invalid token or user unauthorized"),
//    })
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

    //    @ApiOperation(value = "Change user admin status", notes = "Changes the admin status of a user (admin access required)")
//    @ApiResponses({
//            @ApiResponse(code = 200, message = "Successfully changed user admin status"),
//            @ApiResponse(code = 400, message = "User not found"),
//            @ApiResponse(code = 403, message = "Access denied"),
//            @ApiResponse(code = 401, message = "Invalid token or user unauthorized"),
//    })
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

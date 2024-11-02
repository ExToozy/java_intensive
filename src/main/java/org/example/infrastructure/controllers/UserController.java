package org.example.infrastructure.controllers;

import lombok.RequiredArgsConstructor;
import org.example.annotations.Loggable;
import org.example.core.dtos.user_dtos.ChangeAdminStatusDto;
import org.example.core.dtos.user_dtos.UpdateUserDto;
import org.example.core.dtos.user_dtos.UserDto;
import org.example.core.services.UserService;
import org.example.exceptions.InvalidEmailException;
import org.example.exceptions.InvalidTokenException;
import org.example.exceptions.UserNotFoundException;
import org.example.infrastructure.constants.ErrorMessageConstants;
import org.example.infrastructure.data.mappers.UserMapper;
import org.example.infrastructure.util.TokenHelper;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.nio.file.AccessDeniedException;
import java.util.List;


@Loggable
@RestController
@RequestMapping("/api/v2/users")
@RequiredArgsConstructor
public class UserController {
    private final UserMapper userMapper;
    private final UserService userService;

    @GetMapping
    public List<UserDto> getUsers(
            @RequestHeader("Authorization") String token
    ) throws InvalidTokenException, AccessDeniedException {
        int userIdFromToken = TokenHelper.getUserIdFromToken(token);
        throwAccessDeniedIfUserNotAdmin(userIdFromToken);
        return userMapper.toUserDtoList(userService.getAll());
    }

    @GetMapping(value = "/{id}")
    public UserDto getUser(
            @RequestHeader("Authorization") String token,
            @PathVariable("id") int userId
    ) throws UserNotFoundException, InvalidTokenException, AccessDeniedException {
        int userIdFromToken = TokenHelper.getUserIdFromToken(token);
        throwAccessDeniedIfUserNotAdminOrSelfAccess(userId, userIdFromToken);
        return userMapper.toUserDto(userService.getById(userId));

    }

    @DeleteMapping(value = "/{id}")
    public void deleteUser(
            @RequestHeader("Authorization") String token,
            @PathVariable("id") int userId
    ) throws InvalidTokenException, AccessDeniedException {
        int userIdFromToken = TokenHelper.getUserIdFromToken(token);
        throwAccessDeniedIfUserNotAdminOrSelfAccess(userId, userIdFromToken);
        userService.remove(userId);

    }

    @PutMapping(value = "/{id}")
    public void updateUser(
            @RequestHeader("Authorization") String token,
            @PathVariable("id") int userId,
            @RequestBody @Valid UpdateUserDto updateUserDto
    ) throws UserNotFoundException, InvalidEmailException, InvalidTokenException, AccessDeniedException {
        int userIdFromToken = TokenHelper.getUserIdFromToken(token);
        throwAccessDeniedIfUserNotAdminOrSelfAccess(userId, userIdFromToken);
        userService.update(userId, updateUserDto);

    }

    @PostMapping(value = "/{id}/change-admin-status")
    public void updateUserStatus(
            @RequestHeader("Authorization") String token,
            @PathVariable("id") int userId,
            @RequestBody @Valid ChangeAdminStatusDto changeAdminStatusDto
    ) throws UserNotFoundException, InvalidTokenException, AccessDeniedException {
        int userIdFromToken = TokenHelper.getUserIdFromToken(token);
        throwAccessDeniedIfUserNotAdmin(userIdFromToken);
        userService.changeUserAdminStatus(userId, changeAdminStatusDto);

    }

    private void throwAccessDeniedIfUserNotHasAccess(boolean accessExp) throws AccessDeniedException {
        if (accessExp) {
            throw new AccessDeniedException(ErrorMessageConstants.UNKNOWN_ENDPOINT);
        }
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
}

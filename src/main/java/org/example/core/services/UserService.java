package org.example.core.services;

import lombok.RequiredArgsConstructor;
import org.example.core.dtos.user_dtos.AuthUserDto;
import org.example.core.dtos.user_dtos.ChangeAdminStatusDto;
import org.example.core.dtos.user_dtos.UpdateUserDto;
import org.example.core.models.User;
import org.example.core.repositories.IUserRepository;
import org.example.core.util.PasswordManager;
import org.example.core.util.RegexUtil;
import org.example.exceptions.InvalidEmailException;
import org.example.exceptions.UserNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Сервис для работы с пользователями.
 * Предоставляет методы для создания, удаления, обновления и получения пользователей, а также для проверки существования email.
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final IUserRepository userRepository;


    /**
     * Создаёт нового пользователя.
     *
     * @param dto данные для создания пользователя
     * @return созданный пользователь
     */
    public User create(AuthUserDto dto) {
        return userRepository.create(dto);
    }

    /**
     * Удаляет пользователя и все его привычки и отметки о выполнении.
     *
     * @param id идентификатор пользователя
     */
    public void remove(int id) {
        userRepository.remove(id);
    }

    /**
     * Удаляет пользователя и все его привычки и отметки о выполнении.
     *
     * @param id идентификатор пользователя
     */
    public boolean isUserAdmin(int id) {
        try {
            return userRepository.getById(id).isAdmin();
        } catch (UserNotFoundException e) {
            return false;
        }
    }

    /**
     * Возвращает пользователя по его email.
     *
     * @param email email пользователя
     * @return пользователь
     * @throws UserNotFoundException если пользователь не найден
     */
    public User getUserByEmail(String email) throws UserNotFoundException {
        return userRepository.getByEmail(email);
    }

    /**
     * Проверяет, существует ли пользователь с указанным email.
     *
     * @param email email для проверки
     * @return true, если пользователь существует, иначе false
     */
    public boolean checkEmailExist(String email) {
        try {
            userRepository.getByEmail(email);
            return true;
        } catch (UserNotFoundException e) {
            return false;
        }
    }

    /**
     * Возвращает всех пользователей.
     *
     * @return список всех пользователей
     */
    public List<User> getAll() {
        return userRepository.getAll();
    }

    /**
     * Изменяет статус администратора пользователя.
     *
     * @param userId идентификатор пользователя
     * @param dto    данные для изменения статуса
     * @throws UserNotFoundException если пользователь не найден
     */
    public void changeUserAdminStatus(int userId, ChangeAdminStatusDto dto) throws UserNotFoundException {
        userRepository.changeUserAdminStatus(userId, dto);
    }

    /**
     * Обновляет данные пользователя.
     *
     * @param dto данные для обновления пользователя
     * @throws UserNotFoundException если пользователь не найден
     * @throws InvalidEmailException если email некорректный
     */
    public void update(int userId, UpdateUserDto dto) throws UserNotFoundException, InvalidEmailException {
        if (dto.getEmail() == null && dto.getPassword() == null) {
            return;
        }
        if (dto.getPassword() != null) {
            dto.setPassword(PasswordManager.getPasswordHash(dto.getPassword()));
        }
        if (dto.getEmail() != null && RegexUtil.isInvalidEmail(dto.getEmail())) {
            throw new InvalidEmailException();
        }
        User user = userRepository.getById(userId);
        if (dto.getPassword() == null) dto.setPassword(user.getPassword());
        if (dto.getEmail() == null) dto.setEmail(user.getEmail());

        userRepository.update(userId, dto);
    }

    public User getById(int id) throws UserNotFoundException {
        return userRepository.getById(id);
    }
}

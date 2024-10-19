package org.example.core.services;

import org.example.core.exceptions.InvalidEmailException;
import org.example.core.exceptions.UserNotFoundException;
import org.example.core.models.User;
import org.example.core.repositories.user_repository.IUserRepository;
import org.example.core.repositories.user_repository.dtos.ChangeAdminStatusDto;
import org.example.core.repositories.user_repository.dtos.CreateUserDto;
import org.example.core.repositories.user_repository.dtos.UpdateUserDto;
import org.example.core.util.RegexUtil;

import java.util.List;

/**
 * Сервис для работы с пользователями.
 * Предоставляет методы для создания, удаления, обновления и получения пользователей, а также для проверки существования email.
 */
public class UserService {

    private final IUserRepository userRepository;

    private final HabitService habitService;

    /**
     * Конструктор {@link UserService}.
     *
     * @param userRepository {@link IUserRepository} репозиторий для работы с пользователями
     * @param habitService   {@link HabitService} сервис для работы с привычками
     */
    public UserService(IUserRepository userRepository, HabitService habitService) {
        this.userRepository = userRepository;
        this.habitService = habitService;
    }

    /**
     * Создаёт нового пользователя.
     *
     * @param dto данные для создания пользователя
     * @return созданный пользователь
     */
    public User create(CreateUserDto dto) {
        return userRepository.create(dto);
    }

    /**
     * Удаляет пользователя и все его привычки и отметки о выполнении.
     *
     * @param id идентификатор пользователя
     */
    public void remove(int id) {
        habitService.removeAllUserHabitsAndTracks(id);
        userRepository.remove(id);
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
     * @param dto данные для изменения статуса
     * @throws UserNotFoundException если пользователь не найден
     */
    public void changeUserAdminStatus(ChangeAdminStatusDto dto) throws UserNotFoundException {
        userRepository.changeUserAdminStatus(dto);
    }

    /**
     * Обновляет данные пользователя.
     *
     * @param dto данные для обновления пользователя
     * @throws UserNotFoundException если пользователь не найден
     * @throws InvalidEmailException если email некорректный
     */
    public void update(UpdateUserDto dto) throws UserNotFoundException, InvalidEmailException {
        if (RegexUtil.isInvalidEmail(dto.getEmail())) {
            throw new InvalidEmailException();
        }
        userRepository.update(dto);
    }
}

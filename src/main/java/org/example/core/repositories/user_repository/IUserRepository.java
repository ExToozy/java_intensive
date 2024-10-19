package org.example.core.repositories.user_repository;

import org.example.core.exceptions.UserNotFoundException;
import org.example.core.models.User;
import org.example.core.repositories.user_repository.dtos.ChangeAdminStatusDto;
import org.example.core.repositories.user_repository.dtos.CreateUserDto;
import org.example.core.repositories.user_repository.dtos.UpdateUserDto;

import java.util.List;

public interface IUserRepository {

    /**
     * Создаёт нового пользователя.
     *
     * @param dto {@link CreateUserDto}, данные для создания пользователя
     * @return созданный пользователь
     */
    User create(CreateUserDto dto);

    /**
     * Возвращает пользователя по его email.
     *
     * @param email электронная почта пользователя
     * @return {@link User} найденный пользователь
     * @throws UserNotFoundException если пользователь не найден
     */
    User getByEmail(String email) throws UserNotFoundException;

    /**
     * Возвращает список всех пользователей.
     *
     * @return {@code List<User>}, список всех пользователей
     */
    List<User> getAll();

    /**
     * Обновляет данные пользователя.
     *
     * @param dto {@link CreateUserDto}, данные для обновления пользователя
     * @throws UserNotFoundException если пользователь не найден
     */
    void update(UpdateUserDto dto) throws UserNotFoundException;

    /**
     * Удаляет пользователя по его идентификатору.
     *
     * @param id идентификатор пользователя
     */
    void remove(int id);

    /**
     * Меняет статус администратора пользователя.
     *
     * @param dto данные для изменения статуса администратора
     * @throws UserNotFoundException если пользователь не найден
     */
    void changeUserAdminStatus(ChangeAdminStatusDto dto) throws UserNotFoundException;
}

package org.example.core.repositories.user_repository;

import org.example.core.exceptions.UserNotFoundException;
import org.example.core.models.User;
import org.example.core.repositories.user_repository.dtos.ChangeAdminStatusDto;
import org.example.core.repositories.user_repository.dtos.CreateUserDto;
import org.example.core.repositories.user_repository.dtos.UpdateUserDto;

import java.util.List;
import java.util.UUID;

public interface IUserRepository {

    /**
     * —оздаЄт нового пользовател€.
     *
     * @param dto {@link CreateUserDto}, данные дл€ создани€ пользовател€
     * @return созданный пользователь
     */
    User create(CreateUserDto dto);

    /**
     * ¬озвращает пользовател€ по его email.
     *
     * @param email электронна€ почта пользовател€
     * @return {@link User} найденный пользователь
     * @throws UserNotFoundException если пользователь не найден
     */
    User getByEmail(String email) throws UserNotFoundException;

    /**
     * ¬озвращает список всех пользователей.
     *
     * @return {@code List<User>}, список всех пользователей
     */
    List<User> getAll();

    /**
     * ќбновл€ет данные пользовател€.
     *
     * @param dto {@link CreateUserDto}, данные дл€ обновлени€ пользовател€
     * @throws UserNotFoundException если пользователь не найден
     */
    void update(UpdateUserDto dto) throws UserNotFoundException;

    /**
     * ”дал€ет пользовател€ по его идентификатору.
     *
     * @param id идентификатор пользовател€
     */
    void remove(UUID id);

    /**
     * ћен€ет статус администратора пользовател€.
     *
     * @param dto данные дл€ изменени€ статуса администратора
     * @throws UserNotFoundException если пользователь не найден
     */
    void changeUserAdminStatus(ChangeAdminStatusDto dto) throws UserNotFoundException;
}

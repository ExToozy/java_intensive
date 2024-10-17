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
     * ������ ������ ������������.
     *
     * @param dto {@link CreateUserDto}, ������ ��� �������� ������������
     * @return ��������� ������������
     */
    User create(CreateUserDto dto);

    /**
     * ���������� ������������ �� ��� email.
     *
     * @param email ����������� ����� ������������
     * @return {@link User} ��������� ������������
     * @throws UserNotFoundException ���� ������������ �� ������
     */
    User getByEmail(String email) throws UserNotFoundException;

    /**
     * ���������� ������ ���� �������������.
     *
     * @return {@code List<User>}, ������ ���� �������������
     */
    List<User> getAll();

    /**
     * ��������� ������ ������������.
     *
     * @param dto {@link CreateUserDto}, ������ ��� ���������� ������������
     * @throws UserNotFoundException ���� ������������ �� ������
     */
    void update(UpdateUserDto dto) throws UserNotFoundException;

    /**
     * ������� ������������ �� ��� ��������������.
     *
     * @param id ������������� ������������
     */
    void remove(UUID id);

    /**
     * ������ ������ �������������� ������������.
     *
     * @param dto ������ ��� ��������� ������� ��������������
     * @throws UserNotFoundException ���� ������������ �� ������
     */
    void changeUserAdminStatus(ChangeAdminStatusDto dto) throws UserNotFoundException;
}

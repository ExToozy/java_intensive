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
import java.util.UUID;

/**
 * ������ ��� ������ � ��������������.
 * ������������� ������ ��� ��������, ��������, ���������� � ��������� �������������, � ����� ��� �������� ������������� email.
 */
public class UserService {

    private final IUserRepository userRepository;

    private final HabitService habitService;

    /**
     * ����������� {@link UserService}.
     *
     * @param userRepository {@link IUserRepository} ����������� ��� ������ � ��������������
     * @param habitService   {@link HabitService} ������ ��� ������ � ����������
     */
    public UserService(IUserRepository userRepository, HabitService habitService) {
        this.userRepository = userRepository;
        this.habitService = habitService;
    }

    /**
     * ������ ������ ������������.
     *
     * @param dto ������ ��� �������� ������������
     * @return ��������� ������������
     */
    public User create(CreateUserDto dto) {
        return userRepository.create(dto);
    }

    /**
     * ������� ������������ � ��� ��� �������� � ������� � ����������.
     *
     * @param id ������������� ������������
     */
    public void remove(UUID id) {
        userRepository.remove(id);
        habitService.removeAllUserHabitsAndTracks(id);
    }

    /**
     * ���������� ������������ �� ��� email.
     *
     * @param email email ������������
     * @return ������������
     * @throws UserNotFoundException ���� ������������ �� ������
     */
    public User getUserByEmail(String email) throws UserNotFoundException {
        return userRepository.getByEmail(email);
    }

    /**
     * ���������, ���������� �� ������������ � ��������� email.
     *
     * @param email email ��� ��������
     * @return true, ���� ������������ ����������, ����� false
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
     * ���������� ���� �������������.
     *
     * @return ������ ���� �������������
     */
    public List<User> getAll() {
        return userRepository.getAll();
    }

    /**
     * �������� ������ �������������� ������������.
     *
     * @param dto ������ ��� ��������� �������
     * @throws UserNotFoundException ���� ������������ �� ������
     */
    public void changeUserAdminStatus(ChangeAdminStatusDto dto) throws UserNotFoundException {
        userRepository.changeUserAdminStatus(dto);
    }

    /**
     * ��������� ������ ������������.
     *
     * @param dto ������ ��� ���������� ������������
     * @throws UserNotFoundException ���� ������������ �� ������
     * @throws InvalidEmailException ���� email ������������
     */
    public void update(UpdateUserDto dto) throws UserNotFoundException, InvalidEmailException {
        if (RegexUtil.isInvalidEmail(dto.getEmail())) {
            throw new InvalidEmailException();
        }
        userRepository.update(dto);
    }
}

package org.example.core.repositories.habit_repository;


import org.example.core.models.Habit;
import org.example.core.repositories.habit_repository.dtos.CreateHabitDto;
import org.example.core.repositories.habit_repository.dtos.UpdateHabitDto;

import java.util.List;
import java.util.UUID;

/**
 * ��������� ��� ������ � ���������� ��������.
 * ���������� ������ ��� ��������, ���������, ���������� � �������� ��������.
 */
public interface IHabitRepository {

    /**
     * ������ ����� �������� �� ������ ����������� DTO.
     *
     * @param dto ������ {@link CreateHabitDto}, ���������� ���������� ��� �������� ��������
     */
    void create(CreateHabitDto dto);

    /**
     * ���������� ������ ���� �������� ������������ �� ��� ��������������.
     *
     * @param userId ���������� ������������� ������������
     * @return {@code List<Habit>} ������ �������� ������������
     */
    List<Habit> getAllHabitsByUserId(UUID userId);

    /**
     * ��������� ������������ �������� �� ������ ����������� DTO.
     *
     * @param dto ������ {@link UpdateHabitDto}, ���������� ���������� ������ ��������
     */
    void update(UpdateHabitDto dto);

    /**
     * ������� �������� �� � ��������������.
     *
     * @param id ������������� ��������
     */
    void remove(UUID id);
}

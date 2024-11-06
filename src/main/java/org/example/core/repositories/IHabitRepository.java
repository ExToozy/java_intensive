package org.example.core.repositories;


import org.example.core.dtos.habit_dtos.CreateHabitDto;
import org.example.core.dtos.habit_dtos.UpdateHabitDto;
import org.example.core.models.Habit;
import org.example.exceptions.HabitNotFoundException;

import java.util.List;

/**
 * Интерфейс для работы с хранилищем привычек.
 * Определяет методы для создания, получения, обновления и удаления привычек.
 */
public interface IHabitRepository {

    /**
     * Создаёт новую привычку на основе переданного DTO.
     *
     * @param dto объект {@link CreateHabitDto}, содержащий информацию для создания привычки
     */
    void create(CreateHabitDto dto);

    /**
     * Возвращает список всех привычек пользователя по его идентификатору.
     *
     * @param userId уникальный идентификатор пользователя
     * @return {@code List<Habit>} список привычек пользователя
     */
    List<Habit> getAllHabitsByUserId(int userId);

    /**
     * Возвращает привычку пользователя по её идентификатору.
     *
     * @param habitId уникальный идентификатор привычки
     * @return {@link Habit} привычка пользователя
     */
    Habit getHabitById(int habitId) throws HabitNotFoundException;

    /**
     * Обновляет существующую привычку на основе переданного DTO.
     *
     * @param dto объект {@link UpdateHabitDto}, содержащий обновлённые данные привычки
     */
    void update(int habitId, UpdateHabitDto dto);

    /**
     * Удаляет привычку по её идентификатору.
     *
     * @param id идентификатор привычки
     */
    void remove(int id);
}

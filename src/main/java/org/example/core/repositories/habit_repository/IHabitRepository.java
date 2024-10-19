package org.example.core.repositories.habit_repository;


import org.example.core.models.Habit;
import org.example.core.repositories.habit_repository.dtos.CreateHabitDto;
import org.example.core.repositories.habit_repository.dtos.UpdateHabitDto;

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
     * Обновляет существующую привычку на основе переданного DTO.
     *
     * @param dto объект {@link UpdateHabitDto}, содержащий обновлённые данные привычки
     */
    void update(UpdateHabitDto dto);

    /**
     * Удаляет привычку по её идентификатору.
     *
     * @param id идентификатор привычки
     */
    void remove(int id);
}

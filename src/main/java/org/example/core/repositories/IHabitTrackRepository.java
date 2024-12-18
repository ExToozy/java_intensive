package org.example.core.repositories;

import org.example.core.models.HabitTrack;

import java.util.List;

/**
 * Интерфейс для работы с записями отслеживания привычек.
 * Позволяет добавлять, получать и удалять записи о том, когда была выполнена привычка.
 */
public interface IHabitTrackRepository {

    /**
     * Создаёт новую отметку о выполнении для привычки.
     *
     * @param dto данные для создания записи отслеживания привычки
     */
    void create(int dto);

    /**
     * Возвращает список всех отметок о выполнении для указанной привычки.
     *
     * @param habitId идентификатор привычки
     * @return список записей отслеживания для этой привычки
     */
    List<HabitTrack> getHabitTracks(int habitId);


    /**
     * Удаляет все отметки о выполнении для указанной привычки.
     *
     * @param habitId идентификатор привычки
     */
    void removeAllByHabitId(int habitId);

    /**
     * Удаляет отметку о выполнении.
     *
     * @param id идентификатор отметки
     */
    void remove(int id);
}

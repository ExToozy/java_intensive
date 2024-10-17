package org.example.core.repositories.habit_track_repository;

import org.example.core.models.HabitTrack;
import org.example.core.repositories.habit_track_repository.dtos.CreateHabitTrackDto;

import java.util.List;
import java.util.UUID;

/**
 * »нтерфейс дл€ работы с запис€ми отслеживани€ привычек.
 * ѕозвол€ет добавл€ть, получать и удал€ть записи о том, когда была выполнена привычка.
 */
public interface IHabitTrackRepository {

    /**
     * —оздаЄт новую отметку о выполнении дл€ привычки.
     *
     * @param dto данные дл€ создани€ записи отслеживани€ привычки
     */
    void create(CreateHabitTrackDto dto);

    /**
     * ¬озвращает список всех отметок о выполнении дл€ указанной привычки.
     *
     * @param habitId идентификатор привычки
     * @return список записей отслеживани€ дл€ этой привычки
     */
    List<HabitTrack> getHabitTracks(UUID habitId);

    /**
     * ”дал€ет все отметки о выполнении дл€ указанной привычки.
     *
     * @param id идентификатор привычки
     */
    void removeByHabitId(UUID id);
}

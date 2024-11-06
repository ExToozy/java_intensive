package org.example.core.services;

import lombok.RequiredArgsConstructor;
import org.example.core.models.HabitTrack;
import org.example.core.repositories.IHabitTrackRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Сервис для работы с отметками привычек.
 * Позволяет создавать отметки о выполнении привычки и проверять их статус.
 */
@Service
@RequiredArgsConstructor
public class HabitTrackService {

    private final IHabitTrackRepository habitTrackRepository;

    /**
     * Помечает привычку как выполненную, создавая отметку о выполнении.
     *
     * @param habitId идентификатор привычки, которую нужно отметить как выполненную
     */
    public void completeHabit(int habitId) {
        habitTrackRepository.create(habitId);
    }

    /**
     * Возвращает список отметок о выпонении для указанной привычки.
     *
     * @param habitId идентификатор привычки
     * @return список записей отслеживания привычки
     */
    public List<HabitTrack> getHabitTracks(int habitId) {
        return habitTrackRepository.getHabitTracks(habitId);
    }

    /**
     * Удаляет все отметки о выпонении для указанной привычки.
     *
     * @param habitId идентификатор привычки
     */
    public void removeHabitTracks(int habitId) {
        habitTrackRepository.removeAllByHabitId(habitId);
    }

    public void remove(int trackId) {
        habitTrackRepository.remove(trackId);
    }

}


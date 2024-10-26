package org.example.core.services;

import org.example.core.dtos.habit_track_dtos.CreateHabitTrackDto;
import org.example.core.models.Habit;
import org.example.core.models.HabitTrack;
import org.example.core.repositories.IHabitRepository;
import org.example.core.repositories.IHabitTrackRepository;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

/**
 * Сервис для работы с отметками привычек.
 * Позволяет создавать отметки о выполнении привычки и проверять их статус.
 */
public class HabitTrackService {

    private final IHabitTrackRepository habitTrackRepository;
    private final IHabitRepository habitRepository;

    /**
     * Конструктор {@link HabitTrackService}.
     *
     * @param habitTrackRepository {@link IHabitTrackRepository} репозиторий для работы с отметками
     */
    public HabitTrackService(IHabitTrackRepository habitTrackRepository, IHabitRepository habitRepository) {
        this.habitTrackRepository = habitTrackRepository;
        this.habitRepository = habitRepository;
    }

    /**
     * Помечает привычку как выполненную, создавая отметку о выполнении.
     *
     * @param dto {@link CreateHabitTrackDto} данные для создания записи о выполнении привычки
     */
    public void completeHabit(CreateHabitTrackDto dto) {
        habitTrackRepository.create(dto);
    }

    /**
     * Проверяет, выполнена ли привычка.
     * Привычка является выполненой если существует такая отметка,
     * с момента создания которой, прошло меньше дней чем период выполнения привычки
     *
     * @param habit привычка для проверки
     * @return true, если привычка выполнена в указанный период, иначе false
     */
    public boolean isCompleteHabit(Habit habit) {
        List<HabitTrack> tracks = getHabitTracks(habit.getId());
        Period period = habit.getFrequency().toPeriod();
        return tracks.stream()
                .anyMatch(habitTrack -> habitTrack
                        .getCompleteDate()
                        .isAfter(LocalDate.now().minusDays(period.getDays()))
                );
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

    public List<HabitTrack> getUserHabitTracks(int userId) {
        List<HabitTrack> userHabitTracks = new ArrayList<>();
        List<Habit> userHabits = habitRepository.getAllHabitsByUserId(userId);
        userHabits.forEach(habit -> userHabitTracks.addAll(habitTrackRepository.getHabitTracks(habit.getId())));
        return userHabitTracks;
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


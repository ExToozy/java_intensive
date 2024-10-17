package org.example.core.services;

import org.example.core.models.Habit;
import org.example.core.models.HabitTrack;
import org.example.core.repositories.habit_track_repository.IHabitTrackRepository;
import org.example.core.repositories.habit_track_repository.dtos.CreateHabitTrackDto;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.UUID;

/**
 * —ервис дл€ работы с отметками привычек.
 * ѕозвол€ет создавать отметки о выполнении привычки и провер€ть их статус.
 */
public class HabitTrackService {

    private final IHabitTrackRepository habitTrackRepository;

    /**
     *  онструктор {@link HabitTrackService}.
     *
     * @param habitTrackRepository {@link IHabitTrackRepository} репозиторий дл€ работы с отметками
     */
    public HabitTrackService(IHabitTrackRepository habitTrackRepository) {
        this.habitTrackRepository = habitTrackRepository;
    }

    /**
     * ѕомечает привычку как выполненную, создава€ отметку о выполнении.
     *
     * @param dto {@link CreateHabitTrackDto} данные дл€ создани€ записи о выполнении привычки
     */
    public void completeHabit(CreateHabitTrackDto dto) {
        habitTrackRepository.create(dto);
    }

    /**
     * ѕровер€ет, выполнена ли привычка.
     * ѕривычка €вл€етс€ выполненой если существует така€ отметка,
     * с момента создани€ которой, прошло меньше дней чем период выполнени€ привычки
     *
     * @param habit привычка дл€ проверки
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
     * ¬озвращает список отметок о выпонении дл€ указанной привычки.
     *
     * @param habitId идентификатор привычки
     * @return список записей отслеживани€ привычки
     */
    public List<HabitTrack> getHabitTracks(UUID habitId) {
        return habitTrackRepository.getHabitTracks(habitId);
    }

    /**
     * ”дал€ет все отметки о выпонении дл€ указанной привычки.
     *
     * @param habitId идентификатор привычки
     */
    public void removeHabitTracks(UUID habitId) {
        habitTrackRepository.removeByHabitId(habitId);
    }
}


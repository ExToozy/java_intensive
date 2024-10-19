package org.example.core.services;

import org.example.core.models.Habit;
import org.example.core.models.HabitTrack;
import org.example.core.repositories.habit_repository.IHabitRepository;
import org.example.core.repositories.habit_repository.dtos.CreateHabitDto;
import org.example.core.repositories.habit_repository.dtos.UpdateHabitDto;

import java.time.LocalDate;
import java.time.Period;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Сервис для работы с привычками.
 * Предоставляет методы для создания, обновления, получения и удаления привычек, а также для работы с записями отслеживания привычек.
 */
public class HabitService {

    private final IHabitRepository habitRepository;

    private final HabitTrackService habitTrackService;

    /**
     * Конструктор для {@link HabitService}.
     *
     * @param habitRepository   {@link IHabitRepository}, репозиторий для работы с привычками
     * @param habitTrackService {@link HabitTrackService}, сервис для работы с записями отслеживания привычек
     */
    public HabitService(IHabitRepository habitRepository, HabitTrackService habitTrackService) {
        this.habitRepository = habitRepository;
        this.habitTrackService = habitTrackService;
    }

    /**
     * Создаёт новую привычку.
     *
     * @param dto {@link CreateHabitDto}, данные для создания привычки
     */
    public void createHabit(CreateHabitDto dto) {
        habitRepository.create(dto);
    }

    /**
     * Возвращает список привычек пользователя, отфильтрованный по статусу выполнения.
     *
     * @param userId     идентификатор пользователя
     * @param isComplete если true — возвращает выполненные привычки, иначе — невыполненные
     * @return {@code List<Habit>} список привычек пользователя
     */
    public List<Habit> getUserHabitsByCompleteStatus(int userId, boolean isComplete) {
        if (isComplete) {
            return habitRepository.getAllHabitsByUserId(userId).stream().filter(this::isCompleteHabit).toList();
        } else {
            return habitRepository.getAllHabitsByUserId(userId).stream().filter(habit -> !isCompleteHabit(habit)).toList();
        }
    }

    /**
     * Проверяет, выполнена ли привычка.
     *
     * @param habit привычка для проверки
     * @return true, если привычка выполнена, иначе false
     */
    public boolean isCompleteHabit(Habit habit) {
        List<HabitTrack> tracks = habitTrackService.getHabitTracks(habit.getId());
        Period period = habit.getFrequency().toPeriod();
        return tracks.stream()
                .anyMatch(habitTrack -> habitTrack.getCompleteDate().isAfter(LocalDate.now().minusDays(period.getDays())));
    }

    /**
     * Возвращает количество выполнений привычки за указанный период.
     *
     * @param habit  привычка
     * @param period период для подсчёта
     * @return количество выполнений привычки за указанный период
     */
    public int getHabitExecutionCountByPeriod(Habit habit, Period period) {
        List<HabitTrack> tracks = habitTrackService.getHabitTracks(habit.getId());
        int executionsCount = 0;
        for (var track : tracks) {
            if (track.getCompleteDate().isAfter(LocalDate.now().minusDays(period.getDays()))) {
                executionsCount++;
            }
        }
        return executionsCount;
    }

    /**
     * Возвращает статистику по привычкам пользователя.
     * Статистика включает в себя:
     * track_count - общее количество выполнений,
     * completion_percent - процент успешного выполнения привычки с момента её создания,
     * current_streak - текущая серия выполнений
     *
     * @param userId идентификатор пользователя
     * @return словарь, где ключ — привычка {@link Habit}, а значение — статистика по привычке {@code Map<String, Integer>}
     */
    public Map<Habit, Map<String, Integer>> getHabitStatistics(int userId) {
        Map<Habit, Map<String, Integer>> habitStatistics = new HashMap<>();
        List<Habit> habits = habitRepository.getAllHabitsByUserId(userId);
        for (var habit : habits) {
            List<HabitTrack> tracks = habitTrackService.getHabitTracks(habit.getId());
            Map<String, Integer> statistic = new HashMap<>();

            int trackCount = tracks.size();
            int daysSinceCreationHabit = habit.getDayOfCreation().until(LocalDate.now()).getDays();
            int maxTrackCount = (daysSinceCreationHabit / habit.getFrequency().toPeriod().getDays()) + 1;

            statistic.put("track_count", tracks.size());
            statistic.put("completion_percent", (trackCount * 100) / maxTrackCount);
            statistic.put("current_streak", getHabitStreak(habit));

            habitStatistics.put(habit, statistic);
        }
        return habitStatistics;
    }

    /**
     * Возвращает текущую серию выполнений привычки (стрик).
     * Серией выполнений считается, если привычка была выполнена n раз,
     * при этом между этими выполнениями, нет периода большего, чем частота выполнения привычки
     *
     * @param habit привычка
     * @return текущую серию выполнений
     */
    public int getHabitStreak(Habit habit) {
        if (!isCompleteHabit(habit)) {
            return 0;
        }
        List<HabitTrack> tracks = habitTrackService.getHabitTracks(habit.getId());
        tracks.sort((o1, o2) -> o2.getCompleteDate().compareTo(o1.getCompleteDate()));
        int streak = 1;
        for (int i = 0; i < tracks.size() - 1; i++) {
            int habitFrequencyInDays = habit.getFrequency().toPeriod().getDays();
            boolean isCompleteHabitInTime = tracks.get(i)
                    .getCompleteDate()
                    .isBefore(tracks.get(i + 1).getCompleteDate().plusDays(habitFrequencyInDays));
            isCompleteHabitInTime = isCompleteHabitInTime || tracks.get(i)
                    .getCompleteDate()
                    .isEqual(tracks.get(i + 1).getCompleteDate().plusDays(habitFrequencyInDays));
            if (isCompleteHabitInTime) {
                streak++;
            } else {
                break;
            }
        }
        return streak;
    }

    /**
     * Обновляет привычку.
     *
     * @param dto {@link UpdateHabitDto} данные для обновления привычки
     */
    public void updateHabit(UpdateHabitDto dto) {
        habitRepository.update(dto);
    }

    /**
     * Возвращает дату до которой привычку нужно выполнить.
     *
     * @param habit {@link Habit} привычка
     * @return дату до которой привычку нужно выполнить
     */
    public LocalDate getHabitDeadlineDay(Habit habit) {
        List<HabitTrack> tracks = habitTrackService.getHabitTracks(habit.getId());
        LocalDate lastCompleteDay;
        if (tracks.isEmpty()) {
            lastCompleteDay = habit.getDayOfCreation();
        } else {
            lastCompleteDay = LocalDate.EPOCH;
            for (var track : tracks) {
                if (lastCompleteDay.isBefore(track.getCompleteDate())) {
                    lastCompleteDay = track.getCompleteDate();
                }
            }
        }
        return lastCompleteDay.plusDays(habit.getFrequency().toPeriod().getDays());
    }

    /**
     * Удаляет все привычки и отмеки о выполнении пользователя.
     *
     * @param id идентификатор пользователя
     */
    public void removeAllUserHabitsAndTracks(int id) {
        getUserHabits(id).forEach(habit -> removeHabitAndTracks(habit.getId()));
    }

    /**
     * Возвращает все привычки пользователя.
     *
     * @param userId идентификатор пользователя
     * @return список привычек
     */
    public List<Habit> getUserHabits(int userId) {
        return habitRepository.getAllHabitsByUserId(userId);
    }

    /**
     * Удаляет привычку и все её отметки.
     *
     * @param habitId идентификатор привычки
     */
    public void removeHabitAndTracks(int habitId) {
        habitTrackService.removeHabitTracks(habitId);
        habitRepository.remove(habitId);
    }
}

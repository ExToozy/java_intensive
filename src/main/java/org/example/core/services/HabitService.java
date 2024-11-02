package org.example.core.services;

import lombok.RequiredArgsConstructor;
import org.example.core.dtos.habit_dtos.CreateHabitDto;
import org.example.core.dtos.habit_dtos.UpdateHabitDto;
import org.example.core.models.Habit;
import org.example.core.models.HabitTrack;
import org.example.core.repositories.IHabitRepository;
import org.example.exceptions.HabitNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Сервис для работы с привычками.
 * Предоставляет методы для создания, обновления, получения и удаления привычек, а также для работы с записями отслеживания привычек.
 */
@Service
@RequiredArgsConstructor
public class HabitService {

    private final IHabitRepository habitRepository;

    private final HabitTrackService habitTrackService;

    private final UserService userService;


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
     * @param userId  идентификатор пользователя
     * @param habitId идентификатор привычки для проверки
     * @return true, если привычка выполнена, иначе false
     */
    public boolean isCompleteUserHabit(int userId, int habitId) throws HabitNotFoundException {
        Habit habit = getUserHabit(userId, habitId);
        return isCompleteHabit(habit);
    }

    private boolean isCompleteHabit(Habit habit) {
        List<HabitTrack> tracks = habitTrackService.getHabitTracks(habit.getId());
        Period period = habit.getFrequency().getPeriod();
        return tracks.stream()
                .anyMatch(habitTrack -> habitTrack.getCompleteDate().isAfter(LocalDate.now().minusDays(period.getDays())));
    }

    /**
     * Возвращает статистику по всем привычкам пользователя.
     *
     * @param userId идентификатор пользователя
     * @return список {@code List<Map<String, Object>>} статистик по каждой привычке
     */
    public List<Map<String, Object>> getStatisticsOfAllUserHabits(int userId) {
        List<Map<String, Object>> habitStatistics = new ArrayList<>();
        List<Habit> habits = habitRepository.getAllHabitsByUserId(userId);
        habits.forEach(habit -> habitStatistics.add(getHabitStats(habit)));
        return habitStatistics;
    }

    /**
     * Возвращает статистику по привычке пользователя.
     *
     * @param userId идентификатор пользователя
     * @return список {@code List<Map<String, Object>>} статистик по каждой привычке
     * @throws HabitNotFoundException если пользователь не администратор, но пытается получить статистику не для своей привычки
     */
    public Map<String, Object> getStatisticsOfOneUserHabit(int userId, int habitId) throws HabitNotFoundException {
        Habit habit = getUserHabit(userId, habitId);
        return getHabitStats(habit);
    }

    /**
     * Возвращает статистику по привычке.
     * Статистика включает в себя:
     * track_count - общее количество выполнений,
     * completion_percent - процент успешного выполнения привычки с момента её создания,
     * current_streak - текущая серия выполнений
     *
     * @param habit {@link Habit} идентификатор пользователя
     * @return словарь {@code Map<String, Object>} статистика по привычке
     */
    private Map<String, Object> getHabitStats(Habit habit) {
        List<HabitTrack> tracks = habitTrackService.getHabitTracks(habit.getId());
        Map<String, Object> statistic = new HashMap<>();

        int trackCount = tracks.size();
        int daysSinceCreationHabit = habit.getDayOfCreation().until(LocalDate.now()).getDays();
        int maxTrackCount = (daysSinceCreationHabit / habit.getFrequency().getPeriod().getDays()) + 1;

        statistic.put("habit_id", habit.getId());
        statistic.put("completion_status", isCompleteHabit(habit));
        statistic.put("track_count", tracks.size());
        statistic.put("completion_percent", (trackCount * 100) / maxTrackCount);
        statistic.put("current_streak", getHabitStreak(habit));
        return statistic;
    }

    /**
     * Возвращает текущую серию выполнений привычки (стрик).
     * Серией выполнений считается, если привычка была выполнена n раз,
     * при этом между этими выполнениями, нет периода большего, чем частота выполнения привычки
     *
     * @param habit привычка
     * @return текущую серию выполнений
     */
    private int getHabitStreak(Habit habit) {
        if (!isCompleteHabit(habit)) {
            return 0;
        }
        List<HabitTrack> tracks = habitTrackService.getHabitTracks(habit.getId());
        tracks.sort((o1, o2) -> o2.getCompleteDate().compareTo(o1.getCompleteDate()));
        int streak = 1;
        for (int i = 0; i < tracks.size() - 1; i++) {
            int habitFrequencyInDays = habit.getFrequency().getPeriod().getDays();
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
    public void updateUserHabit(int userId, int habitId, UpdateHabitDto dto) throws HabitNotFoundException {
        if (isUserHabitOrUserIsAdmin(userId, habitId)) {
            habitRepository.update(habitId, dto);
        } else {
            throw new HabitNotFoundException();
        }
    }

    /**
     * Возвращает дату до которой привычку нужно выполнить.
     *
     * @param habitId идентификатор привычки
     * @return дату до которой привычку нужно выполнить
     */
    public LocalDate getHabitDeadlineDay(int userId, int habitId) throws HabitNotFoundException {
        Habit habit = getUserHabit(userId, habitId);
        List<HabitTrack> tracks = habitTrackService.getHabitTracks(habitId);
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
        return lastCompleteDay.plusDays(habit.getFrequency().getPeriod().getDays());
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


    public Habit getUserHabit(int userId, int habitId) throws HabitNotFoundException {
        if (isUserHabitOrUserIsAdmin(userId, habitId)) {
            return habitRepository.getHabitById(habitId);
        }
        throw new HabitNotFoundException();
    }

    /**
     * Удаляет привычку и все её отметки.
     *
     * @param habitId идентификатор привычки
     */
    public void removeUserHabit(int userId, int habitId) throws HabitNotFoundException {
        if (isUserHabitOrUserIsAdmin(userId, habitId)) {
            habitRepository.remove(habitId);
        } else {
            throw new HabitNotFoundException();
        }
    }

    public boolean isUserHabitOrUserIsAdmin(int userId, int habitId) {
        if (userService.isUserAdmin(userId)) {
            return true;
        }
        List<Habit> userHabits = habitRepository.getAllHabitsByUserId(userId);
        return userHabits.stream().anyMatch(habit -> habit.getId() == habitId);
    }

    public boolean isUserHabitTrackOrUserIsAdmin(int userId, int trackId) {
        if (userService.isUserAdmin(userId)) {
            return true;
        }
        List<Habit> userHabits = habitRepository.getAllHabitsByUserId(userId);
        return userHabits
                .stream()
                .anyMatch(habit -> habitTrackService.getHabitTracks(habit.getId())
                        .stream()
                        .anyMatch(habitTrack -> habitTrack.getId() == trackId));
    }
}

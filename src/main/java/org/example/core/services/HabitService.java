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
import java.util.UUID;

/**
 * ������ ��� ������ � ����������.
 * ������������� ������ ��� ��������, ����������, ��������� � �������� ��������, � ����� ��� ������ � �������� ������������ ��������.
 */
public class HabitService {

    private final IHabitRepository habitRepository;

    private final HabitTrackService habitTrackService;

    /**
     * ����������� ��� {@link HabitService}.
     *
     * @param habitRepository   {@link IHabitRepository}, ����������� ��� ������ � ����������
     * @param habitTrackService {@link HabitTrackService}, ������ ��� ������ � �������� ������������ ��������
     */
    public HabitService(IHabitRepository habitRepository, HabitTrackService habitTrackService) {
        this.habitRepository = habitRepository;
        this.habitTrackService = habitTrackService;
    }

    /**
     * ������ ����� ��������.
     *
     * @param dto {@link CreateHabitDto}, ������ ��� �������� ��������
     */
    public void createHabit(CreateHabitDto dto) {
        habitRepository.create(dto);
    }

    /**
     * ���������� ������ �������� ������������, ��������������� �� ������� ����������.
     *
     * @param userId     ������������� ������������
     * @param isComplete ���� true � ���������� ����������� ��������, ����� � �������������
     * @return {@code List<Habit>} ������ �������� ������������
     */
    public List<Habit> getUserHabitsByCompleteStatus(UUID userId, boolean isComplete) {
        if (isComplete) {
            return habitRepository.getAllHabitsByUserId(userId).stream().filter(this::isCompleteHabit).toList();
        } else {
            return habitRepository.getAllHabitsByUserId(userId).stream().filter(habit -> !isCompleteHabit(habit)).toList();
        }
    }

    /**
     * ���������, ��������� �� ��������.
     *
     * @param habit �������� ��� ��������
     * @return true, ���� �������� ���������, ����� false
     */
    public boolean isCompleteHabit(Habit habit) {
        List<HabitTrack> tracks = habitTrackService.getHabitTracks(habit.getId());
        Period period = habit.getFrequency().toPeriod();
        return tracks.stream()
                .anyMatch(habitTrack -> habitTrack.getCompleteDate().isAfter(LocalDate.now().minusDays(period.getDays())));
    }

    /**
     * ���������� ���������� ���������� �������� �� ��������� ������.
     *
     * @param habit  ��������
     * @param period ������ ��� ��������
     * @return ���������� ���������� �������� �� ��������� ������
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
     * ���������� ���������� �� ��������� ������������.
     * ���������� �������� � ����:
     * track_count - ����� ���������� ����������,
     * completion_percent - ������� ��������� ���������� �������� � ������� � ��������,
     * current_streak - ������� ����� ����������
     *
     * @param userId ������������� ������������
     * @return �������, ��� ���� � �������� {@link Habit}, � �������� � ���������� �� �������� {@code Map<String, Integer>}
     */
    public Map<Habit, Map<String, Integer>> getHabitStatistics(UUID userId) {
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
     * ���������� ������� ����� ���������� �������� (�����).
     * ������ ���������� ���������, ���� �������� ���� ��������� n ���,
     * ��� ���� ����� ����� ������������, ��� ������� ��������, ��� ������� ���������� ��������
     *
     * @param habit ��������
     * @return ������� ����� ����������
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
     * ��������� ��������.
     *
     * @param dto {@link UpdateHabitDto} ������ ��� ���������� ��������
     */
    public void updateHabit(UpdateHabitDto dto) {
        habitRepository.update(dto);
    }

    /**
     * ���������� ���� �� ������� �������� ����� ���������.
     *
     * @param habit {@link Habit} ��������
     * @return ���� �� ������� �������� ����� ���������
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
     * ������� ��� �������� � ������ � ���������� ������������.
     *
     * @param id ������������� ������������
     */
    public void removeAllUserHabitsAndTracks(UUID id) {
        getUserHabits(id).forEach(habit -> removeHabitAndTracks(habit.getId()));
    }

    /**
     * ���������� ��� �������� ������������.
     *
     * @param userId ������������� ������������
     * @return ������ ��������
     */
    public List<Habit> getUserHabits(UUID userId) {
        return habitRepository.getAllHabitsByUserId(userId);
    }

    /**
     * ������� �������� � ��� � �������.
     *
     * @param habitId ������������� ��������
     */
    public void removeHabitAndTracks(UUID habitId) {
        habitRepository.remove(habitId);
        habitTrackService.removeHabitTracks(habitId);
    }
}

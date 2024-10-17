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
 * ������ ��� ������ � ��������� ��������.
 * ��������� ��������� ������� � ���������� �������� � ��������� �� ������.
 */
public class HabitTrackService {

    private final IHabitTrackRepository habitTrackRepository;

    /**
     * ����������� {@link HabitTrackService}.
     *
     * @param habitTrackRepository {@link IHabitTrackRepository} ����������� ��� ������ � ���������
     */
    public HabitTrackService(IHabitTrackRepository habitTrackRepository) {
        this.habitTrackRepository = habitTrackRepository;
    }

    /**
     * �������� �������� ��� �����������, �������� ������� � ����������.
     *
     * @param dto {@link CreateHabitTrackDto} ������ ��� �������� ������ � ���������� ��������
     */
    public void completeHabit(CreateHabitTrackDto dto) {
        habitTrackRepository.create(dto);
    }

    /**
     * ���������, ��������� �� ��������.
     * �������� �������� ���������� ���� ���������� ����� �������,
     * � ������� �������� �������, ������ ������ ���� ��� ������ ���������� ��������
     *
     * @param habit �������� ��� ��������
     * @return true, ���� �������� ��������� � ��������� ������, ����� false
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
     * ���������� ������ ������� � ��������� ��� ��������� ��������.
     *
     * @param habitId ������������� ��������
     * @return ������ ������� ������������ ��������
     */
    public List<HabitTrack> getHabitTracks(UUID habitId) {
        return habitTrackRepository.getHabitTracks(habitId);
    }

    /**
     * ������� ��� ������� � ��������� ��� ��������� ��������.
     *
     * @param habitId ������������� ��������
     */
    public void removeHabitTracks(UUID habitId) {
        habitTrackRepository.removeByHabitId(habitId);
    }
}


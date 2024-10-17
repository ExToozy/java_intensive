package org.example.core.repositories.habit_track_repository;

import org.example.core.models.HabitTrack;
import org.example.core.repositories.habit_track_repository.dtos.CreateHabitTrackDto;

import java.util.List;
import java.util.UUID;

/**
 * ��������� ��� ������ � �������� ������������ ��������.
 * ��������� ���������, �������� � ������� ������ � ���, ����� ���� ��������� ��������.
 */
public interface IHabitTrackRepository {

    /**
     * ������ ����� ������� � ���������� ��� ��������.
     *
     * @param dto ������ ��� �������� ������ ������������ ��������
     */
    void create(CreateHabitTrackDto dto);

    /**
     * ���������� ������ ���� ������� � ���������� ��� ��������� ��������.
     *
     * @param habitId ������������� ��������
     * @return ������ ������� ������������ ��� ���� ��������
     */
    List<HabitTrack> getHabitTracks(UUID habitId);

    /**
     * ������� ��� ������� � ���������� ��� ��������� ��������.
     *
     * @param id ������������� ��������
     */
    void removeByHabitId(UUID id);
}

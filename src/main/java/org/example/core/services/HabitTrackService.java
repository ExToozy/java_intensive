package org.example.core.services;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.example.core.models.Habit;
import org.example.core.models.HabitTrack;
import org.example.core.repositories.IHabitRepository;
import org.example.core.repositories.IHabitTrackRepository;
import org.example.exceptions.HabitNotFoundException;
import org.example.exceptions.HabitTrackNotFoundException;
import org.example.exceptions.InvalidTokenException;
import org.example.infrastructure.util.JwtProvider;
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
    private final IHabitRepository habitRepository;
    private final JwtProvider jwtProvider;

    /**
     * Помечает привычку как выполненную, создавая отметку о выполнении.
     *
     * @param token
     * @param habitId идентификатор привычки, которую нужно отметить как выполненную
     */
    public void completeHabit(String token, int habitId) throws InvalidTokenException, HabitNotFoundException {
        if (isUserHabitOrUserIsAdmin(token, habitId)) {
            habitTrackRepository.create(habitId);
        } else {
            throw new HabitNotFoundException();
        }
    }

    /**
     * Возвращает список отметок о выпонении для указанной привычки.
     *
     * @param token
     * @param habitId идентификатор привычки
     * @return список записей отслеживания привычки
     */
    public List<HabitTrack> getHabitTracks(String token, int habitId) throws HabitNotFoundException, InvalidTokenException {
        if (isUserHabitOrUserIsAdmin(token, habitId)) {
            return habitTrackRepository.getHabitTracks(habitId);
        } else {
            throw new HabitNotFoundException();
        }
    }

    /**
     * Удаляет все отметки о выпонении для указанной привычки.
     *
     * @param token   jwt токен пользователя
     * @param habitId идентификатор привычки
     */
    public void removeUserHabitTracks(String token, int habitId) throws InvalidTokenException, HabitNotFoundException {
        if (isUserHabitOrUserIsAdmin(token, habitId)) {
            habitTrackRepository.removeAllByHabitId(habitId);
        } else {
            throw new HabitNotFoundException();
        }
    }

    private boolean isUserHabitOrUserIsAdmin(String token, int habitId) throws InvalidTokenException {
        if (isUserAdmin(token)) {
            return true;
        }
        int userId = jwtProvider.getUserIdFromToken(token);
        List<Habit> userHabits = habitRepository.getAllHabitsByUserId(userId);
        return userHabits.stream().anyMatch(habit -> habit.getId() == habitId);
    }

    private boolean isUserHabitTrackOrUserIsAdmin(String token, int trackId) throws InvalidTokenException {
        if (isUserAdmin(token)) {
            return true;
        }
        int userId = jwtProvider.getUserIdFromToken(token);
        List<Habit> userHabits = habitRepository.getAllHabitsByUserId(userId);
        try {
            for (Habit habit : userHabits) {
                for (HabitTrack habitTrack : getHabitTracks(token, habit.getId())) {
                    if (habitTrack.getId() == trackId) {
                        return true;
                    }
                }
            }
        } catch (HabitNotFoundException e) {
            return false;
        }
        return false;
    }

    private boolean isUserAdmin(String token) {
        Claims claims = jwtProvider.getClaims(token).orElse(null);
        if (claims == null) {
            return false;
        }
        return claims.get("is_admin", Boolean.class);
    }

    public void remove(String token, int trackId) throws HabitTrackNotFoundException, InvalidTokenException {
        if (isUserHabitTrackOrUserIsAdmin(token, trackId)) {
            habitTrackRepository.remove(trackId);
        } else {
            throw new HabitTrackNotFoundException();
        }
    }

}


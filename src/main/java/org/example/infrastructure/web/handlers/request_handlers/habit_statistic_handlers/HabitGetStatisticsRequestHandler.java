package org.example.infrastructure.web.handlers.request_handlers.habit_statistic_handlers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.annotations.Auditable;
import org.example.core.models.Habit;
import org.example.infrastructure.data.mappers.HabitMapper;
import org.example.infrastructure.util.JsonResponse;
import org.example.infrastructure.util.ServiceHelper;
import org.example.infrastructure.util.TokenHelper;
import org.example.infrastructure.web.handlers.request_handlers.HttpServletRequestHandler;

import java.util.Map;

/**
 * Обработчик запросов на получение статистики привычек.
 * Реализует интерфейс {@link HttpServletRequestHandler} и обрабатывает
 * запросы для получения статистики привычек пользователя, идентификатор которого
 * извлекается из токена авторизации.
 */
@Auditable
public class HabitGetStatisticsRequestHandler implements HttpServletRequestHandler {
    public final static HabitGetStatisticsRequestHandler INSTANCE = new HabitGetStatisticsRequestHandler();

    private HabitGetStatisticsRequestHandler() {
    }

    /**
     * Обрабатывает HTTP-запрос на получение статистики привычек.
     * Извлекает идентификатор пользователя из токена авторизации и получает
     * статистику привычек, связанную с пользователем.
     *
     * @param req объект {@link HttpServletRequest}, содержащий запрос на получение статистики привычек.
     * @return объект {@link JsonResponse}, содержащий статус операции и данные статистики привычек.
     * @throws Exception если при обработке запроса возникнет ошибка.
     */
    @Override
    public JsonResponse handleRequest(HttpServletRequest req) throws Exception {
        var jsonResponse = new JsonResponse();

        int userId = TokenHelper.getUserIdFromToken(req.getHeader("Authorization"));

        Map<Habit, Map<String, Integer>> habitStatistics = ServiceHelper.getHabitServiceInstance().getHabitStatistics(userId);
        if (habitStatistics.isEmpty()) {
            jsonResponse.setStatusCode(HttpServletResponse.SC_NO_CONTENT);
        }
        jsonResponse.putDataEntry("habitStatistics", HabitMapper.INSTANCE.toHabitStatisticsMapDto(habitStatistics));

        return jsonResponse;
    }
}
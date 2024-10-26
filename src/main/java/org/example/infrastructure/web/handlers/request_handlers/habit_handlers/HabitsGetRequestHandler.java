package org.example.infrastructure.web.handlers.request_handlers.habit_handlers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.core.models.Habit;
import org.example.infrastructure.util.JsonResponse;
import org.example.infrastructure.util.ServiceHelper;
import org.example.infrastructure.util.TokenHelper;
import org.example.infrastructure.web.handlers.request_handlers.HttpServletRequestHandler;

import java.io.IOException;
import java.util.List;

/**
 * Обработчик запросов на получение привычек пользователя.
 * Реализует интерфейс {@link HttpServletRequestHandler} и отвечает за
 * получение списка привычек для текущего пользователя.
 */
public class HabitsGetRequestHandler implements HttpServletRequestHandler {

    public static final HabitsGetRequestHandler INSTANCE = new HabitsGetRequestHandler();

    private HabitsGetRequestHandler() {
    }

    /**
     * Обрабатывает HTTP-запрос на получение привычек пользователя.
     *
     * @param req объект {@link HttpServletRequest}, содержащий запрос на получение привычек.
     * @return объект {@link JsonResponse}, содержащий список привычек или статус ответа.
     * @throws IOException если при обработке запроса возникнет ошибка ввода-вывода.
     */
    public JsonResponse handleRequest(HttpServletRequest req) throws IOException {
        int userId = TokenHelper.getUserIdFromToken(req.getHeader("Authorization"));
        var jsonResponse = new JsonResponse();

        List<Habit> userHabits = ServiceHelper.getHabitServiceInstance().getUserHabits(userId);
        if (userHabits.isEmpty()) {
            jsonResponse.setStatusCode(HttpServletResponse.SC_NO_CONTENT);
        }
        jsonResponse.putDataEntry("habits", userHabits);

        return jsonResponse;
    }
}

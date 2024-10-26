package org.example.infrastructure.web.handlers.request_handlers.habit_handlers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.infrastructure.constants.ErrorMessageConstants;
import org.example.infrastructure.data.validators.ValidationResult;
import org.example.infrastructure.data.validators.json_validators.JsonHabitValidator;
import org.example.infrastructure.util.JsonResponse;
import org.example.infrastructure.util.ServiceHelper;
import org.example.infrastructure.util.ServletRequestHelper;
import org.example.infrastructure.util.TokenHelper;
import org.example.infrastructure.web.handlers.request_handlers.HttpServletRequestHandler;

import java.util.Map;


/**
 * Обработчик запросов на удаление привычки.
 * Реализует интерфейс {@link HttpServletRequestHandler} и отвечает за
 * удаление привычки пользователя.
 */
public class HabitDeleteRequestHandler implements HttpServletRequestHandler {
    public static final HabitDeleteRequestHandler INSTANCE = new HabitDeleteRequestHandler();

    private HabitDeleteRequestHandler() {
    }

    /**
     * Обрабатывает HTTP-запрос на удаление привычки.
     *
     * @param req объект {@link HttpServletRequest}, содержащий запрос на удаление привычки.
     * @return объект {@link JsonResponse}, содержащий статус удаления.
     * @throws Exception если возникла ошибка при обработке запроса.
     */
    public JsonResponse handleRequest(HttpServletRequest req) throws Exception {
        Map<String, Object> jsonMap = ServletRequestHelper.getJsonMap(req);
        var jsonResponse = new JsonResponse();
        ValidationResult validationResult = JsonHabitValidator.validateDeleteHabitJson(jsonMap);

        if (validationResult.isValid()) {
            int userId = TokenHelper.getUserIdFromToken(req.getHeader("Authorization"));
            int habitId = (int) jsonMap.get("habitId");

            if (ServiceHelper.getHabitServiceInstance().isUserHabit(userId, habitId)) {
                ServiceHelper.getHabitServiceInstance().removeHabitAndTracks(habitId);
                jsonResponse.setStatusCode(HttpServletResponse.SC_NO_CONTENT);

            } else {
                jsonResponse.addError(String.format(ErrorMessageConstants.HABIT_NOT_FOUND_FORMAT, habitId));
                jsonResponse.setStatusCode(HttpServletResponse.SC_BAD_REQUEST);
            }
        } else {
            jsonResponse.setStatusCode(HttpServletResponse.SC_BAD_REQUEST);
            jsonResponse.addAllErrors(validationResult.errors());
        }
        return jsonResponse;
    }
}

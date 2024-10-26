package org.example.infrastructure.web.handlers.request_handlers.habit_track_handlers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.infrastructure.constants.ErrorMessageConstants;
import org.example.infrastructure.data.validators.ValidationResult;
import org.example.infrastructure.data.validators.json_validators.JsonHabitTrackValidator;
import org.example.infrastructure.util.JsonResponse;
import org.example.infrastructure.util.ServiceHelper;
import org.example.infrastructure.util.ServletRequestHelper;
import org.example.infrastructure.util.TokenHelper;
import org.example.infrastructure.web.handlers.request_handlers.HttpServletRequestHandler;

import java.util.Map;

/**
 * Обработчик запроса на удаление отслеживаемой привычки.
 * Реализует интерфейс {@link HttpServletRequestHandler} и обрабатывает
 * запросы для удаления отслеживаемых привычек, связанных с пользователем,
 * идентификатор которого извлекается из токена авторизации.
 */
public class HabitTrackDeleteRequestHandler implements HttpServletRequestHandler {
    public final static HabitTrackDeleteRequestHandler INSTANCE = new HabitTrackDeleteRequestHandler();

    private HabitTrackDeleteRequestHandler() {
    }

    /**
     * Обрабатывает HTTP-запрос на удаление отслеживаемой привычки.
     * Извлекает идентификатор пользователя из токена авторизации и идентификатор привычки из запроса,
     * затем валидирует входные данные перед попыткой удаления привычки.
     *
     * @param req объект {@link HttpServletRequest}, содержащий запрос на удаление отслеживаемой привычки.
     * @return объект {@link JsonResponse}, содержащий статус операции и возможные ошибки.
     * @throws Exception если при обработке запроса возникнет ошибка.
     */
    @Override
    public JsonResponse handleRequest(HttpServletRequest req) throws Exception {
        Map<String, Object> jsonMap = ServletRequestHelper.getJsonMap(req);
        var jsonResponse = new JsonResponse();
        ValidationResult validationResult = JsonHabitTrackValidator.validateDeleteHabitTrackJson(jsonMap);

        if (validationResult.isValid()) {
            int userId = TokenHelper.getUserIdFromToken(req.getHeader("Authorization"));
            int trackId = (int) jsonMap.get("trackId");

            if (ServiceHelper.getHabitServiceInstance().isUserHabitTrack(userId, trackId)) {
                ServiceHelper.getHabitTrackServiceInstance().remove(trackId);
                jsonResponse.setStatusCode(HttpServletResponse.SC_NO_CONTENT);

            } else {
                jsonResponse.addError(String.format(ErrorMessageConstants.HABIT_TRACK_NOT_FOUND_FORMAT, trackId));
                jsonResponse.setStatusCode(HttpServletResponse.SC_BAD_REQUEST);
            }
        } else {
            jsonResponse.setStatusCode(HttpServletResponse.SC_BAD_REQUEST);
            jsonResponse.addAllErrors(validationResult.errors());
        }
        return jsonResponse;
    }
}

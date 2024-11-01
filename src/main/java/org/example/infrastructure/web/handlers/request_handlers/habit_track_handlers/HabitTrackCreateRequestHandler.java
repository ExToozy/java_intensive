package org.example.infrastructure.web.handlers.request_handlers.habit_track_handlers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.annotations.Auditable;
import org.example.core.dtos.habit_track_dtos.CreateHabitTrackDto;
import org.example.infrastructure.constants.ErrorMessageConstants;
import org.example.infrastructure.data.mappers.HabitTrackMapper;
import org.example.infrastructure.data.validators.ValidationResult;
import org.example.infrastructure.data.validators.json_validators.JsonHabitTrackValidator;
import org.example.infrastructure.util.JsonResponse;
import org.example.infrastructure.util.ServiceHelper;
import org.example.infrastructure.util.ServletRequestHelper;
import org.example.infrastructure.util.TokenHelper;
import org.example.infrastructure.web.handlers.request_handlers.HttpServletRequestHandler;

import java.util.Map;

/**
 * Обработчик запроса на создание отслеживаемой привычки.
 * Реализует интерфейс {@link HttpServletRequestHandler} и обрабатывает
 * запросы для создания отслеживаемых привычек, связанных с пользователем,
 * идентификатор которого извлекается из токена авторизации.
 */
@Auditable
public class HabitTrackCreateRequestHandler implements HttpServletRequestHandler {
    public final static HabitTrackCreateRequestHandler INSTANCE = new HabitTrackCreateRequestHandler();

    private HabitTrackCreateRequestHandler() {
    }

    /**
     * Обрабатывает HTTP-запрос на создание отслеживаемой привычки.
     * Извлекает идентификатор пользователя из токена авторизации и создает
     * новую отслеживаемую привычку на основе предоставленных данных.
     *
     * @param req объект {@link HttpServletRequest}, содержащий запрос на создание отслеживаемой привычки.
     * @return объект {@link JsonResponse}, содержащий статус операции и возможные ошибки.
     * @throws Exception если при обработке запроса возникнет ошибка.
     */
    @Override
    public JsonResponse handleRequest(HttpServletRequest req) throws Exception {
        Map<String, Object> jsonMap = ServletRequestHelper.getJsonMap(req);
        var jsonResponse = new JsonResponse();
        ValidationResult validationResult = JsonHabitTrackValidator.validateCreateHabitTrackJson(jsonMap);

        if (validationResult.isValid()) {
            int userId = TokenHelper.getUserIdFromToken(req.getHeader("Authorization"));

            CreateHabitTrackDto dto = HabitTrackMapper.INSTANCE.toCreateHabitTrackDto(jsonMap);

            if (ServiceHelper.getHabitServiceInstance().isUserHabit(userId, dto.getHabitId())) {
                ServiceHelper.getHabitTrackServiceInstance().completeHabit(dto);
                jsonResponse.setStatusCode(HttpServletResponse.SC_CREATED);
            } else {
                jsonResponse.addError(String.format(ErrorMessageConstants.HABIT_NOT_FOUND_FORMAT, dto.getHabitId()));
                jsonResponse.setStatusCode(HttpServletResponse.SC_BAD_REQUEST);
            }

        } else {
            jsonResponse.setStatusCode(HttpServletResponse.SC_BAD_REQUEST);
            jsonResponse.addAllErrors(validationResult.errors());
        }
        return jsonResponse;
    }
}

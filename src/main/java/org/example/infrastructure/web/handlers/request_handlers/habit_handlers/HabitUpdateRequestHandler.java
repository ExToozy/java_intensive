package org.example.infrastructure.web.handlers.request_handlers.habit_handlers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.annotations.Auditable;
import org.example.core.dtos.habit_dtos.UpdateHabitDto;
import org.example.infrastructure.constants.ErrorMessageConstants;
import org.example.infrastructure.data.mappers.HabitMapper;
import org.example.infrastructure.data.validators.ValidationResult;
import org.example.infrastructure.data.validators.json_validators.JsonHabitValidator;
import org.example.infrastructure.util.JsonResponse;
import org.example.infrastructure.util.ServiceHelper;
import org.example.infrastructure.util.ServletRequestHelper;
import org.example.infrastructure.util.TokenHelper;
import org.example.infrastructure.web.handlers.request_handlers.HttpServletRequestHandler;

import java.io.IOException;
import java.util.Map;

/**
 * Обработчик запросов на обновление привычек.
 * Реализует интерфейс {@link HttpServletRequestHandler} и отвечает за
 * обновление привычек пользователя на основе данных, полученных из запроса.
 */
@Auditable
public class HabitUpdateRequestHandler implements HttpServletRequestHandler {
    public static final HabitUpdateRequestHandler INSTANCE = new HabitUpdateRequestHandler();

    private HabitUpdateRequestHandler() {
    }


    /**
     * Обрабатывает HTTP-запрос на обновление привычки.
     * Извлекает идентификатор пользователя из токена авторизации и обновляет
     * привычку на основе переданных данных.
     *
     * @param req объект {@link HttpServletRequest}, содержащий запрос на обновление привычки.
     * @return объект {@link JsonResponse}, содержащий статус операции и возможные ошибки.
     * @throws IOException если при обработке запроса возникнет ошибка ввода-вывода.
     */
    @Override
    public JsonResponse handleRequest(HttpServletRequest req) throws IOException {
        Map<String, Object> jsonMap = ServletRequestHelper.getJsonMap(req);
        var jsonResponse = new JsonResponse();
        ValidationResult validationResult = JsonHabitValidator.validateUpdateHabitJson(jsonMap);

        if (validationResult.isValid()) {
            int userId = TokenHelper.getUserIdFromToken(req.getHeader("Authorization"));
            UpdateHabitDto dto = HabitMapper.INSTANCE.toUpdateHabitDto(jsonMap);

            if (ServiceHelper.getHabitServiceInstance().isUserHabit(userId, dto.id())) {
                ServiceHelper.getHabitServiceInstance().updateHabit(dto);

            } else {
                jsonResponse.addError(String.format(ErrorMessageConstants.HABIT_NOT_FOUND_FORMAT, dto.id()));
                jsonResponse.setStatusCode(HttpServletResponse.SC_BAD_REQUEST);
            }
        } else {
            jsonResponse.setStatusCode(HttpServletResponse.SC_BAD_REQUEST);
            jsonResponse.addAllErrors(validationResult.errors());
        }
        return jsonResponse;
    }
}

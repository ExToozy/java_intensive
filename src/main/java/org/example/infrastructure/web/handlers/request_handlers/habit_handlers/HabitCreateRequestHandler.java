package org.example.infrastructure.web.handlers.request_handlers.habit_handlers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.core.dtos.habit_dtos.CreateHabitDto;
import org.example.infrastructure.data.mappers.HabitMapper;
import org.example.infrastructure.data.validators.ValidationResult;
import org.example.infrastructure.data.validators.json_validators.JsonHabitValidator;
import org.example.infrastructure.util.JsonResponse;
import org.example.infrastructure.util.ServiceHelper;
import org.example.infrastructure.util.ServletRequestHelper;
import org.example.infrastructure.util.TokenHelper;
import org.example.infrastructure.web.handlers.request_handlers.HttpServletRequestHandler;

import java.util.Map;

/**
 * Обработчик запросов на создание привычки.
 * Реализует интерфейс {@link HttpServletRequestHandler} и отвечает за
 * создание новой привычки для пользователя.
 */
public class HabitCreateRequestHandler implements HttpServletRequestHandler {
    public static final HabitCreateRequestHandler INSTANCE = new HabitCreateRequestHandler();

    private HabitCreateRequestHandler() {
    }

    /**
     * Обрабатывает HTTP-запрос на создание привычки.
     *
     * @param req объект {@link HttpServletRequest}, содержащий запрос на создание привычки.
     * @return объект {@link JsonResponse}, содержащий статус создания.
     * @throws Exception если возникла ошибка при обработке запроса.
     */
    @Override
    public JsonResponse handleRequest(HttpServletRequest req) throws Exception {
        Map<String, Object> jsonMap = ServletRequestHelper.getJsonMap(req);
        var jsonResponse = new JsonResponse();
        ValidationResult validationResult = JsonHabitValidator.validateCreateHabitJson(jsonMap);

        if (validationResult.isValid()) {
            int userId = TokenHelper.getUserIdFromToken(req.getHeader("Authorization"));
            CreateHabitDto dto = HabitMapper.INSTANCE.toCreateHabitDto(jsonMap, userId);
            ServiceHelper.getHabitServiceInstance().createHabit(dto);
            jsonResponse.setStatusCode(HttpServletResponse.SC_CREATED);
        } else {
            jsonResponse.setStatusCode(HttpServletResponse.SC_BAD_REQUEST);
            jsonResponse.addAllErrors(validationResult.errors());
        }
        return jsonResponse;
    }
}

package org.example.infrastructure.web.handlers.request_handlers.user_handlers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.core.dtos.user_dtos.ChangeAdminStatusDto;
import org.example.core.models.User;
import org.example.infrastructure.data.mappers.UserMapper;
import org.example.infrastructure.data.validators.ValidationResult;
import org.example.infrastructure.data.validators.json_validators.JsonUserValidator;
import org.example.infrastructure.util.*;
import org.example.infrastructure.web.handlers.request_handlers.HttpServletRequestHandler;

import java.util.Map;


/**
 * Обработчик запроса на изменение статуса администратора пользователя,
 * реализующий {@link HttpServletRequestHandler}.
 * Предназначен для обработки HTTP-запросов на изменение прав администратора.
 * Доступ к запросу имеет только администратор.
 */
public class UserChangeAdminStatusRequestHandler implements HttpServletRequestHandler {
    public final static UserChangeAdminStatusRequestHandler INSTANCE = new UserChangeAdminStatusRequestHandler();

    private UserChangeAdminStatusRequestHandler() {
    }

    /**
     * Обрабатывает HTTP-запрос на изменение статуса администратора у пользователя.
     * Проверяет, что текущий пользователь является администратором, и валидирует входные данные запроса.
     * Возвращает JSON-ответ с результатом выполнения операции или сообщением об ошибке.
     *
     * @param req объект {@link HttpServletRequest}, содержащий запрос на изменение статуса администратора.
     * @return объект {@link JsonResponse}, содержащий результат операции или сообщение об ошибке.
     * @throws Exception если при обработке запроса возникнет ошибка.
     */
    @Override
    public JsonResponse handleRequest(HttpServletRequest req) throws Exception {
        JsonResponse jsonResponse;
        int userId = TokenHelper.getUserIdFromToken(req.getHeader("Authorization"));
        User user = ServiceHelper.getUserServiceInstance().getById(userId);
        if (user.isAdmin()) {
            jsonResponse = new JsonResponse();
            Map<String, Object> jsonMap = ServletRequestHelper.getJsonMap(req);
            ValidationResult validationResult = JsonUserValidator.validateChangeAdminStatusUserJson(jsonMap);
            if (validationResult.isValid()) {
                ChangeAdminStatusDto dto = UserMapper.INSTANCE.toChangeAdminStatusDto(jsonMap);
                ServiceHelper.getUserServiceInstance().changeUserAdminStatus(dto);
            } else {
                jsonResponse.addAllErrors(validationResult.errors());
                jsonResponse.setStatusCode(HttpServletResponse.SC_BAD_REQUEST);
            }
        } else {
            jsonResponse = JsonResponseHelper.getNotAllowedMethodResponse();
        }

        return jsonResponse;
    }
}

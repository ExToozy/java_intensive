package org.example.infrastructure.web.handlers.request_handlers.user_handlers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.annotations.Auditable;
import org.example.core.models.User;
import org.example.infrastructure.constants.ErrorMessageConstants;
import org.example.infrastructure.data.validators.ValidationResult;
import org.example.infrastructure.data.validators.json_validators.JsonUserValidator;
import org.example.infrastructure.util.JsonResponse;
import org.example.infrastructure.util.ServiceHelper;
import org.example.infrastructure.util.ServletRequestHelper;
import org.example.infrastructure.util.TokenHelper;
import org.example.infrastructure.web.handlers.request_handlers.HttpServletRequestHandler;

import java.util.Map;


/**
 * Обработчик запроса на удаление пользователя, реализующий {@link HttpServletRequestHandler}.
 * Предназначен для обработки HTTP-запросов на удаление пользователя и создания JSON-ответа.
 * Запрос доступен только для администратора или для самого пользователя.
 */
@Auditable
public class UserDeleteRequestHandler implements HttpServletRequestHandler {
    public final static UserDeleteRequestHandler INSTANCE = new UserDeleteRequestHandler();

    private UserDeleteRequestHandler() {
    }

    /**
     * Обрабатывает HTTP-запрос на удаление пользователя. Проверяет права доступа:
     * пользователь должен быть администратором или запрашивать удаление своего аккаунта.
     * Возвращает JSON-ответ с результатом операции.
     *
     * @param req объект {@link HttpServletRequest}, содержащий запрос на удаление пользователя.
     * @return объект {@link JsonResponse}, содержащий результат операции удаления или сообщение об ошибке.
     * @throws Exception если при обработке запроса возникнет ошибка.
     */
    @Override
    public JsonResponse handleRequest(HttpServletRequest req) throws Exception {
        Map<String, Object> jsonMap = ServletRequestHelper.getJsonMap(req);
        var jsonResponse = new JsonResponse();
        ValidationResult validationResult = JsonUserValidator.validateDeleteUserJson(jsonMap);

        if (validationResult.isValid()) {
            int userId = TokenHelper.getUserIdFromToken(req.getHeader("Authorization"));

            int userIdToDelete = (int) jsonMap.get("userId");
            User user = ServiceHelper.getUserServiceInstance().getById(userId);

            if (user != null && (userId == userIdToDelete || user.isAdmin())) {
                ServiceHelper.getUserServiceInstance().remove(userId);
                jsonResponse.setStatusCode(HttpServletResponse.SC_NO_CONTENT);

            } else {
                jsonResponse.addError(ErrorMessageConstants.USER_NOT_FOUND);
                jsonResponse.setStatusCode(HttpServletResponse.SC_BAD_REQUEST);
            }
        } else {
            jsonResponse.setStatusCode(HttpServletResponse.SC_BAD_REQUEST);
            jsonResponse.addAllErrors(validationResult.errors());
        }
        return jsonResponse;
    }
}

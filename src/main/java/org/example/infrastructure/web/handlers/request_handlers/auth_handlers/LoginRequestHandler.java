package org.example.infrastructure.web.handlers.request_handlers.auth_handlers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.core.exceptions.UserNotFoundException;
import org.example.core.models.User;
import org.example.infrastructure.constants.ErrorMessageConstants;
import org.example.infrastructure.data.mappers.UserMapper;
import org.example.infrastructure.data.validators.ValidationResult;
import org.example.infrastructure.data.validators.json_validators.JsonUserValidator;
import org.example.infrastructure.util.JsonResponse;
import org.example.infrastructure.util.ServiceHelper;
import org.example.infrastructure.util.ServletRequestHelper;
import org.example.infrastructure.web.handlers.request_handlers.HttpServletRequestHandler;

/**
 * Обработчик запросов на вход пользователя.
 * Реализует интерфейс {@link HttpServletRequestHandler} и отвечает за
 * аутентификацию пользователей.
 */
public class LoginRequestHandler implements HttpServletRequestHandler {
    public final static LoginRequestHandler INSTANCE = new LoginRequestHandler();

    private LoginRequestHandler() {
    }

    /**
     * Обрабатывает HTTP-запрос на вход пользователя.
     *
     * @param req объект {@link HttpServletRequest}, содержащий данные для входа.
     * @return объект {@link JsonResponse}, содержащий статус входа и токен пользователя.
     * @throws Exception если возникла ошибка при обработке запроса.
     */
    @Override
    public JsonResponse handleRequest(HttpServletRequest req) throws Exception {
        var jsonMap = ServletRequestHelper.getJsonMap(req);
        var jsonResponse = new JsonResponse();
        ValidationResult validationResult = JsonUserValidator.validateAuthUserJson(jsonMap);
        if (validationResult.isValid()) {
            var dto = UserMapper.INSTANCE.toAuthUserDto(jsonMap);
            try {
                User user = ServiceHelper.getAuthServiceInstance().login(dto);
                jsonResponse.putDataEntry("token", Integer.toString(user.getId()));
            } catch (UserNotFoundException e) {
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

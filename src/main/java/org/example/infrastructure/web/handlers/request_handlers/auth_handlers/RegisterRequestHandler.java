package org.example.infrastructure.web.handlers.request_handlers.auth_handlers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.annotations.Auditable;
import org.example.core.exceptions.InvalidEmailException;
import org.example.core.exceptions.UserAlreadyExistException;
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
 * Обработчик запросов на регистрацию пользователей.
 * Реализует интерфейс {@link HttpServletRequestHandler} и отвечает за
 * регистрацию нового пользователя в системе.
 */
@Auditable
public class RegisterRequestHandler implements HttpServletRequestHandler {

    public static final RegisterRequestHandler INSTANCE = new RegisterRequestHandler();

    private RegisterRequestHandler() {
    }

    /**
     * Обрабатывает HTTP-запрос на регистрацию нового пользователя.
     *
     * @param req объект {@link HttpServletRequest}, содержащий данные для регистрации.
     * @return объект {@link JsonResponse}, содержащий статус регистрации и токен пользователя.
     * @throws Exception если возникла ошибка при обработке запроса.
     */
    @Override
    public JsonResponse handleRequest(HttpServletRequest req) throws Exception {
        var jsonMap = ServletRequestHelper.getJsonMap(req);
        ValidationResult validationResult = JsonUserValidator.validateAuthUserJson(jsonMap);
        var jsonResponse = new JsonResponse();
        if (validationResult.isValid()) {
            var dto = UserMapper.INSTANCE.toAuthUserDto(jsonMap);
            try {
                User user = ServiceHelper.getAuthServiceInstance().register(dto);
                jsonResponse.putDataEntry("token", Integer.toString(user.getId()));
                jsonResponse.setStatusCode(HttpServletResponse.SC_CREATED);
            } catch (UserAlreadyExistException e) {
                jsonResponse.addError(String.format(ErrorMessageConstants.USER_ALREADY_EXIST_FORMAT, dto.getEmail()));
                jsonResponse.setStatusCode(HttpServletResponse.SC_BAD_REQUEST);
            } catch (InvalidEmailException e) {
                jsonResponse.addError(String.format(ErrorMessageConstants.INVALID_EMAIL_FORMAT, dto.getEmail()));
                jsonResponse.setStatusCode(HttpServletResponse.SC_BAD_REQUEST);
            }
        } else {
            jsonResponse.setStatusCode(HttpServletResponse.SC_BAD_REQUEST);
            jsonResponse.addAllErrors(validationResult.errors());
        }
        return jsonResponse;
    }
}

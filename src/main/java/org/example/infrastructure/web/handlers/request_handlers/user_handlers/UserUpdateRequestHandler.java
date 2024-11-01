package org.example.infrastructure.web.handlers.request_handlers.user_handlers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.annotations.Auditable;
import org.example.core.dtos.user_dtos.UpdateUserDto;
import org.example.core.exceptions.InvalidEmailException;
import org.example.core.models.User;
import org.example.infrastructure.constants.ErrorMessageConstants;
import org.example.infrastructure.data.mappers.UserMapper;
import org.example.infrastructure.data.validators.ValidationResult;
import org.example.infrastructure.data.validators.json_validators.JsonUserValidator;
import org.example.infrastructure.util.JsonResponse;
import org.example.infrastructure.util.ServiceHelper;
import org.example.infrastructure.util.ServletRequestHelper;
import org.example.infrastructure.util.TokenHelper;
import org.example.infrastructure.web.handlers.request_handlers.HttpServletRequestHandler;

import java.util.Map;

/**
 * Обработчик для обновления данных пользователя, реализующий {@link HttpServletRequestHandler}.
 * Предназначен для обработки HTTP-запроса с JSON-данными и обновления информации о пользователе
 * в соответствии с переданными данными.
 */
@Auditable
public class UserUpdateRequestHandler implements HttpServletRequestHandler {
    public final static UserUpdateRequestHandler INSTANCE = new UserUpdateRequestHandler();

    private UserUpdateRequestHandler() {
    }

    /**
     * Обрабатывает HTTP-запрос для обновления информации о пользователе, извлекая данные из JSON и выполняя валидацию.
     * Если запрос корректен и пользователь существует, обновляет информацию о пользователе.
     *
     * @param req объект {@link HttpServletRequest}, содержащий запрос на обновление пользователя.
     * @return объект {@link JsonResponse}, содержащий статус операции и возможные ошибки.
     * @throws Exception если при обработке возникнут ошибки.
     */
    @Override
    public JsonResponse handleRequest(HttpServletRequest req) throws Exception {
        Map<String, Object> jsonMap = ServletRequestHelper.getJsonMap(req);
        var jsonResponse = new JsonResponse();
        ValidationResult validationResult = JsonUserValidator.validateUpdateUserJson(jsonMap);

        if (validationResult.isValid()) {
            int userId = TokenHelper.getUserIdFromToken(req.getHeader("Authorization"));
            UpdateUserDto dto = UserMapper.INSTANCE.toUpdateUserDto(jsonMap);
            User user = ServiceHelper.getUserServiceInstance().getById(userId);

            if (user != null && (userId == dto.getUserId() || user.isAdmin())) {
                try {
                    ServiceHelper.getUserServiceInstance().update(dto);
                } catch (InvalidEmailException e) {
                    jsonResponse.addError(String.format(ErrorMessageConstants.INVALID_EMAIL_FORMAT, dto.getEmail()));
                    jsonResponse.setStatusCode(HttpServletResponse.SC_BAD_REQUEST);
                }
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

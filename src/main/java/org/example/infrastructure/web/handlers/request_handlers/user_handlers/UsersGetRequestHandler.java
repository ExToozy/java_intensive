package org.example.infrastructure.web.handlers.request_handlers.user_handlers;

import jakarta.servlet.http.HttpServletRequest;
import org.example.annotations.Auditable;
import org.example.core.models.User;
import org.example.infrastructure.data.mappers.UserMapper;
import org.example.infrastructure.util.JsonResponse;
import org.example.infrastructure.util.JsonResponseHelper;
import org.example.infrastructure.util.ServiceHelper;
import org.example.infrastructure.util.TokenHelper;
import org.example.infrastructure.web.handlers.request_handlers.HttpServletRequestHandler;

import java.util.List;

/**
 * Обработчик для получения списка всех пользователей, реализующий {@link HttpServletRequestHandler}.
 * Предназначен для обработки HTTP-запросов и возврата списка пользователей в формате JSON.
 * Доступен только для пользователей с правами администратора.
 */
@Auditable
public class UsersGetRequestHandler implements HttpServletRequestHandler {
    public final static UsersGetRequestHandler INSTANCE = new UsersGetRequestHandler();

    private UsersGetRequestHandler() {
    }

    /**
     * Обрабатывает HTTP-запрос для получения списка пользователей. Проверяет права доступа:
     * только администратор может получить список пользователей. Возвращает JSON-ответ,
     * содержащий список пользователей, или сообщение об ошибке при недостатке прав.
     *
     * @param req объект {@link HttpServletRequest}, содержащий запрос на получение списка пользователей.
     * @return объект {@link JsonResponse}, содержащий список пользователей или сообщение об ошибке.
     * @throws Exception если при обработке возникнут ошибки.
     */
    @Override
    public JsonResponse handleRequest(HttpServletRequest req) throws Exception {
        JsonResponse jsonResponse;
        int userId = TokenHelper.getUserIdFromToken(req.getHeader("Authorization"));
        User user = ServiceHelper.getUserServiceInstance().getById(userId);
        if (user.isAdmin()) {
            jsonResponse = new JsonResponse();
            List<User> users = ServiceHelper.getUserServiceInstance().getAll();
            jsonResponse.putDataEntry("users", UserMapper.INSTANCE.toUserDtoList(users));
        } else {
            jsonResponse = JsonResponseHelper.getNotAllowedMethodResponse();
        }
        return jsonResponse;
    }
}

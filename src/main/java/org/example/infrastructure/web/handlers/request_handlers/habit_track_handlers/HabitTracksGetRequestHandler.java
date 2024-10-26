package org.example.infrastructure.web.handlers.request_handlers.habit_track_handlers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.core.models.HabitTrack;
import org.example.infrastructure.util.JsonResponse;
import org.example.infrastructure.util.ServiceHelper;
import org.example.infrastructure.util.TokenHelper;
import org.example.infrastructure.web.handlers.request_handlers.HttpServletRequestHandler;

import java.util.List;

/**
 * Обработчик запроса на получение списка отслеживаемых привычек пользователя.
 * Реализует интерфейс {@link HttpServletRequestHandler} и обрабатывает
 * запросы для получения отслеживаемых привычек, связанных с пользователем,
 * идентификатор которого извлекается из токена авторизации.
 */
public class HabitTracksGetRequestHandler implements HttpServletRequestHandler {
    public final static HabitTracksGetRequestHandler INSTANCE = new HabitTracksGetRequestHandler();

    private HabitTracksGetRequestHandler() {
    }

    /**
     * Обрабатывает HTTP-запрос на получение списка отслеживаемых привычек пользователя.
     * Извлекает идентификатор пользователя из токена авторизации и запрашивает список
     * отслеживаемых привычек для этого пользователя.
     *
     * @param req объект {@link HttpServletRequest}, содержащий запрос на получение отслеживаемых привычек.
     * @return объект {@link JsonResponse}, содержащий данные с отслеживаемыми привычками или статус "No Content".
     * @throws Exception если при обработке запроса возникнет ошибка.
     */
    @Override
    public JsonResponse handleRequest(HttpServletRequest req) throws Exception {
        var jsonResponse = new JsonResponse();

        int userId = TokenHelper.getUserIdFromToken(req.getHeader("Authorization"));

        List<HabitTrack> habitTracks = ServiceHelper.getHabitTrackServiceInstance().getUserHabitTracks(userId);
        if (habitTracks.isEmpty()) {
            jsonResponse.setStatusCode(HttpServletResponse.SC_NO_CONTENT);
        }
        jsonResponse.putDataEntry("habitTracks", habitTracks);

        return jsonResponse;
    }
}

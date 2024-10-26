package org.example.infrastructure.web.servlets;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.annotations.Loggable;
import org.example.infrastructure.constants.ApiEndpointConstants;
import org.example.infrastructure.web.handlers.HttpServletHandler;
import org.example.infrastructure.web.handlers.request_handlers.habit_track_handlers.HabitTrackCreateRequestHandler;
import org.example.infrastructure.web.handlers.request_handlers.habit_track_handlers.HabitTrackDeleteRequestHandler;
import org.example.infrastructure.web.handlers.request_handlers.habit_track_handlers.HabitTracksGetRequestHandler;

import java.io.IOException;

@Loggable
@WebServlet(ApiEndpointConstants.HABIT_TRACK_ENDPOINT)
public class HabitTrackServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpServletHandler.handle(req, resp, HabitTracksGetRequestHandler.INSTANCE);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpServletHandler.handle(req, resp, HabitTrackCreateRequestHandler.INSTANCE);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpServletHandler.handle(req, resp, HabitTrackDeleteRequestHandler.INSTANCE);
    }
}

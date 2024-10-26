package org.example.infrastructure.web.servlets;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.annotations.Loggable;
import org.example.infrastructure.constants.ApiEndpointConstants;
import org.example.infrastructure.web.handlers.HttpServletHandler;
import org.example.infrastructure.web.handlers.request_handlers.habit_handlers.HabitCreateRequestHandler;
import org.example.infrastructure.web.handlers.request_handlers.habit_handlers.HabitDeleteRequestHandler;
import org.example.infrastructure.web.handlers.request_handlers.habit_handlers.HabitUpdateRequestHandler;
import org.example.infrastructure.web.handlers.request_handlers.habit_handlers.HabitsGetRequestHandler;

import java.io.IOException;

@Loggable
@WebServlet(ApiEndpointConstants.HABIT_ENDPOINT)
public class HabitServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpServletHandler.handle(req, resp, HabitsGetRequestHandler.INSTANCE);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpServletHandler.handle(req, resp, HabitCreateRequestHandler.INSTANCE);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpServletHandler.handle(req, resp, HabitUpdateRequestHandler.INSTANCE);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpServletHandler.handle(req, resp, HabitDeleteRequestHandler.INSTANCE);
    }
}

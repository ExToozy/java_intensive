package org.example.infrastructure.web.servlets;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.annotations.Loggable;
import org.example.infrastructure.constants.ApiEndpointConstants;
import org.example.infrastructure.web.handlers.HttpServletHandler;
import org.example.infrastructure.web.handlers.request_handlers.habit_statistic_handlers.HabitGetStatisticsRequestHandler;

import java.io.IOException;

@Loggable
@WebServlet(ApiEndpointConstants.HABIT_STATISTIC_ENDPOINT)
public class HabitStatisticServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpServletHandler.handle(req, resp, HabitGetStatisticsRequestHandler.INSTANCE);
    }
}

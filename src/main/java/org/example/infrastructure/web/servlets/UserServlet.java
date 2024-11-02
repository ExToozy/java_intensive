package org.example.infrastructure.web.servlets;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.annotations.Loggable;
import org.example.infrastructure.constants.ApiEndpointConstants;
import org.example.infrastructure.web.handlers.HttpServletHandler;
import org.example.infrastructure.web.handlers.request_handlers.user_handlers.UserChangeAdminStatusRequestHandler;
import org.example.infrastructure.web.handlers.request_handlers.user_handlers.UserDeleteRequestHandler;
import org.example.infrastructure.web.handlers.request_handlers.user_handlers.UserUpdateRequestHandler;
import org.example.infrastructure.web.handlers.request_handlers.user_handlers.UsersGetRequestHandler;

import java.io.IOException;

@Loggable
@WebServlet(ApiEndpointConstants.USER_ENDPOINT)
public class UserServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpServletHandler.handle(req, resp, UsersGetRequestHandler.INSTANCE);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpServletHandler.handle(req, resp, UserChangeAdminStatusRequestHandler.INSTANCE);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpServletHandler.handle(req, resp, UserUpdateRequestHandler.INSTANCE);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpServletHandler.handle(req, resp, UserDeleteRequestHandler.INSTANCE);
    }
}

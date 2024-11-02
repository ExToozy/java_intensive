package org.example.infrastructure.web.servlets;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.annotations.Loggable;
import org.example.infrastructure.constants.ApiEndpointConstants;
import org.example.infrastructure.web.handlers.HttpServletHandler;
import org.example.infrastructure.web.handlers.request_handlers.auth_handlers.RegisterRequestHandler;

import java.io.IOException;

@Loggable
@WebServlet(ApiEndpointConstants.REGISTER_ENDPOINT)
public class RegisterServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpServletHandler.handle(req, resp, RegisterRequestHandler.INSTANCE);
    }
}

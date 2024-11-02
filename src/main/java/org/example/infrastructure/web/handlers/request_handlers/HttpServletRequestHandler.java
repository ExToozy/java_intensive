package org.example.infrastructure.web.handlers.request_handlers;

import jakarta.servlet.http.HttpServletRequest;
import org.example.infrastructure.util.JsonResponse;

/**
 * Иинтерфейс для обработки HTTP-запросов и возврата JSON-ответов.
 */
public interface HttpServletRequestHandler {
    JsonResponse handleRequest(HttpServletRequest req) throws Exception;
}

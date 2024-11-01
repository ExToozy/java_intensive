package org.example.aspects;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.example.infrastructure.util.JsonMapper;
import org.example.infrastructure.util.JsonResponse;
import org.example.infrastructure.web.handlers.request_handlers.HttpServletRequestHandler;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

@Aspect
public class AuditableAspect {
    @Pointcut("within(@org.example.annotations.Auditable *) && execution(* * (..))")
    public void annotatedByAuditable() {
    }

    @Around("annotatedByAuditable()")
    public Object audit(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object result = proceedingJoinPoint.proceed();
        auditUserRequest(proceedingJoinPoint, (JsonResponse) result);
        return result;
    }

    private boolean isRequestHandlerImpl(ProceedingJoinPoint proceedingJoinPoint) {
        return Arrays.stream(proceedingJoinPoint
                        .getTarget()
                        .getClass()
                        .getInterfaces())
                .anyMatch(classInterface -> classInterface.getName().equals(HttpServletRequestHandler.class.getName()));
    }

    private void auditUserRequest(ProceedingJoinPoint proceedingJoinPoint, JsonResponse result) throws IOException {
        if (isRequestHandlerImpl(proceedingJoinPoint) && proceedingJoinPoint.getSignature().getName().equals("handleRequest")) {
            Object[] args = proceedingJoinPoint.getArgs();
            HttpServletRequest request = (HttpServletRequest) Arrays.stream(args)
                    .filter(o -> o instanceof HttpServletRequest)
                    .findFirst()
                    .orElse(null);
            if (request != null) {
                Map<String, Object> jsonMap = JsonMapper.getJsonMap(request.getReader());
                String strRequestMap = StringUtils.join(jsonMap);

                System.out.printf("User send request to %s with body %s and their response %s%n",
                        request.getRequestURI(), strRequestMap, result.toJsonString());
            }
        }
    }
}

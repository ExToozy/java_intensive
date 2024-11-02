package org.example.aspects;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.example.core.dtos.user_audit_dtos.CreateUserAuditDto;
import org.example.infrastructure.data.repositories.JdbcUserAuditRepository;
import org.example.infrastructure.util.TokenHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Slf4j
public class AuditableAspect {

    ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private JdbcUserAuditRepository jdbcUserAuditRepository;

    @Autowired(required = false)
    private HttpServletRequest request;


    @Pointcut("@annotation(org.example.annotations.Auditable)")
    public void annotatedByAuditable() {
    }

    @Around("annotatedByAuditable()")
    public Object audit(ProceedingJoinPoint joinPoint) throws Throwable {
        Object responseBody = joinPoint.proceed();
        Object[] args = joinPoint.getArgs();
        String token = null;
        Object requestBody = null;

        for (Object arg : args) {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            if (signature.getMethod().isAnnotationPresent(RequestHeader.class) && arg instanceof String) {
                token = (String) arg;
            } else if (signature.getMethod().isAnnotationPresent(RequestBody.class)) {
                requestBody = arg;
            }
        }

        if (token != null) {
            String requestUri = request.getRequestURI();
            int userIdFromToken = TokenHelper.getUserIdFromToken(token);
            String requestBodyStr = null;
            String responseBodyStr = null;
            if (requestBody != null) {
                requestBodyStr = objectMapper.writeValueAsString(requestBody);
            }
            if (responseBody != null) {
                responseBodyStr = objectMapper.writeValueAsString(responseBody);
            }
            CreateUserAuditDto dto = new CreateUserAuditDto(userIdFromToken, requestUri, requestBodyStr, responseBodyStr);
            jdbcUserAuditRepository.create(dto);
        }

        return responseBody;
    }
}

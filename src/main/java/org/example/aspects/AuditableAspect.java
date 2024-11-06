package org.example.aspects;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.example.core.dtos.user_audit_dtos.CreateUserAuditDto;
import org.example.exceptions.InvalidTokenException;
import org.example.infrastructure.data.repositories.JdbcUserAuditRepository;
import org.example.infrastructure.util.TokenHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Parameter;

@Aspect
@Slf4j
@Component
public class AuditableAspect {

    @Autowired
    ObjectMapper mapper;

    @Autowired
    JdbcUserAuditRepository jdbcUserAuditRepository;


    @Pointcut("@annotation(org.example.annotations.Auditable)")
    public void annotatedByAuditable() {
    }

    @Around("annotatedByAuditable()")
    public Object audit(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        Object responseBody = joinPoint.proceed();
        Object[] args = joinPoint.getArgs();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Parameter[] parameters = signature.getMethod().getParameters();
        String token = null;
        Object requestBody = null;
        for (int i = 0; i < parameters.length; i++) {
            Parameter arg = parameters[i];
            RequestHeader authorizationHeader = arg.getAnnotation(RequestHeader.class);
            if (authorizationHeader != null && authorizationHeader.value().equals("Authorization")) {
                token = (String) args[i];
                continue;
            }
            RequestBody requestBodyAnnotation = arg.getAnnotation(RequestBody.class);
            if (requestBodyAnnotation != null) {
                requestBody = args[i];
            }
        }
        createUserAudit(token, requestBody, responseBody, request);

        return responseBody;
    }

    private void createUserAudit(String token, Object requestBody, Object responseBody, HttpServletRequest request) throws InvalidTokenException, JsonProcessingException {
        if (token != null && request != null) {
            String requestUri = request.getRequestURI();
            int userIdFromToken = TokenHelper.getUserIdFromToken(token);
            String requestBodyStr = null;
            String responseBodyStr = null;
            if (requestBody != null) {
                requestBodyStr = mapper.writeValueAsString(requestBody);
            }
            if (responseBody != null) {
                responseBodyStr = mapper.writeValueAsString(responseBody);
            }
            CreateUserAuditDto dto = new CreateUserAuditDto(userIdFromToken, requestUri, requestBodyStr, responseBodyStr);
            jdbcUserAuditRepository.create(dto);
        }
    }
}

package org.example.aspects;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.NoArgsConstructor;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Parameter;

@Aspect
@NoArgsConstructor
public class AuditableAspect {

    private final static ObjectMapper OBJECT_MAPPER;

    static {
        OBJECT_MAPPER = new ObjectMapper();
        OBJECT_MAPPER.registerModule(new JavaTimeModule());
    }

    @Autowired
    JdbcUserAuditRepository jdbcUserAuditRepository;

    @Autowired(required = false)
    HttpServletRequest request;


    @Pointcut("@annotation(org.example.annotations.Auditable)")
    public void annotatedByAuditable() {
    }

    @Around("annotatedByAuditable()")
    public Object audit(ProceedingJoinPoint joinPoint) throws Throwable {
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
        createUserAudit(token, requestBody, responseBody);

        return responseBody;
    }

    private void createUserAudit(String token, Object requestBody, Object responseBody) throws InvalidTokenException, JsonProcessingException {
        if (token != null && request != null) {
            String requestUri = request.getRequestURI();
            System.out.println(requestUri);
            int userIdFromToken = TokenHelper.getUserIdFromToken(token);
            System.out.println(userIdFromToken);
            String requestBodyStr = null;
            String responseBodyStr = null;
            if (requestBody != null) {
                requestBodyStr = OBJECT_MAPPER.writeValueAsString(requestBody);
            }
            if (responseBody != null) {
                responseBodyStr = OBJECT_MAPPER.writeValueAsString(responseBody);
            }
            CreateUserAuditDto dto = new CreateUserAuditDto(userIdFromToken, requestUri, requestBodyStr, responseBodyStr);
            jdbcUserAuditRepository.create(dto);
        }
    }
}

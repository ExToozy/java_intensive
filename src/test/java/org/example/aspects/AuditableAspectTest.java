package org.example.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.example.infrastructure.data.repositories.JdbcUserAuditRepository;
import org.example.infrastructure.util.TokenHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuditableAspectTest {

    @Mock
    private JdbcUserAuditRepository jdbcUserAuditRepository;

    @Mock
    private HttpServletRequest request;

    @Mock
    private ProceedingJoinPoint joinPoint;

    @InjectMocks
    private AuditableAspect auditableAspect;

    @BeforeEach
    void setUp() {
        auditableAspect.request = request;
    }

    @Test
    void testAudit_ValidToken_CreatesUserAudit() throws Throwable {
        String token = "Bearer 123";
        String requestUri = "/test/uri";
        String requestBody = "test";
        String responseBody = "test";

        Method method = mock(Method.class);

        Parameter headerParam = mock(Parameter.class);
        RequestHeader headerAnnotation = mock(RequestHeader.class);
        when(headerAnnotation.value()).thenReturn("Authorization");
        when(headerParam.getAnnotation(RequestHeader.class)).thenReturn(headerAnnotation);

        Parameter bodyParam = mock(Parameter.class);
        when(bodyParam.getAnnotation(RequestBody.class)).thenReturn(mock(RequestBody.class));
        when(bodyParam.getAnnotation(RequestHeader.class)).thenReturn(null);

        when(method.getParameters()).thenReturn(new Parameter[]{headerParam, bodyParam});

        MethodSignature methodSignature = mock(MethodSignature.class);
        when(joinPoint.getSignature()).thenReturn(methodSignature);
        when(methodSignature.getMethod()).thenReturn(method);

        when(joinPoint.getArgs()).thenReturn(new Object[]{token, requestBody});
        when(joinPoint.proceed()).thenReturn(responseBody);
        when(request.getRequestURI()).thenReturn(requestUri);

        try (var mockedStatic = mockStatic(TokenHelper.class)) {
            mockedStatic.when(() -> TokenHelper.getUserIdFromToken(token)).thenReturn(123);

            Object result = auditableAspect.audit(joinPoint);

            verify(jdbcUserAuditRepository, times(1))
                    .create(argThat(argument -> {
                        System.out.println(argument);
                        return argument.getUserId() == 123 &&
                                argument.getRequestBody() != null &&
                                argument.getRequestUri().equals(requestUri) &&
                                argument.getResponseBody() != null;
                    }));

            assertThat(result).isEqualTo(responseBody);
        }
    }
}

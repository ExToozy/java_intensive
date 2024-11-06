package org.example.core.dtos.user_audit_dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserAuditDto {
    int userId;
    String requestUri;
    String requestBody;
    String responseBody;
}

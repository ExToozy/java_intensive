package com.example.audit_aspect_starter.dtos;

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

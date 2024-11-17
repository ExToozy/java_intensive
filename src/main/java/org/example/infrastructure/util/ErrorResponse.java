package org.example.infrastructure.util;


import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "Error response", description = "Response with error info", example = "{\"error\":\"error message\"}")
public record ErrorResponse(Object error) {
}

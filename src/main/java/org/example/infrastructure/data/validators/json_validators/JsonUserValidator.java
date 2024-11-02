package org.example.infrastructure.data.validators.json_validators;

import org.example.infrastructure.data.validators.ValidationResult;
import org.example.infrastructure.util.JsonValidatorHelper;

import java.util.Map;

/**
 * Класс для валидации JSON-объектов, связанных с пользователями.
 * Содержит методы для проверки структуры JSON-данных, отправляемых для
 * различных операций с пользователями, таких как аутентификация, обновление и удаление.
 */
public class JsonUserValidator {

    /**
     * Валидация JSON для аутентификации пользователя.
     *
     * @param jsonMap JSON-данные, содержащие информацию для аутентификации
     * @return результат валидации, содержащий ошибки, если они есть
     */
    public static ValidationResult validateAuthUserJson(Map<String, Object> jsonMap) {
        Map<String, Map<String, Object>> fieldValidationMap = Map.of(
                "email", Map.of(
                        "class", String.class
                ),
                "password", Map.of(
                        "class", String.class
                )
        );
        return JsonValidatorHelper.validateJsonFields(jsonMap, fieldValidationMap);
    }

    /**
     * Валидация JSON для удаления пользователя.
     *
     * @param jsonMap JSON-данные, содержащие информацию для удаления пользователя
     * @return результат валидации, содержащий ошибки, если они есть
     */
    public static ValidationResult validateDeleteUserJson(Map<String, Object> jsonMap) {
        Map<String, Map<String, Object>> fieldValidationMap = Map.of(
                "userId", Map.of(
                        "class", Integer.class
                )
        );
        return JsonValidatorHelper.validateJsonFields(jsonMap, fieldValidationMap);
    }

    /**
     * Валидация JSON для обновления информации о пользователе.
     *
     * @param jsonMap JSON-данные, содержащие информацию для обновления пользователя
     * @return результат валидации, содержащий ошибки, если они есть
     */
    public static ValidationResult validateUpdateUserJson(Map<String, Object> jsonMap) {
        Map<String, Map<String, Object>> fieldValidationMap = Map.of(
                "email", Map.of(
                        "class", String.class,
                        "nullable", true
                ),
                "password", Map.of(
                        "class", String.class,
                        "nullable", true
                )
        );
        return JsonValidatorHelper.validateJsonFields(jsonMap, fieldValidationMap);
    }

    /**
     * Валидация JSON для изменения статуса администратора пользователя.
     *
     * @param jsonMap JSON-данные, содержащие информацию для изменения статуса
     * @return результат валидации, содержащий ошибки, если они есть
     */
    public static ValidationResult validateChangeAdminStatusUserJson(Map<String, Object> jsonMap) {
        Map<String, Map<String, Object>> fieldValidationMap = Map.of(
                "userId", Map.of(
                        "class", Integer.class
                ),
                "isAdmin", Map.of(
                        "class", Boolean.class
                )
        );
        return JsonValidatorHelper.validateJsonFields(jsonMap, fieldValidationMap);
    }
}

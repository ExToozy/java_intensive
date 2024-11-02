package org.example.infrastructure.data.validators.json_validators;

import org.example.infrastructure.data.validators.ValidationResult;
import org.example.infrastructure.util.JsonValidatorHelper;

import java.util.Map;

/**
 * Класс для валидации JSON-объектов, связанных с трекингом привычек.
 * Содержит методы для проверки структуры JSON-данных, отправляемых для
 * создания и удаления треков привычек.
 */
public class JsonHabitTrackValidator {

    /**
     * Валидация JSON для создания трека привычки.
     *
     * @param jsonMap JSON-данные, содержащие информацию для создания трека привычки
     * @return результат валидации, содержащий ошибки, если они есть
     */
    public static ValidationResult validateCreateHabitTrackJson(Map<String, Object> jsonMap) {
        Map<String, Map<String, Object>> fieldValidationMap = Map.of(
                "habitId", Map.of(
                        "class", Integer.class
                )
        );
        return JsonValidatorHelper.validateJsonFields(jsonMap, fieldValidationMap);
    }

    /**
     * Валидация JSON для удаления трека привычки.
     *
     * @param jsonMap JSON-данные, содержащие информацию для удаления трека привычки
     * @return результат валидации, содержащий ошибки, если они есть
     */
    public static ValidationResult validateDeleteHabitTrackJson(Map<String, Object> jsonMap) {
        Map<String, Map<String, Object>> fieldValidationMap = Map.of(
                "trackId", Map.of(
                        "class", Integer.class
                )
        );
        return JsonValidatorHelper.validateJsonFields(jsonMap, fieldValidationMap);
    }
}

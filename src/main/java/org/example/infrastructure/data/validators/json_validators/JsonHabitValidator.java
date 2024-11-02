package org.example.infrastructure.data.validators.json_validators;

import org.example.core.models.HabitFrequency;
import org.example.infrastructure.data.validators.ValidationResult;
import org.example.infrastructure.util.JsonValidatorHelper;

import java.util.Arrays;
import java.util.Map;

/**
 * Класс для валидации JSON-объектов, связанных с привычками.
 * Содержит методы для проверки структуры JSON-данных, отправляемых для
 * различных операций с привычками, таких как создание, обновление и удаление.
 */
public class JsonHabitValidator {

    /**
     * Валидация JSON для создания привычки.
     *
     * @param jsonMap JSON-данные, содержащие информацию для создания привычки
     * @return результат валидации, содержащий ошибки, если они есть
     */
    public static ValidationResult validateCreateHabitJson(Map<String, Object> jsonMap) {
        Map<String, Map<String, Object>> fieldValidationMap = Map.of(
                "name", Map.of(
                        "class", String.class
                ),
                "description", Map.of(
                        "class", String.class
                ),
                "frequency", Map.of(
                        "class", String.class,
                        "choices", Arrays.stream(HabitFrequency.values()).map(Enum::toString).toArray()
                )
        );
        return JsonValidatorHelper.validateJsonFields(jsonMap, fieldValidationMap);
    }

    /**
     * Валидация JSON для обновления привычки.
     *
     * @param jsonMap JSON-данные, содержащие информацию для обновления привычки
     * @return результат валидации, содержащий ошибки, если они есть
     */
    public static ValidationResult validateUpdateHabitJson(Map<String, Object> jsonMap) {
        Map<String, Map<String, Object>> fieldValidationMap = Map.of(
                "habitId", Map.of(
                        "class", Integer.class
                ),
                "name", Map.of(
                        "class", String.class
                ),
                "description", Map.of(
                        "class", String.class
                ),
                "frequency", Map.of(
                        "class", String.class,
                        "choices", Arrays.stream(HabitFrequency.values()).map(Enum::toString).toArray()
                )
        );
        return JsonValidatorHelper.validateJsonFields(jsonMap, fieldValidationMap);
    }

    /**
     * Валидация JSON для удаления привычки.
     *
     * @param jsonMap JSON-данные, содержащие информацию для удаления привычки
     * @return результат валидации, содержащий ошибки, если они есть
     */
    public static ValidationResult validateDeleteHabitJson(Map<String, Object> jsonMap) {
        Map<String, Map<String, Object>> fieldValidationMap = Map.of(
                "habitId", Map.of(
                        "class", Integer.class
                )
        );
        return JsonValidatorHelper.validateJsonFields(jsonMap, fieldValidationMap);
    }
}

package org.example.infrastructure.constants;

public class ErrorMessageConstants {
    public final static String USER_UNAUTHORIZED_OR_TOKEN_INVALID = "User is not authorized or token is invalid";
    public final static String USER_NOT_FOUND = "User not found";
    public final static String USER_ALREADY_EXIST_FORMAT = "User with %s already exist";
    public final static String INVALID_EMAIL_FORMAT = "Email is %s invalid";
    public final static String HABIT_NOT_FOUND_FORMAT = "Habit with id %s not found";
    public final static String HABIT_TRACK_NOT_FOUND_FORMAT = "Habit track with id %s not found";
    public final static String METHOD_NOT_ALLOWED = "Method not allowed";
    public final static String FIELD_NOT_IN_CHOICES_FORMAT = "Field: '%s' must be equal to one of these values: %s";
    public final static String FIELD_TYPE_ERROR = "Field: '%s' must be %s";
    public final static String JSON_IS_NULL = "Json is null";
    public final static String JSON_REQUIRED_FIELD_FORMAT = "Field: '%s' is required";
    public final static String JSON_FIELD_MUST_NOT_BE_NULL_FORMAT = "Field: '%s' must not be null";
    public final static String UNKNOWN_ENDPOINT = "Endpoint not found";
    public static final String INTERVAL_SERVER_ERROR = "Internal server error. Try later";
}

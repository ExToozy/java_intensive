package org.example.infrastructure.constants;

public class ApiEndpointConstants {
    public static final String ALL_ENDPOINTS = "/api/*";
    public static final String REGISTER_ENDPOINT = "/api/register";
    public static final String[] REGISTER_ENDPOINT_METHODS = {"POST"};

    public static final String LOGIN_ENDPOINT = "/api/login";
    public static final String[] LOGIN_ENDPOINT_METHODS = {"POST"};

    public static final String HABIT_ENDPOINT = "/api/habits";
    public static final String[] HABIT_ENDPOINT_METHODS = {"POST", "GET", "DELETE", "PUT"};

    public static final String HABIT_TRACK_ENDPOINT = "/api/habit-tracks";
    public static final String[] HABIT_TRACK_ENDPOINT_METHODS = {"POST", "GET", "DELETE"};

    public static final String HABIT_STATISTIC_ENDPOINT = "/api/habits-statistic";
    public static final String[] HABIT_STATISTIC_ENDPOINT_METHODS = {"GET"};

    public static final String USER_ENDPOINT = "/api/users";
    public static final String[] USER_ENDPOINT_METHODS = {"GET", "POST", "DELETE", "PUT"};
}

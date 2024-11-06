package org.example.infrastructure.constants;

public class SqlConstants {
    public static final String CREATE_HABIT_SQL = "INSERT INTO habit_tracker_schema.habits (user_id, name, description, frequency) VALUES (?, ?, ?, ?)";
    public static final String GET_USER_HABITS_SQL = "SELECT * FROM habit_tracker_schema.habits where user_id = ?";
    public static final String GET_USER_HABIT_BY_ID_SQL = "SELECT * FROM habit_tracker_schema.habits where id = ?";
    public static final String UPDATE_HABIT_SQL = "UPDATE habit_tracker_schema.habits SET name = ?, description = ?, frequency = ? WHERE id = ?";
    public static final String REMOVE_HABIT_SQL = "DELETE FROM habit_tracker_schema.habits WHERE id = ?";
    public static final String CREATE_HABIT_TRACK_SQL = "INSERT INTO habit_tracker_schema.habit_tracks (habit_id) VALUES (?)";
    public static final String GET_HABIT_TRACKS_SQL = "SELECT * FROM habit_tracker_schema.habit_tracks where habit_id = ?";
    public static final String REMOVE_ALL_HABIT_TRACK_SQL = "DELETE FROM habit_tracker_schema.habit_tracks WHERE habit_id = ?";
    public static final String REMOVE_HABIT_TRACK_SQL = "DELETE FROM habit_tracker_schema.habit_tracks WHERE id = ?";
    public static final String CREATE_USER_SQL = "INSERT INTO habit_tracker_schema.users (email, password) VALUES (?, ?)";
    public static final String GET_USER_BY_EMAIL_SQL = "SELECT * FROM habit_tracker_schema.users WHERE email = ?";
    public static final String GET_ALL_USERS_SQL = "SELECT * FROM habit_tracker_schema.users";
    public static final String UPDATE_USER_SQL = "UPDATE habit_tracker_schema.users SET email = ?, password = ? WHERE id = ?";
    public static final String REMOVE_USER_SQL = "DELETE FROM habit_tracker_schema.users WHERE id = ?";
    public static final String CHANGE_USER_STATUS_SQL = "UPDATE habit_tracker_schema.users SET is_admin = ? WHERE id = ?";
    public static final String GET_USER_BY_ID_SQL = "SELECT * FROM habit_tracker_schema.users WHERE id = ?";
    public static final String CREATE_USER_AUDIT_SQL = "INSERT INTO habit_tracker_schema.user_audit (user_id, request_uri, request_body, response_body) VALUES (?, ?, ?, ?)";
}

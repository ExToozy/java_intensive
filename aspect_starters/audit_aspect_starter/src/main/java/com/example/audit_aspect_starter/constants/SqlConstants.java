package com.example.audit_aspect_starter.constants;

public class SqlConstants {
    public static final String CREATE_USER_AUDIT_SQL = "INSERT INTO habit_tracker_schema.user_audit (user_id, request_uri, request_body, response_body) VALUES (?, ?, ?, ?)";
}

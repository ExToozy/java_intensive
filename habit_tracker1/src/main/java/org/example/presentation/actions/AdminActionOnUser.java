package org.example.presentation.actions;

public enum AdminActionOnUser {
    OPEN_HABITS("View user habits"),
    CHANGE_ADMIN_STATUS("Change admin status"),
    DELETE("Delete user"),
    EXIT("Exit");
    private final String str;

    AdminActionOnUser(String str) {
        this.str = str;
    }

    @Override
    public String toString() {
        return str;
    }
}

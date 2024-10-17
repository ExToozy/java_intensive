package org.example.presentation.console.actions;

public enum AdminAction {
    OPEN_HABIT_TRACKER("Open habits menu"),
    SHOW_USERS("View users"),
    EXIT("Exit");
    private final String str;

    AdminAction(String str) {
        this.str = str;
    }

    @Override
    public String toString() {
        return str;
    }
}

package org.example.presentation.actions;

public enum UpdateUserAction {
    UPDATE_EMAIL("Update email"),
    UPDATE_PASSWORD("Update password"),
    CANCEL("Cancel");
    private final String str;

    UpdateUserAction(String str) {
        this.str = str;
    }

    @Override
    public String toString() {
        return str;
    }
}

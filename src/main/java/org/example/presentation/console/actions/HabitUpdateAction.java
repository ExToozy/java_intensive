package org.example.presentation.console.actions;

public enum HabitUpdateAction {
    UPDATE_NAME("Change name"),
    UPDATE_DESCRIPTION("Change description"),
    UPDATE_FREQUENCY("Change frequency"),
    SAVE("Save"),
    CANCEL("Cancel");
    private final String str;

    HabitUpdateAction(String str) {
        this.str = str;
    }

    @Override
    public String toString() {
        return str;
    }
}

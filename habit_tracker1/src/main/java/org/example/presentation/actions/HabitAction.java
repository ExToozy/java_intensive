package org.example.presentation.actions;

public enum HabitAction {
    COMPLETE("Complete habit"),
    UPDATE("Edit habit"),
    DELETE("Delete habit"),
    SHOW_COMPLETE_COUNT("View the number of executions for the period"),
    EXIT("Exit");
    private final String str;

    HabitAction(String str) {
        this.str = str;
    }

    @Override
    public String toString() {
        return str;
    }
}

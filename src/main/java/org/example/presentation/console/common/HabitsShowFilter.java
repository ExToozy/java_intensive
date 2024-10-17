package org.example.presentation.console.common;

public enum HabitsShowFilter {
    ALL("All"),
    COMPLETED("Completed"),
    NOT_COMPLETED("Not completed");
    private final String str;

    HabitsShowFilter(String str) {
        this.str = str;
    }

    @Override
    public String toString() {
        return str;
    }
}

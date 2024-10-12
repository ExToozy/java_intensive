package org.example.presentation.actions;

public enum UserAction {
    CREATE_HABIT("Create habit"),
    SHOW_HABITS("View habits"),
    SHOW_STATISTICS("Show habit statistics"),
    DELETE("Delete account"),
    UPDATE("Edit account"),
    EXIT("Exit");
    private final String str;

    UserAction(String str) {
        this.str = str;
    }

    @Override
    public String toString() {
        return str;
    }
}

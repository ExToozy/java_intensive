package org.example.presentation.console.actions;

public enum AuthAction {
    LOGIN("Sign in"),
    REGISTER("Sign up");
    private final String str;

    AuthAction(String str) {
        this.str = str;
    }

    @Override
    public String toString() {
        return str;
    }
}

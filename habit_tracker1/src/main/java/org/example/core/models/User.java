package org.example.core.models;

import java.util.UUID;

public class User {
    private UUID id;
    private String email;
    private String password;
    private boolean isAdmin;

    public User(UUID id, String email, String password, boolean isAdmin) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.isAdmin = isAdmin;
    }

    public UUID getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public boolean isAdmin() {
        return isAdmin;
    }
}

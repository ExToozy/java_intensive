package org.example.infastructure.data.models;

import java.util.UUID;

public class UserEntity {
    private UUID id;
    private boolean isAdmin;
    private String email;
    private String password;

    public UserEntity(UUID id, String email, String password, boolean isAdmin) {
        this.isAdmin = isAdmin;
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public UUID getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        this.isAdmin = admin;
    }
}

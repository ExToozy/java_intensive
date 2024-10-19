package org.example.infrastructure.data.models;


public class UserEntity {
    private int id;
    private boolean isAdmin;
    private String email;
    private String password;

    public UserEntity(int id, String email, String password, boolean isAdmin) {
        this.isAdmin = isAdmin;
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public int getId() {
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

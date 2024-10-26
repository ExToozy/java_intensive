package org.example.core.dtos.user_dtos;

public class AuthUserDto {
    private final String email;

    private String password;

    public AuthUserDto(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

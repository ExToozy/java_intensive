package org.example.core.dtos.user_dtos;


public class UpdateUserDto {
    private final int userId;
    private String email;
    private String password;
    public UpdateUserDto(int userId, String email, String password) {
        this.userId = userId;
        this.email = email;
        this.password = password;
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

    public int getUserId() {
        return userId;
    }
}

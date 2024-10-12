package org.example.core.repositories.user_repository.dtos;

public class CreateUserDto {
    private String email;

    private String password;

    public CreateUserDto(String email, String password) {
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

package org.example.core.repositories.user_repository.dtos;


public class ChangeAdminStatusDto {
    private int userId;
    private boolean isAdmin;

    public ChangeAdminStatusDto(int userId, boolean isAdmin) {
        this.userId = userId;
        this.isAdmin = isAdmin;
    }

    public int getUserId() {
        return userId;
    }

    public boolean isAdmin() {
        return isAdmin;
    }
}

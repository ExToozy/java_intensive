package org.example.core.dtos.user_dtos;


public class ChangeAdminStatusDto {
    private final int userId;
    private final boolean isAdmin;

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

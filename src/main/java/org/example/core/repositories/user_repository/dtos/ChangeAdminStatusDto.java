package org.example.core.repositories.user_repository.dtos;

import java.util.UUID;

public class ChangeAdminStatusDto {
    private UUID userId;
    private boolean isAdmin;

    public ChangeAdminStatusDto(UUID userId, boolean isAdmin) {
        this.userId = userId;
        this.isAdmin = isAdmin;
    }

    public UUID getUserId() {
        return userId;
    }

    public boolean isAdmin() {
        return isAdmin;
    }
}

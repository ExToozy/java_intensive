package org.example.core.models;

import java.util.UUID;

/**
 *  ласс, представл€ющий пользовател€.
 * —одержит: идентификатор пользовател€, email, пароль и поле, определ€ющие €вл€етьс€ ли пользователь администатором.
 */
public class User {
    private UUID id;
    private String email;
    private String password;
    private boolean isAdmin;

    /**
     * —оздаЄт нового пользовател€.
     *
     * @param id       уникальный идентификатор пользовател€
     * @param email    электронна€ почта пользовател€
     * @param password пароль пользовател€
     * @param isAdmin  поле, определ€ющие €вл€етс€ ли пользователь администатором
     */
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

package org.example.core.models;

/**
 * Класс, представляющий пользователя.
 * Содержит: идентификатор пользователя, email, пароль и поле, определяющие являеться ли пользователь администатором.
 */
public class User {
    private int id;
    private String email;
    private String password;
    private boolean isAdmin;

    /**
     * Создаёт нового пользователя.
     *
     * @param id       уникальный идентификатор пользователя
     * @param email    электронная почта пользователя
     * @param password пароль пользователя
     * @param isAdmin  поле, определяющие является ли пользователь администатором
     */
    public User(int id, String email, String password, boolean isAdmin) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.isAdmin = isAdmin;
    }

    public int getId() {
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

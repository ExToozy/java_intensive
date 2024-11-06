package org.example.core.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Класс, представляющий пользователя.
 * Содержит: идентификатор пользователя, email, пароль и поле, определяющие являеться ли пользователь администатором.
 */
@Getter
@AllArgsConstructor
public class User {
    private final int id;
    private final String email;
    private final String password;
    private final boolean isAdmin;
}

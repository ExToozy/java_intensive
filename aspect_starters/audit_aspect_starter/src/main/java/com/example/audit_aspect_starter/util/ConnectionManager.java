package com.example.audit_aspect_starter.util;

import com.example.audit_aspect_starter.configs.DbConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Менеджер соединений для работы с базой данных.
 * Обеспечивает загрузку драйвера, инициализацию параметров
 * соединения и управление соединением с базой данных.
 */
@Component("auditConnectionManager")
@RequiredArgsConstructor
public class ConnectionManager implements AutoCloseable {

    private final DbConfig dbConfig;
    private Connection connection;


    /**
     * Открывает новое соединение с базой данных.
     *
     * @return объект Connection, представляющий соединение с базой данных
     * @throws SQLException если возникает ошибка при открытии соединения
     */
    public Connection open() throws SQLException {
        connection = DriverManager.getConnection(dbConfig.getUrl(), dbConfig.getUsername(), dbConfig.getPassword());
        return connection;
    }

    /**
     * Закрывает текущее соединение с базой данных.
     *
     * @throws SQLException если возникает ошибка при закрытии соединения
     */
    @Override
    public void close() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }
}

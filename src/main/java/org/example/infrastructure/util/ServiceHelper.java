package org.example.infrastructure.util;

import org.example.core.repositories.IHabitRepository;
import org.example.core.repositories.IHabitTrackRepository;
import org.example.core.repositories.IUserRepository;
import org.example.core.services.AuthService;
import org.example.core.services.HabitService;
import org.example.core.services.HabitTrackService;
import org.example.core.services.UserService;
import org.example.infrastructure.data.repositories.jdbc_repositories.JdbcHabitRepository;
import org.example.infrastructure.data.repositories.jdbc_repositories.JdbcHabitTrackRepository;
import org.example.infrastructure.data.repositories.jdbc_repositories.JdbcUserRepository;

/**
 * Утилита для получения экземпляров сервисов приложения.
 * Обеспечивает единый доступ к экземплярам сервисов и их зависимостям.
 */
public class ServiceHelper {

    private static final AuthService AUTH_SERVICE_INSTANCE;
    private static final HabitService HABIT_SERVICE_INSTANCE;
    private static final HabitTrackService HABIT_TRACK_SERVICE_INSTANCE;
    private static final UserService USER_SERVICE_INSTANCE;

    static {
        IUserRepository userRepository = new JdbcUserRepository();
        IHabitRepository habitRepository = new JdbcHabitRepository();
        IHabitTrackRepository habitTrackRepository = new JdbcHabitTrackRepository();

        HABIT_TRACK_SERVICE_INSTANCE = new HabitTrackService(habitTrackRepository, habitRepository);
        HABIT_SERVICE_INSTANCE = new HabitService(habitRepository, HABIT_TRACK_SERVICE_INSTANCE);
        USER_SERVICE_INSTANCE = new UserService(userRepository, HABIT_SERVICE_INSTANCE);
        AUTH_SERVICE_INSTANCE = new AuthService(USER_SERVICE_INSTANCE);
    }

    private ServiceHelper() {
    }

    /**
     * Получает экземпляр сервиса аутентификации.
     *
     * @return экземпляр AuthService
     */
    public static AuthService getAuthServiceInstance() {
        return AUTH_SERVICE_INSTANCE;
    }

    /**
     * Получает экземпляр сервиса управления привычками.
     *
     * @return экземпляр HabitService
     */
    public static HabitService getHabitServiceInstance() {
        return HABIT_SERVICE_INSTANCE;
    }

    /**
     * Получает экземпляр сервиса управления трекерами привычек.
     *
     * @return экземпляр HabitTrackService
     */
    public static HabitTrackService getHabitTrackServiceInstance() {
        return HABIT_TRACK_SERVICE_INSTANCE;
    }

    /**
     * Получает экземпляр сервиса управления пользователями.
     *
     * @return экземпляр UserService
     */
    public static UserService getUserServiceInstance() {
        return USER_SERVICE_INSTANCE;
    }
}

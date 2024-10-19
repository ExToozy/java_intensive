package org.example.presentation.console.handlers;

import org.example.core.models.Habit;
import org.example.core.repositories.habit_track_repository.dtos.CreateHabitTrackDto;
import org.example.infrastructure.controllers.console.ConsoleHabitController;
import org.example.infrastructure.controllers.console.ConsoleHabitTrackController;
import org.example.presentation.console.ActionManager;
import org.example.presentation.console.actions.HabitAction;
import org.example.presentation.console.in.ConsoleInHelper;
import org.example.presentation.console.out.ConsoleOutHelper;

import java.time.Period;

/**
 * Обработчик действий с привычками.
 * Обрабатывает действия, связанные с привычками, такие как завершение, обновление, удаление и отображение статистики.
 */
public class HabitActionHandler {
    private final ConsoleHabitTrackController habitTrackController;
    private final ConsoleHabitController habitController;
    private final ActionManager actionManager;

    /**
     * Конструктор HabitActionHandler.
     *
     * @param habitTrackController контроллер для работы с отслеживанием привычек
     * @param habitController      контроллер для работы с привычками
     * @param actionManager        менеджер действий
     */
    public HabitActionHandler(
            ConsoleHabitTrackController habitTrackController,
            ConsoleHabitController habitController,
            ActionManager actionManager
    ) {
        this.habitTrackController = habitTrackController;
        this.habitController = habitController;
        this.actionManager = actionManager;
    }

    /**
     * Обрабатывает действие с привычкой.
     *
     * @param action действие с привычкой
     * @param habit  привычка для выполнения действия
     */
    public void handleHabitAction(HabitAction action, Habit habit) {
        switch (action) {
            case COMPLETE:
                habitTrackController.completeHabit(new CreateHabitTrackDto(habit.getId()));
                break;
            case UPDATE:
                actionManager.manageUpdateHabitAction(habit);
                break;
            case DELETE:
                habitController.removeHabitAndTracks(habit.getId());
                break;
            case SHOW_COMPLETE_COUNT:
                Period period = ConsoleInHelper.getPeriodFromInput();
                int executionsCount = habitController.getHabitExecutionsCountByPeriod(habit, period);
                ConsoleOutHelper.printHabitExecutionCount(executionsCount);
                break;
            case EXIT:
                break;
        }
    }
}

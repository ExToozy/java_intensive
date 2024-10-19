package org.example.presentation.console.handlers;

import org.example.core.models.Habit;
import org.example.core.models.HabitFrequency;
import org.example.core.repositories.habit_repository.dtos.UpdateHabitDto;
import org.example.infrastructure.controllers.console.ConsoleHabitController;
import org.example.presentation.console.actions.HabitUpdateAction;
import org.example.presentation.console.in.ConsoleInHelper;
import org.example.presentation.console.out.ConsoleOutHelper;

/**
 * ќбработчик дл€ обновлени€ привычки.
 */
public class UpdateHabitActionHandler {
    private final ConsoleHabitController habitController;

    /**
     *  онструктор UpdateHabitActionHandler.
     *
     * @param habitController контроллер дл€ работы с привычками
     */
    public UpdateHabitActionHandler(ConsoleHabitController habitController) {
        this.habitController = habitController;
    }

    /**
     * ќбрабатывает действие обновлени€ привычки.
     *
     * @param habit привычка дл€ обновлени€
     */
    public void handleUpdateHabitAction(Habit habit) {
        boolean exitFlag = false;
        boolean saveFlag = false;
        String newName = habit.getName();
        String newDescription = habit.getDescription();
        HabitFrequency newFrequency = habit.getFrequency();
        while (!exitFlag) {
            ConsoleOutHelper.printHabitUpdateInfo(newName, newDescription, newFrequency);
            HabitUpdateAction action = ConsoleInHelper.getActionFromInput(HabitUpdateAction.class);
            switch (action) {
                case UPDATE_NAME:
                    newName = ConsoleOutHelper.getNewHabitNameFromInput();
                    break;
                case UPDATE_DESCRIPTION:
                    newDescription = ConsoleOutHelper.getNewHabitDescriptionFromInput();
                    break;
                case UPDATE_FREQUENCY:
                    newFrequency = ConsoleOutHelper.getNewHabitFrequencyFromInput();
                    break;
                case SAVE:
                    saveFlag = true;
                    exitFlag = true;
                    break;
                case CANCEL:
                    exitFlag = true;
                    break;
            }
        }
        if (saveFlag) {
            habitController.updateHabit(new UpdateHabitDto(habit.getId(), newName, newDescription, newFrequency));
        }
    }
}

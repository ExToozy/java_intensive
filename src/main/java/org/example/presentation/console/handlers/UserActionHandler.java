package org.example.presentation.console.handlers;

import org.example.core.models.Habit;
import org.example.core.models.User;
import org.example.core.repositories.habit_repository.dtos.CreateHabitDto;
import org.example.infastructure.controllers.console.ConsoleHabitController;
import org.example.infastructure.controllers.console.ConsoleHabitTrackController;
import org.example.infastructure.controllers.console.ConsoleUserController;
import org.example.presentation.console.ActionManager;
import org.example.presentation.console.actions.UserAction;
import org.example.presentation.console.common.HabitsShowFilter;
import org.example.presentation.console.in.ConsoleInHelper;
import org.example.presentation.console.out.ConsoleOutHelper;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * ���������� �������� ������������.
 * ������������ ��������, ��������� � �������������, ����� ��� �������� �������� � ����������� ����������.
 */
public class UserActionHandler {
    private final ConsoleHabitController habitController;
    private final ActionManager actionManager;
    private final ConsoleUserController userController;
    private final ConsoleHabitTrackController habitTrackController;

    /**
     * ����������� UserActionHandler.
     *
     * @param habitController      ���������� ��� ������ � ����������
     * @param actionManager        �������� ��������
     * @param userController       ���������� ��� ������ � ��������������
     * @param habitTrackController ���������� ��� ������ � ������������� ��������
     */
    public UserActionHandler(
            ConsoleHabitController habitController,
            ActionManager actionManager,
            ConsoleUserController userController,
            ConsoleHabitTrackController habitTrackController
    ) {
        this.habitController = habitController;
        this.actionManager = actionManager;
        this.userController = userController;
        this.habitTrackController = habitTrackController;
    }

    /**
     * ������������ �������� ������������.
     *
     * @param user       ������������, ����������� ��������
     * @param userAction �������� ������������
     * @return ���� ������
     */
    public boolean handleUserAction(User user, UserAction userAction) {
        switch (userAction) {
            case CREATE_HABIT:
                CreateHabitDto habitDto = ConsoleInHelper.getCreateHabitDtoFromInput(user);
                habitController.createHabit(habitDto);
                break;
            case SHOW_HABITS:
                return handleUserShowHabitsAction(user);
            case SHOW_STATISTICS:
                Map<Habit, Map<String, Integer>> statistic = habitController.getHabitStatistics(user.getId());
                ConsoleOutHelper.printAllHabitStats(statistic);
                break;
            case DELETE:
                userController.deleteUser(user.getId());
                return true;
            case UPDATE:
                return actionManager.manageUpdateUserAction(user);
            case EXIT:
                return true;
        }
        return false;
    }

    /**
     * ������������ ����� �������� ������������.
     *
     * @param user ������������, ��� �������� ����� ��������
     * @return ���� ������
     */
    private boolean handleUserShowHabitsAction(User user) {
        ConsoleOutHelper.printEnumWithNumbers(HabitsShowFilter.class);
        var filter = ConsoleInHelper.getEnumConstantFromInput(HabitsShowFilter.class);
        var habits = getUserHabitsByFilter(user, filter);
        printHabits(habits);
        if (habits.isEmpty()) {
            ConsoleOutHelper.printHabitsListIsEmpty();
            return true;
        }
        int habitIndex = ConsoleInHelper.getValidatedHabitIndex(habits);
        actionManager.manageHabitAction(habits.get(habitIndex));
        return false;
    }

    /**
     * �������� �������� ������������ �� �������.
     *
     * @param user   ������������, ��� �������� ����� ��������
     * @param filter ������ ��� ��������� ��������
     * @return ������ �������� ������������
     */
    private List<Habit> getUserHabitsByFilter(User user, HabitsShowFilter filter) {
        List<Habit> habits = new ArrayList<>();
        switch (filter) {
            case ALL -> habits = habitController.getUserHabits(user.getId());
            case COMPLETED -> habits = habitController.getUserHabitsByCompleteStatus(user.getId(), true);
            case NOT_COMPLETED -> habits = habitController.getUserHabitsByCompleteStatus(user.getId(), false);
        }
        return habits;
    }

    /**
     * �������� ��������.
     *
     * @param habits ������ �������� ��� ������
     */
    private void printHabits(List<Habit> habits) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        for (int i = 0; i < habits.size(); i++) {
            var habit = habits.get(i);
            String deadlineDay = habitController.getHabitDeadlineDay(habit).format(formatter);
            boolean isCompleted = habitTrackController.isCompleteHabit(habit);
            ConsoleOutHelper.printHabitInlineInfo(i, habit, isCompleted, deadlineDay);
        }
    }
}

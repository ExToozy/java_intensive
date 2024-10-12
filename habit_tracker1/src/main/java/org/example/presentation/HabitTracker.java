package org.example.presentation;

import org.example.core.exceptions.InvalidEmail;
import org.example.core.exceptions.UserAlreadyExistException;
import org.example.core.exceptions.UserNotFoundException;
import org.example.core.models.Habit;
import org.example.core.models.HabitFrequency;
import org.example.core.models.User;
import org.example.core.repositories.habit_repository.dtos.CreateHabitDto;
import org.example.core.repositories.habit_repository.dtos.UpdateHabitDto;
import org.example.core.repositories.habit_track_repository.dtos.CreateHabitTrackDto;
import org.example.core.repositories.user_repository.dtos.ChangeAdminStatusDto;
import org.example.core.repositories.user_repository.dtos.CreateUserDto;
import org.example.infastructure.controllers.console.ConsoleAuthController;
import org.example.infastructure.controllers.console.ConsoleHabitController;
import org.example.infastructure.controllers.console.ConsoleHabitTrackController;
import org.example.infastructure.controllers.console.ConsoleUserController;
import org.example.presentation.actions.*;
import org.example.presentation.common.HabitsShowFilter;
import org.example.presentation.common.StatisticPeriod;
import org.example.presentation.in.ConsoleInHelper;
import org.example.presentation.out.ConsoleOutHelper;

import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HabitTracker {
    private final ConsoleUserController userController;
    private final ConsoleAuthController authController;
    private final ConsoleHabitController habitController;
    private final ConsoleHabitTrackController habitTrackController;

    public HabitTracker(
            ConsoleUserController userController,
            ConsoleAuthController authController,
            ConsoleHabitController habitController,
            ConsoleHabitTrackController habitTrackController
    ) {
        this.userController = userController;
        this.authController = authController;
        this.habitController = habitController;
        this.habitTrackController = habitTrackController;
    }

    public void run() {
        do {
            User user = runAuthLoop();
            if (user.isAdmin()) {
                runAdminActionsLoop(user);
            } else {
                runActionsLoop(user);
            }
        } while (getExitAnswer());
        ConsoleOutHelper.printMessage("Thank you for being with us");
    }

    private void runAdminActionsLoop(User user) {
        boolean exitFlag = false;
        while (!exitFlag) {
            AdminAction action = getActionFromInput(AdminAction.class);
            exitFlag = handleAdminAction(user, action);
        }
    }

    private boolean handleAdminAction(User user, AdminAction action) {
        if (action == AdminAction.OPEN_HABIT_TRACKER) {
            runActionsLoop(user);
        } else if (action == AdminAction.SHOW_USERS) {
            List<User> users = userController.getAll();
            printUsers(users);
            ConsoleOutHelper.printMessage("Enter user number to open it");
            int userIndex = getValidatedIndex(users.size());
            return openUser(users.get(userIndex), user);
        } else {
            return true;
        }
        return false;
    }

    private boolean openUser(User openedUser, User currentUser) {
        AdminActionOnUser action = getActionFromInput(AdminActionOnUser.class);
        return handleAdminActionOnUser(openedUser, action, currentUser);
    }

    private boolean handleAdminActionOnUser(User openedUser, AdminActionOnUser action, User currentUser) {
        switch (action) {
            case OPEN_HABITS -> handleUserAction(openedUser, UserAction.SHOW_HABITS);
            case CHANGE_ADMIN_STATUS -> {
                try {
                    userController.changeUserAdminStatus(new ChangeAdminStatusDto(openedUser.getId(), !openedUser.isAdmin()));
                    return openedUser.getId().equals(currentUser.getId());
                } catch (UserNotFoundException e) {
                    ConsoleOutHelper.printMessage("An error occurred while changing the status. The user may have already been deleted");
                }
            }
            case DELETE -> {
                userController.deleteUser(openedUser.getId());
                return openedUser.getId().equals(currentUser.getId());
            }
            case EXIT -> {
                return true;
            }
        }
        return false;
    }

    private void printUsers(List<User> users) {
        for (int i = 0, usersSize = users.size(); i < usersSize; i++) {
            var user = users.get(i);
            String isAdminStr = user.isAdmin() ? "(Administrator)" : "";
            ConsoleOutHelper.printMessage(String.format("%d %s %s", i + 1, user.getEmail(), isAdminStr));
        }
    }

    private User runAuthLoop() {
        User user = null;
        do {
            try {
                user = authOrRegisterUser();
            } catch (UserNotFoundException e) {
                ConsoleOutHelper.printMessage("User with these credentials not found");
            } catch (UserAlreadyExistException e) {
                ConsoleOutHelper.printMessage("A user with such emails already exists");
            } catch (InvalidEmail e) {
                ConsoleOutHelper.printMessage("Please enter a valid email");
            }
        } while (user == null);
        return user;
    }

    private void runActionsLoop(User user) {
        boolean exitFlag = false;
        while (!exitFlag) {
            UserAction userAction = getActionFromInput(UserAction.class);
            exitFlag = handleUserAction(user, userAction);
        }
    }

    private User authOrRegisterUser() throws UserNotFoundException, UserAlreadyExistException, InvalidEmail {
        AuthAction action = getActionFromInput(AuthAction.class);
        return handleAuthUserAction(action);
    }

    private boolean getExitAnswer() {
        ConsoleOutHelper.printMessage("Go to login page? y/n");
        while (true) {
            String answer = ConsoleInHelper.readLine();
            if (answer.equalsIgnoreCase("n")) {
                return false;
            } else if (answer.equalsIgnoreCase("y")) {
                return true;
            }
            System.out.println("Incorrect input, please try again. Write y/n");
        }
    }

    private <T extends Enum<T>> T getActionFromInput(Class<T> actionEnum) {
        ConsoleOutHelper.printMessage("Enter action number");
        printEnumWithNumbers(actionEnum);
        return getEnumConstantFromInput(actionEnum);
    }


    private int readInt() {
        while (true) {
            try {
                return ConsoleInHelper.readInt();
            } catch (NumberFormatException e) {
                ConsoleOutHelper.printMessage("You type an incorrect number, please try again.");
            }
        }
    }

    private boolean handleUserAction(User user, UserAction userAction) {
        if (userAction == UserAction.CREATE_HABIT) {
            createHabit(user);
        } else if (userAction == UserAction.SHOW_HABITS) {
            printEnumWithNumbers(HabitsShowFilter.class);
            var filter = getEnumConstantFromInput(HabitsShowFilter.class);

            var habits = getUserHabitsByFilter(user, filter);
            printHabits(habits);

            if (habits.isEmpty()) {
                ConsoleOutHelper.printMessage("There are no habits here yet");
                return false;
            }
            ConsoleOutHelper.printMessage("Enter the habit number to open it");
            int habitIndex = getValidatedIndex(habits.size());
            openHabit(habitIndex, habits);
        } else if (userAction == UserAction.SHOW_STATISTICS) {
            showHabitStatistics(user);
        }
        return userAction == UserAction.EXIT;
    }

    private List<Habit> getUserHabitsByFilter(User user, HabitsShowFilter filter) {
        List<Habit> habits = new ArrayList<>();
        switch (filter) {
            case ALL -> habits = habitController.getUserHabits(user.getId());
            case COMPLETED -> habits = habitController.getUserHabitsByCompleteStatus(user.getId(), true);
            case NOT_COMPLETED -> habits = habitController.getUserHabitsByCompleteStatus(user.getId(), false);
        }
        return habits;
    }

    private int getValidatedIndex(int listLength) {
        while (true) {
            int inputToValidate = readInt() - 1;
            if (inputToValidate >= 0 && inputToValidate < listLength) {
                return inputToValidate;
            }
            ConsoleOutHelper.printMessage("There is no such number, try again");
        }
    }

    private void showHabitStatistics(User user) {
        ConsoleOutHelper.printMessage("Statistics for all habits:");
        Map<Habit, Map<String, Integer>> statistic = habitController.getHabitStatistics(user.getId());
        for (var habitEntrySet : statistic.entrySet()) {
            Map<String, Integer> habitStatistics = habitEntrySet.getValue();
            ConsoleOutHelper.printMessage(habitEntrySet.getKey().getName());
            ConsoleOutHelper.printMessage(String.format("\t Total number of executions: %s", habitStatistics.get("track_count")));
            ConsoleOutHelper.printMessage(String.format("\t Completion percentage: %s", habitStatistics.get("completion_percent")));
            ConsoleOutHelper.printMessage(String.format("\t Current streak: %s", habitStatistics.get("current_streak")));
        }
    }

    private User handleAuthUserAction(AuthAction action) throws UserNotFoundException, UserAlreadyExistException, InvalidEmail {
        ConsoleOutHelper.printMessage("Enter email:");
        String login = ConsoleInHelper.readLine();
        ConsoleOutHelper.printMessage("Enter password:");
        String password = ConsoleInHelper.readLine();
        CreateUserDto dto = new CreateUserDto(login, password);
        return switch (action) {
            case LOGIN -> authController.login(dto);
            case REGISTER -> authController.register(dto);
        };
    }

    private void createHabit(User user) {
        ConsoleOutHelper.printMessage("Enter the name:");
        String name = ConsoleInHelper.readLine();
        ConsoleOutHelper.printMessage("Enter the description:");
        String description = ConsoleInHelper.readLine();
        ConsoleOutHelper.printMessage("How often should it be done?:");
        HabitFrequency frequency = getFrequencyFromInput();
        habitController.createHabit(new CreateHabitDto(user.getId(), name, description, frequency));
    }

    private void printHabits(List<Habit> habits) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        for (int i = 0; i < habits.size(); i++) {
            var habit = habits.get(i);
            boolean isCompleted = habitTrackController.isCompleteHabit(habit);
            String completeStr = isCompleted ? "[completed]" : "[not completed]";
            String habitInfo;
            if (isCompleted) {
                habitInfo = String.format("%d. %s %s ", i + 1, habit.getName(), completeStr);
            } else {
                String deadlineDay = habitController.getHabitDeadlineDay(habit).format(formatter);
                habitInfo = String.format("%d. %s %s must be completed before %s", i + 1, habit.getName(), completeStr, deadlineDay);
            }
            ConsoleOutHelper.printMessage(habitInfo);
        }
    }

    private void openHabit(int habitIndex, List<Habit> habits) {
        Habit habit = habits.get(habitIndex);
        ConsoleOutHelper.printMessage(String.format("Name: %s", habit.getName()));
        ConsoleOutHelper.printMessage(String.format("Description: %s", habit.getDescription()));
        ConsoleOutHelper.printMessage(String.format("It need to do: %s", habit.getFrequency()));
        ConsoleOutHelper.printMessage(String.format("Current streak: %s", habitController.getHabitStreak(habit)));
        HabitAction action = getActionFromInput(HabitAction.class);
        handleHabitAction(action, habit);
    }

    private void handleHabitAction(HabitAction action, Habit habit) {
        switch (action) {
            case COMPLETE -> habitTrackController.completeHabit(new CreateHabitTrackDto(habit.getId()));
            case UPDATE -> updateHabitLoop(habit);
            case DELETE -> habitController.removeHabitAndTracks(habit.getId());
            case SHOW_COMPLETE_COUNT -> showCompleteHabitCountByPeriod(habit);
            case EXIT -> {
                // just return from method
            }
        }
    }

    private void showCompleteHabitCountByPeriod(Habit habit) {
        printEnumWithNumbers(StatisticPeriod.class);
        StatisticPeriod periodEnum = getEnumConstantFromInput(StatisticPeriod.class);
        Period period;
        switch (periodEnum) {
            case DAY -> period = Period.ofDays(1);
            case MONTH -> period = Period.ofMonths(1);
            case WEEK -> period = Period.ofWeeks(1);
            case YEAR -> period = Period.ofYears(1);
            default -> {
                return;
            }
        }
        int executionsCount = habitController.getHabitExecutionsCountByPeriod(habit, period);
        ConsoleOutHelper.printMessage(String.format("During this period, the habit was performed: %d times", executionsCount));
    }

    private <E extends Enum<E>> E getEnumConstantFromInput(Class<E> enumClass) {
        E[] enumConstants = enumClass.getEnumConstants();
        int ordinal = getValidatedIndex(enumConstants.length);
        return enumConstants[ordinal];
    }

    private void updateHabitLoop(Habit habit) {
        boolean exitFlag = false;
        String newName = habit.getName();
        String newDescription = habit.getDescription();
        HabitFrequency newFrequency = habit.getFrequency();
        while (!exitFlag) {
            printHabitUpdateInfo(newName, newDescription, newFrequency);
            HabitUpdateAction action = getActionFromInput(HabitUpdateAction.class);
            switch (action) {
                case UPDATE_NAME -> {
                    ConsoleOutHelper.printMessage("Enter a new name:");
                    newName = ConsoleInHelper.readLine();
                }
                case UPDATE_DESCRIPTION -> {
                    ConsoleOutHelper.printMessage("Enter a new description:");
                    newDescription = ConsoleInHelper.readLine();
                }
                case UPDATE_FREQUENCY -> {
                    ConsoleOutHelper.printMessage("Select a new frequency:");
                    newFrequency = getFrequencyFromInput();
                }
                case SAVE -> {
                    habitController.updateHabit(new UpdateHabitDto(habit.getId(), newName, newDescription, newFrequency));
                    exitFlag = true;
                }
                case CANCEL -> exitFlag = true;
            }
        }
    }

    private HabitFrequency getFrequencyFromInput() {
        printEnumWithNumbers(HabitFrequency.class);
        return getEnumConstantFromInput(HabitFrequency.class);
    }

    private <E extends Enum<E>> void printEnumWithNumbers(Class<E> enumClass) {
        E[] enumValues = enumClass.getEnumConstants();
        for (var value : enumValues) {
            ConsoleOutHelper.printMessage(String.format("%d. %s", value.ordinal() + 1, value));
        }
    }

    private void printHabitUpdateInfo(String newName, String newDescription, HabitFrequency newFrequency) {
        ConsoleOutHelper.printMessage(String.format("Current name: %s", newName));
        ConsoleOutHelper.printMessage(String.format("Current description: %s", newDescription));
        ConsoleOutHelper.printMessage(String.format("Current frequency: %s", newFrequency));
    }
}

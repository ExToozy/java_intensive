package org.example.presentation.console.out;

import org.example.core.models.Habit;
import org.example.core.models.HabitFrequency;
import org.example.core.models.User;
import org.example.presentation.console.in.ConsoleInHelper;

import java.util.List;
import java.util.Map;

public class ConsoleOutHelper {
    private ConsoleOutHelper() {
    }

    public static void printUsers(List<User> users) {
        for (int i = 0, usersSize = users.size(); i < usersSize; i++) {
            var user = users.get(i);
            String isAdminStr = user.isAdmin() ? "(Administrator)" : "";
            ConsoleOutHelper.printMessage(String.format("%d %s %s", i + 1, user.getEmail(), isAdminStr));
        }
    }

    public static void printMessage(String message) {
        System.out.println(message);
    }

    public static <E extends Enum<E>> void printEnumWithNumbers(Class<E> enumClass) {
        E[] enumValues = enumClass.getEnumConstants();
        for (var value : enumValues) {
            ConsoleOutHelper.printMessage(String.format("%d. %s", value.ordinal() + 1, value));
        }
    }

    public static void printHabitUpdateInfo(String newName, String newDescription, HabitFrequency newFrequency) {
        ConsoleOutHelper.printMessage(String.format("Current name: %s", newName));
        ConsoleOutHelper.printMessage(String.format("Current description: %s", newDescription));
        ConsoleOutHelper.printMessage(String.format("Current frequency: %s", newFrequency));
    }

    public static void printHabitInlineInfo(int lineIndex, Habit habit, boolean isCompleted, String deadlineDay) {
        String completeStr = isCompleted ? "[completed]" : "[not completed]";
        String habitInfo;
        if (isCompleted) {
            habitInfo = String.format("%d. %s %s ", lineIndex + 1, habit.getName(), completeStr);
        } else {
            habitInfo = String.format("%d. %s %s must be completed before %s", lineIndex + 1, habit.getName(), completeStr, deadlineDay);
        }
        ConsoleOutHelper.printMessage(habitInfo);
    }

    public static void printHabitInfo(Habit habit, int habitStreak) {
        ConsoleOutHelper.printMessage(String.format("Name: %s", habit.getName()));
        ConsoleOutHelper.printMessage(String.format("Description: %s", habit.getDescription()));
        ConsoleOutHelper.printMessage(String.format("It need to do: %s", habit.getFrequency().toStringRepresentation()));
        ConsoleOutHelper.printMessage(String.format("Current streak: %s", habitStreak));
    }

    public static void printAllHabitStats(Map<Habit, Map<String, Integer>> statistic) {
        ConsoleOutHelper.printMessage("Statistics for all habits:");
        for (var habitEntrySet : statistic.entrySet()) {
            Map<String, Integer> habitStatistics = habitEntrySet.getValue();
            printHabitStatistics(habitEntrySet.getKey(), habitStatistics);
        }
    }

    private static void printHabitStatistics(Habit habit, Map<String, Integer> habitStatistics) {
        ConsoleOutHelper.printMessage(habit.getName());
        ConsoleOutHelper.printMessage(String.format("\t Total number of executions: %s", habitStatistics.get("track_count")));
        ConsoleOutHelper.printMessage(String.format("\t Completion percentage: %s", habitStatistics.get("completion_percent")));
        ConsoleOutHelper.printMessage(String.format("\t Current streak: %s", habitStatistics.get("current_streak")));
    }

    public static void printHabitExecutionCount(int executionsCount) {
        ConsoleOutHelper.printMessage(String.format("During this period, the habit was performed: %d times", executionsCount));
    }

    public static String getUserEmailFromInput() {
        ConsoleOutHelper.printMessage("Write a new email");
        return ConsoleInHelper.readLine();
    }

    public static HabitFrequency getNewHabitFrequencyFromInput() {
        HabitFrequency newFrequency;
        ConsoleOutHelper.printMessage("Select a new frequency:");
        newFrequency = ConsoleInHelper.getFrequencyFromInput();
        return newFrequency;
    }

    public static String getNewHabitDescriptionFromInput() {
        String newDescription;
        ConsoleOutHelper.printMessage("Enter a new description:");
        newDescription = ConsoleInHelper.readLine();
        return newDescription;
    }

    public static String getNewHabitNameFromInput() {
        String newName;
        ConsoleOutHelper.printMessage("Enter a new name:");
        newName = ConsoleInHelper.readLine();
        return newName;
    }

    public static void printHabitsListIsEmpty() {
        ConsoleOutHelper.printMessage("There are no habits here yet");
    }


}


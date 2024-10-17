package org.example.presentation.console.in;

import org.example.core.models.Habit;
import org.example.core.models.HabitFrequency;
import org.example.core.models.User;
import org.example.core.repositories.habit_repository.dtos.CreateHabitDto;
import org.example.core.repositories.user_repository.dtos.CreateUserDto;
import org.example.presentation.console.common.StatisticPeriod;
import org.example.presentation.console.out.ConsoleOutHelper;

import java.time.Period;
import java.util.List;
import java.util.Scanner;

public class ConsoleInHelper {
    private static final Scanner scanner = new Scanner(System.in);

    private ConsoleInHelper() {
    }

    public static CreateHabitDto getCreateHabitDtoFromInput(User user) {
        ConsoleOutHelper.printMessage("Enter the name:");
        String name = ConsoleInHelper.readLine();
        ConsoleOutHelper.printMessage("Enter the description:");
        String description = ConsoleInHelper.readLine();
        ConsoleOutHelper.printMessage("How often should it be done?:");
        HabitFrequency frequency = getFrequencyFromInput();
        return new CreateHabitDto(user.getId(), name, description, frequency);
    }

    public static String readLine() {
        return scanner.nextLine();
    }

    public static HabitFrequency getFrequencyFromInput() {
        ConsoleOutHelper.printEnumWithNumbers(HabitFrequency.class);
        return ConsoleInHelper.getEnumConstantFromInput(HabitFrequency.class);
    }

    public static <E extends Enum<E>> E getEnumConstantFromInput(Class<E> enumClass) {
        E[] enumConstants = enumClass.getEnumConstants();
        int ordinal = getValidatedIndex(enumConstants.length);
        return enumConstants[ordinal];
    }

    public static int getValidatedIndex(int listLength) {
        while (true) {
            int inputToValidate = readInt() - 1;
            if (inputToValidate >= 0 && inputToValidate < listLength) {
                return inputToValidate;
            }
            ConsoleOutHelper.printMessage("There is no such number, try again");
        }
    }

    private static int readInt() {
        while (true) {
            try {
                return Integer.parseInt(readLine());
            } catch (NumberFormatException e) {
                ConsoleOutHelper.printMessage("You type an incorrect number, please try again.");
            }
        }
    }

    public static Period getPeriodFromInput() {
        ConsoleOutHelper.printEnumWithNumbers(StatisticPeriod.class);
        StatisticPeriod periodEnum = ConsoleInHelper.getEnumConstantFromInput(StatisticPeriod.class);
        Period period;
        switch (periodEnum) {
            case DAY -> period = Period.ofDays(1);
            case MONTH -> period = Period.ofMonths(1);
            case WEEK -> period = Period.ofWeeks(1);
            case YEAR -> period = Period.ofYears(1);
            default -> {
                return null;
            }
        }
        return period;
    }

    public static <T extends Enum<T>> T getActionFromInput(Class<T> actionEnum) {
        ConsoleOutHelper.printMessage("Enter action number");
        ConsoleOutHelper.printEnumWithNumbers(actionEnum);
        return ConsoleInHelper.getEnumConstantFromInput(actionEnum);
    }

    public static boolean getExitAnswer() {
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

    public static CreateUserDto getCreateUserDtoFromInput() {
        ConsoleOutHelper.printMessage("Enter email:");
        String login = ConsoleInHelper.readLine();
        ConsoleOutHelper.printMessage("Enter password:");
        String password = ConsoleInHelper.readLine();
        return new CreateUserDto(login, password);
    }

    public static int getUserIndexFromInput(List<User> users) {
        ConsoleOutHelper.printMessage("Enter user number to open it");
        return ConsoleInHelper.getValidatedIndex(users.size());
    }

    public static String getUserPasswordFromInput() {
        ConsoleOutHelper.printMessage("Write a new password");
        return ConsoleInHelper.readLine();
    }

    public static int getValidatedHabitIndex(List<Habit> habits) {
        ConsoleOutHelper.printMessage("Enter the habit number to open it");
        return ConsoleInHelper.getValidatedIndex(habits.size());
    }


}

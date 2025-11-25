package org.example;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Scanner;

public class InputResolver {
    private static final Scanner scanner = new Scanner(System.in);
    private static final File DEFAULT_FOLDER = new File("./data");
    public static Input resolve(String[] args) {
        File folder;
        String parameter;
        if (args.length == 2) {
            folder = validateFolder(args[0]);
            parameter = validateParameter(args[1]);
            return new Input(folder, parameter);
        }
        System.out.println("Command line arguments not provided");
        System.out.println("Use default folder: " + DEFAULT_FOLDER.getAbsolutePath() + " ? (y/n)");
        String answer = scanner.nextLine().trim().toLowerCase();
        boolean yes = answer.equals("y") || answer.equals("yes");
        if (yes) {
            folder = validateFolder(DEFAULT_FOLDER.getAbsolutePath());
            System.out.println("Select parameter by number: ");
            Field[] fields = CNCMachine.class.getFields();
            for (int i = 0; i < fields.length; i++) {
                System.out.println((i + 1) + ". " + fields[i].getName());
            }
            int choice = getDefaultParameterNumber(1, fields.length);
            parameter = fields[choice - 1].getName();
        } else {
            System.out.print("Enter folder path: ");
            folder = validateFolder(scanner.nextLine().trim());
            System.out.println("Enter parameter name: ");
            parameter = validateParameter(scanner.nextLine().trim());
        }
        return new Input(folder, parameter);
    }
    private static File validateFolder(String path) {
        if (path == null || path.isBlank()) {
            throw new IllegalArgumentException("Folder path cannot be empty");
        }
        File folder = new File(path);
        if (!folder.exists() || !folder.isDirectory()) {
            throw new IllegalArgumentException("Folder does not exist or is not a directory: " + path);
        }
        return folder;
    }
    private static String validateParameter(String parameter) {
        if (parameter == null || parameter.isBlank()) {
            throw new IllegalArgumentException("Parameter cannot be empty");
        }
        return parameter;
    }
    private static int getDefaultParameterNumber(int min, int max) {
        while (true) {
            String line = scanner.nextLine().trim();
            try {
                int value = Integer.parseInt(line);
                if (value >= min && value <= max) {
                    return value;
                }
            } catch (NumberFormatException ignored) {}
            System.out.println("Enter a number between " + min + " and " + max);
        }
    }

}
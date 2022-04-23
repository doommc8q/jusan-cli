package myFS;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;

// add Exceptions
class MyFile {
    private static final String LS_COMMAND = "ls";
    private static final String LS_PY_COMMAND = "ls_py";
    private static final String IS_DIR_COMMAND = "is_dir";
    private static final String DEFINE_COMMAND = "define";
    private static final String READMOD_COMMAND = "readmod";
    private static final String SETMOD_COMMAND = "setmod";
    private static final String CAT_COMMAND = "cat";
    private static final String APPEND_COMMAND = "append";
    private static final String BC_COMMAND = "bc";
    private static final String GREPLONG_COMMAND = "greplong";
    private static final String HELP_COMMAND = "help";
    private static final String EXIT_COMMAND = "exit";

    private static final String PERMISSION_EXCEPTION_FORMAT = "format of permission should be : rwx, rw-, r-x ...";

    // выводит список всех файлов и директорий для path - ls
    public static void listDirectory(String path) {
        try {
            File file = new File(path);
            if (!file.isDirectory() || !file.exists()) {
                throw new Exception("incorrect directory path");
            }
            File[] listOfFiles = file.listFiles();

            if (listOfFiles != null) {
                for (int i = 0; i < listOfFiles.length; i++) {
                    if (listOfFiles.length - 1 == i) {
                        System.out.println(listOfFiles[i].getName());
                    } else {
                        System.out.print(listOfFiles[i].getName() + " ");
                    }
                }
            }
        } catch (Exception e) {
            System.out.printf("%s\n", e);
        }
    }

    // выводит список файлов с расширением .py в path - ls_py
    public static void listPythonFiles(String path) {
        try {
            File file = new File(path);
            if (!file.exists()) {
                throw new Exception("incorrect file path");
            }
            File[] listOfFiles = file.listFiles();
            ArrayList<String> listOfPythonFile = new ArrayList<>();
            if (listOfFiles != null) {
                for (File listOfFile : listOfFiles) {
                    if (listOfFile.isFile()) {
                        if (listOfFile.getName().endsWith(".py")) {
                            listOfPythonFile.add(listOfFile.getName());
                        }
                    }
                }
            }
            if (listOfPythonFile.size() == 0) {
                throw new Exception("no such type of file");
            }
            for (int i = 0; i < listOfPythonFile.size(); i++) {
                if (listOfPythonFile.size() - 1 == i) {
                    System.out.println(listOfPythonFile.get(i));
                } else {
                    System.out.print(listOfPythonFile.get(i) + " ");
                }
            }
        } catch (Exception e) {
            System.out.printf("%s\n", e);
        }
    }

    // выводит true, если path это директория, в других случаях false - id_dir
    public static void isDirectory(String path) {
        try {
            File file = new File(path);
            if (!file.isDirectory() || !file.exists()) {
                throw new Exception("incorrect file path");
            }
            System.out.println(file.isDirectory());
        } catch (Exception e) {
            System.out.printf("%s\n", e);
        }
    }


    // выводит директория или файл в зависимости от типа path - define
    public static void define(String path) {
        try {
            File file = new File(path);
            if (!file.exists()) {
                throw new Exception("incorrect file or directory path");
            }
            BasicFileAttributes basicFileAttributes = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
            if (basicFileAttributes.isDirectory()) {
                System.out.println("This is a Directory");
            } else if (basicFileAttributes.isRegularFile()) {
                System.out.println("This id a File");
            } else {
                System.out.println("Wrong path");
            }
        } catch (Exception e) {
            System.out.printf("%s\n", e);
        }
    }

    // выводит права для файла в формате rwx для текущего пользователя - readmod
    public static void printPermissions(String path) {
        try {
            File file = new File(path);
            if (!file.isFile() || !file.exists()) {
                throw new Exception("incorrect file or directory path");
            }
            filePermissionChecker(file.canRead(), "r");
            filePermissionChecker(file.canWrite(), "w");
            filePermissionChecker(file.canExecute(), "x");
            System.out.println();

        } catch (Exception e) {
            System.out.printf("%s\n", e);
        }
    }

    // устанавливает права для файла path - setmod
    public static void setPermissions(String path, String permissions) {
        try {
            File file = new File(path);
            if (!file.isFile() || !file.exists()) {
                throw new Exception("incorrect file or directory path");
            }
            char ch0 = permissions.charAt(0);
            char ch1 = permissions.charAt(1);
            char ch2 = permissions.charAt(2);

            ifCaseCheckerWithException(permissions.length() > 3, PERMISSION_EXCEPTION_FORMAT);
            ifCaseCheckerWithException(ch0 != '-' && ch0 != 'r', PERMISSION_EXCEPTION_FORMAT);
            ifCaseCheckerWithException(ch1 != '-' && ch1 != 'w', PERMISSION_EXCEPTION_FORMAT);
            ifCaseCheckerWithException(ch2 != '-' && ch2 != 'x', PERMISSION_EXCEPTION_FORMAT);

            file.setReadable(ch0 != '-');
            file.setWritable(ch1 != '-');
            file.setExecutable(ch2 != '-');

        } catch (Exception e) {
            System.out.printf("%s\n", e);
        }
    }

    // выводит контент файла - cat
    public static void printContent(String path) {
        try {
            File file = new File(path);
            if (!file.isFile() || !file.exists()) {
                throw new Exception("incorrect file path");
            }
            try (Scanner obj = new Scanner(file)) {
                while (obj.hasNextLine()) {
                    System.out.println(obj.nextLine());
                }
            } catch (Exception e) {
                System.out.printf("%s\n", e);
            }
        } catch (Exception e) {
            System.out.printf("%s\n", e);
        }
    }

    // добавляет строке # Autogenerated line в конец path - append
    public static void appendFooter(String path) {
        try {
            File file = new File(path);
            if (!file.isFile() || !file.exists()) {
                throw new Exception("incorrect file path");
            }
            FileWriter fw = new FileWriter(file, true);
            fw.append("\n# Autogenerated line");
            fw.close();
        } catch (Exception e) {
            System.out.printf("%s\n", e);
        }
    }


    // создает копию path в директорию /tmp/${date}.backup где, date - это дата
    // в формате dd-mm-yyyy. path может быть директорией или файлом. При
    // директории, копируется весь контент. - bc
    public static void createBackup(String path) {
        try {
            File file = new File(path);
            if (!file.exists()) {
                throw new Exception("incorrect file or directory path");
            }
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
            Calendar calendar = Calendar.getInstance();
            File newBackupFile = new File("/tmp/" + simpleDateFormat.format(calendar.getTime()) + ".backup");
            if (newBackupFile.exists()) {
                deleteDir(newBackupFile);
            }
            Files.copy(Path.of(file.getPath()), Path.of(newBackupFile.getPath()));
            File[] listOfFiles = file.listFiles();
            if (listOfFiles != null) {
                for (File listOfFile : listOfFiles) {
                    try {
                        Files.copy(Path.of(listOfFile.getPath()), Path.of(newBackupFile.getPath() +
                                "/" + listOfFile.getName()));
                    } catch (Exception e) {
                        System.out.printf("%s\n", e);
                    }
                }
            }
        } catch (Exception e) {
            System.out.printf("%s\n", e);
        }
    }

    // выводит самое длинное слово в файле - greplong
    public static void printLongestWord(String path) {
        try {
            File file = new File(path);
            if (!file.isFile() || !file.exists()) {
                throw new Exception("incorrect filepath");
            }
            try (Scanner scanner = new Scanner(file)) {
                String longestWord = "";
                String currentWord;

                while (scanner.hasNext()) {
                    currentWord = scanner.next();
                    if (currentWord.length() > longestWord.length()) {
                        longestWord = currentWord;
                    }
                }

                if (longestWord.length() == 0) {
                    throw new Exception("file is empty");
                }
                System.out.println(longestWord);
            } catch (Exception e) {
                System.out.printf("%s\n", e);
            }
        } catch (Exception e) {
            System.out.printf("%s\n", e);
        }
    }

    // выводит список команд и их описание - help
    public static void help() {
        printInfo();
    }

    // завершает работу программы - exit
    public static void exit() {
        System.out.println("Goodbye");
    }

    public static void main(String[] args) {
        printInfo();
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("> ");
            while (true) {
                String command = scanner.nextLine();
                String[] splited = command.split("\\s+");
                if (splited.length == 1 && splited[0].equals(EXIT_COMMAND)) {
                    MyFile.exit();
                    return;
                }
                if (splited.length == 1 && splited[0].equals(HELP_COMMAND)) {
                    MyFile.help();
                } else {
                    try {
                        if (splited.length > 2 && !splited[0].equals(SETMOD_COMMAND) ||
                                splited[0].equals(SETMOD_COMMAND) && splited.length > 3) {
                            throw new ArrayIndexOutOfBoundsException();
                        }
                        if (splited.length == 2) {
                            switchCLICommand(splited[0], splited[1], "");
                        } else {
                            switchCLICommand(splited[0], splited[1], splited[2]);

                        }
                    } catch (Exception e) {
                        System.out.printf("%s \n", e);
                    }
                }
                System.out.print("> ");
            }
        } catch (Exception e) {
            System.out.printf("%s\n", e);
        }
    }


    public static void switchCLICommand(String command, String commandPath, String permission) throws Exception {
        switch (command) {
            case LS_COMMAND -> MyFile.listDirectory(commandPath);
            case LS_PY_COMMAND -> MyFile.listPythonFiles(commandPath);
            case IS_DIR_COMMAND -> MyFile.isDirectory(commandPath);
            case DEFINE_COMMAND -> MyFile.define(commandPath);
            case READMOD_COMMAND -> MyFile.printPermissions(commandPath);
            case SETMOD_COMMAND -> MyFile.setPermissions(commandPath, permission);
            case CAT_COMMAND -> MyFile.printContent(commandPath);
            case APPEND_COMMAND -> MyFile.appendFooter(commandPath);
            case BC_COMMAND -> MyFile.createBackup(commandPath);
            case GREPLONG_COMMAND -> MyFile.printLongestWord(commandPath);
            case HELP_COMMAND -> MyFile.help();
            default -> throw new Exception("command not allowed");
        }
    }

    public static void printText(String text) {
        System.out.println(text);
    }

    public static void printInfo() {
        printText("MyFile 1.0 команды:");
        printText("ls <path>               выводит список всех файлов и директорий для path");
        printText("ls_py <path>            выводит список файлов с расширением .py в path");
        printText("is_dir <path>           выводит true, если path это директория, в других случаях false");
        printText("define <path>           выводит директория или файл в зависимости от типа path");
        printText("readmod <path>          выводит права для файла в формате rwx для текущего пользователя");
        printText("setmod <path> <perm>    устанавливает права для файла path");
        printText("cat <path>              выводит контент файла");
        printText("append <path>           добавляет строку # Autogenerated line в конец path");
        printText("bc <path>               создает копию path в директорию /tmp/${date}.backup где, date - это дата в формате dd-mm-yyyy");
        printText("greplong <path>         выводит самое длинное слово в файле");
        printText("help                    выводит список команд и их описание");
        printText("exit                    завершает работу программы");
    }

    public static void filePermissionChecker(boolean permissionAssociation, String type) {
        if (permissionAssociation) {
            System.out.print(type);
        } else {
            System.out.print("-");
        }
    }

    public static void ifCaseCheckerWithException(boolean condition, String exceptionText) throws Exception {
        if (condition) {
            throw new Exception(exceptionText);
        }
    }

    public static void deleteDir(File file) {
        File[] contents = file.listFiles();
        if (contents != null) {
            for (File f : contents) {
                if (!Files.isSymbolicLink(f.toPath())) {
                    deleteDir(f);
                }
            }
        }
        file.delete();
    }
}

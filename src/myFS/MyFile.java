package myFS;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;

// add Exceptions
public class MyFile {
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

    // выводит список всех файлов и директорий для `path` - ls
    public static void listDirectory(String path) {
        try {
            File[] listOfFiles = new File(path).listFiles();
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

    // выводит список файлов с расширением `.py` в `path` - ls_py
    public static void listPythonFiles(String path) {
        try {
            File[] listOfFiles = new File(path).listFiles();
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

    // выводит `true`, если `path` это директория, в других случаях `false` - id_dir
    public static void isDirectory(String path) {
        try {
            File file = new File(path);
            System.out.println(file.isDirectory());
        } catch (Exception e) {
            System.out.printf("%s\n", e);
        }
    }

    // выводит `директория` или `файл` в зависимости от типа `path` - define
    public static void define(String path) {
        try {
            File file = new File(path);
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

    // выводит права для файла в формате `rwx` для текущего пользователя - readmod
    public static void printPermissions(String path) {
        try {
            File file = new File(path);
            boolean exists = file.exists();
            if (exists) {
                filePermissionChecker(file.canRead(), "r");
                filePermissionChecker(file.canWrite(), "w");
                filePermissionChecker(file.canExecute(), "x");
                System.out.println();
            }
        } catch (Exception e) {
            System.out.printf("%s\n", e);
        }
    }

    // устанавливает права для файла `path` - setmod
    public static void setPermissions(String path, String permissions) {
        try {
            File file = new File(path);

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
            Scanner obj = new Scanner(file);
            while (obj.hasNextLine()) {
                System.out.println(obj.nextLine());
            }
        } catch (Exception e) {
            System.out.printf("%s\n", e);
        }
    }

    // добавляет строке `# Autogenerated line` в конец `path` - append
    public static void appendFooter(String path) {
        try {
            File file = new File(path);
            if (!file.exists()) {
                throw new FileNotFoundException();
            }
            FileWriter fw = new FileWriter(file, true);
            fw.append("\n# Autogenerated line");
            fw.close();
        } catch (Exception e) {
            System.out.printf("%s\n", e);
        }
    }

    // создает копию `path` в директорию `/tmp/${date}.backup` где, date - это дата в формате `dd-mm-yyyy`. `path` может быть директорией или файлом. При директории, копируется весь контент. - bc
    public static void createBackup(String path) {
        try {
            File file = new File(path);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
            Calendar calendar = Calendar.getInstance();
            File newBackupFile = new File("/tmp/" + simpleDateFormat.format(calendar.getTime()) + ".backup");
            if (newBackupFile.exists()) {
                newBackupFile.delete();
            }
            Files.copy(Path.of(file.getPath()), Path.of(newBackupFile.getPath()));
            File[] listOfFiles = file.listFiles();
            if (listOfFiles != null) {
                for (File listOfFile : listOfFiles) {
                    try {
                        Files.copy(Path.of(listOfFile.getPath()), Path.of(newBackupFile.getPath() +
                                "/" + listOfFile.getName()));
                    } catch (Exception e) {
                        System.out.println(e);
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
            Scanner scanner = new Scanner(file);
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
        Scanner scanner = new Scanner(System.in);
        System.out.print("> ");
        while (true) {
            String command = scanner.next();
            if (command.equals(EXIT_COMMAND)) {
                MyFile.exit();
                return;
            }
            switchCLICommand(command, scanner);
            System.out.print("> ");
        }
    }

    public static void switchCLICommand(String command, Scanner scanner) {
        switch (command) {
            case LS_COMMAND -> MyFile.listDirectory(scanner.next());
            case LS_PY_COMMAND -> MyFile.listPythonFiles(scanner.next());
            case IS_DIR_COMMAND -> MyFile.isDirectory(scanner.next());
            case DEFINE_COMMAND -> MyFile.define(scanner.next());
            case READMOD_COMMAND -> MyFile.printPermissions(scanner.next());
            case SETMOD_COMMAND -> MyFile.setPermissions(scanner.next(), scanner.next());
            case CAT_COMMAND -> MyFile.printContent(scanner.next());
            case APPEND_COMMAND -> MyFile.appendFooter(scanner.next());
            case BC_COMMAND -> MyFile.createBackup(scanner.next());
            case GREPLONG_COMMAND -> MyFile.printLongestWord(scanner.next());
            case HELP_COMMAND -> MyFile.help();
            default -> {
            }
        }
    }

    public static void printText(String text) {
        System.out.println(text);
    }

    public static void printInfo() {
        printText("MyFS 1.0 команды:");
        printText("ls <path>               выводит список всех файлов и директорий для `path`");
        printText("ls_py <path>            выводит список файлов с расширением `.py` в `path`");
        printText("is_dir <path>           выводит `true`, если `path` это директория, в других случаях `false`");
        printText("define <path>           выводит `директория` или `файл` в зависимости от типа `path`");
        printText("readmod <path>          выводит права для файла в формате `rwx` для текущего пользователя");
        printText("setmod <path> <perm>    устанавливает права для файла `path`");
        printText("cat <path>              выводит контент файла");
        printText("append <path>           добавляет строку `# Autogenerated line` в конец `path`");
        printText("bc <path>               создает копию `path` в директорию `/tmp/${date}.backup` где, date - это дата в формате `dd-mm-yyyy`");
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
}
package ru.example.importxls.util;

import ru.example.importxls.exception.ArgumentsException;

import java.io.File;

public class ArgsUtils {

    public static String checkArgs(String[] args) {
        if (args.length != 1) {
            throw new ArgumentsException("Неверно задан аргумент!");
        }
        File inputFile = new File(args[0]);
        if (!inputFile.isFile()) {
            throw new ArgumentsException("Файл XLS не найден!");
        }
        return inputFile.getAbsolutePath();
    }

}

package ru.vitasoft.util;

import ru.vitasoft.exception.ArgumentsException;

import java.io.File;

public class ArgsUtils {

    public static String checkArgs(String[] args) {
        if (args.length != 1) {
            throw new ArgumentsException("Wrong arguments");
        }
        File inputFile = new File(args[0]);
        if (!inputFile.isFile()) {
            throw new ArgumentsException("Input file not found");
        }
        return inputFile.getAbsolutePath();
    }

}

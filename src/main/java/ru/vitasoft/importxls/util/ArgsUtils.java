package ru.vitasoft.importxls.util;

import ru.vitasoft.importxls.exception.ArgumentsException;

import java.io.File;

public class ArgsUtils {

    public static String checkArgs(String[] args) {
        if (args.length != 1) {
            throw new ArgumentsException("Wrong arguments");
        }
        File inputFile = new File(args[0]);
        if (!inputFile.isFile()) {
            throw new ArgumentsException("Excel file not found");
        }
        return inputFile.getAbsolutePath();
    }

}

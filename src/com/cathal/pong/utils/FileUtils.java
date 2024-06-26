package com.cathal.pong.utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.IntBuffer;

public class FileUtils {

    private FileUtils() {

    }

    public static String loadAsString(String path) {
        StringBuilder result = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            String buffer = "";
            while ((buffer = reader.readLine()) != null) {
                result.append(buffer + '\n');
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Could not find file: " + path + ", or could not read it!");
        }
        return result.toString();
    }
}

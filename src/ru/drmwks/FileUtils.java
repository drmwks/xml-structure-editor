package ru.drmwks;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public final class FileUtils {
    private FileUtils() {}

    public static String readFile(String fileName) {
        InputStream inputStream = FileUtils.class.getClassLoader().getResourceAsStream(fileName);

        return new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8)).lines()
                .collect(Collectors.joining("\n"));
    }
}

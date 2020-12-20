package ru.drmwks;

public class Main {

    public static void main(String... args) {
        String xml = FileUtils.readFile("example.xml");

        System.out.println("<  Result using replacement  >");
        System.out.println(ReplaceExample.processData(xml));

        System.out.println("\n\n");

        System.out.println("<  Result using StAX  >");
        System.out.println(StaxExample.processData(xml));
    }

}

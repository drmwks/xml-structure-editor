package ru.drmwks;

import java.util.Date;

public final class ReplaceExample {
    private ReplaceExample() {}

    public static String processData(String xml) {
        String timeTag = "<time>" + new Date() + "</time>";

        return xml.replaceAll("(?s)<!--.*?-->", "" )
                .replaceAll("<note>\n", "<note>\n" + timeTag)
               .replaceAll("</?note>", "<reminder>")
               .replaceAll("<heading>.*?</heading>", "");
    }
}

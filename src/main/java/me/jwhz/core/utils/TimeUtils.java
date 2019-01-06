package me.jwhz.core.utils;

public class TimeUtils {
    public static String formatSeconds(long time) {

        long leftOverSeconds = time / 1000 % 60;
        long minutes = time / (60 * 1000) % 60;
        long hours = time / (60 * 60 * 1000) % 24;
        long days = time / (24 * 60 * 60 * 1000);

        return (days == 0 ? "" : days + "d ") + (hours == 0 ? "" : hours + "h ") +
                (minutes == 0 ? "" : minutes + "m ") + (leftOverSeconds == 0 ? "" : leftOverSeconds + "s");

    }
}

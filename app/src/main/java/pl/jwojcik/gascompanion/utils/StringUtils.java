package pl.jwojcik.gascompanion.utils;


public class StringUtils {

    public static boolean isNotEmpty(String s) {
        if (s != null && s.trim().length() > 0 && !s.equalsIgnoreCase("null"))
            return true;
        else
            return false;
    }

    public static boolean isEmpty(String s) {
        return !isNotEmpty(s);
    }

}

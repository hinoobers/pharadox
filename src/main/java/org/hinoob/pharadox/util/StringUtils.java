package org.hinoob.pharadox.util;

public class StringUtils {

    public static String formatNumber(int number) {
        if(number < 1000) return String.valueOf(number);
        if(number < 1000000) return number / 1000 + "K";
        if(number < 1000000000) return number / 1000000 + "M";
        return number / 1000000000 + "B";
    }
}

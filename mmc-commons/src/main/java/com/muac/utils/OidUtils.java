package com.muac.utils;

public class OidUtils {


    public static String convertToDotNotation(String restOfTheUrl) {
        return restOfTheUrl.replace("/", ".");
    }

}

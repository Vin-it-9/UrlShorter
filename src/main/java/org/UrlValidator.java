package org;


import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Pattern;

public class UrlValidator {

    private static final Pattern URL_PATTERN = Pattern.compile(
            "^(https?|ftp)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]");

    public static boolean isValid(String url) {
        if (url == null || url.isEmpty()) {
            return false;
        }
        if (!URL_PATTERN.matcher(url).matches()) {
            return false;
        }
        try {
            new URL(url);
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }
}
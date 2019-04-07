package com.company.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    private static final String PHONE_REGEX = "^\\d{3}\\s*\\d{3}\\s*\\d{3}$";
    private static final String JABBER_REGEX = "^[a-z0-9.-_*]+$";

    private Validator() {

    }

    public static boolean isEmail(String text) {
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(text);
        return matcher.matches();
    }

    public static boolean isPhone(String text) {
        Pattern pattern = Pattern.compile(PHONE_REGEX);
        Matcher matcher = pattern.matcher(text);
        return matcher.matches();

    }

    public static boolean isJabber(String text) {
        Pattern pattern = Pattern.compile(JABBER_REGEX);
        Matcher matcher = pattern.matcher(text);
        return matcher.matches();
    }
}

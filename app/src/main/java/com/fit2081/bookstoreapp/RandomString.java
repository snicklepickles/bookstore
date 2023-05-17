package com.fit2081.bookstoreapp;

import java.util.Random;

public abstract class RandomString {

    public static final String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String lower = upper.toLowerCase();
    public static final String digits = "0123456789";
    public static final String alphaNumeric = upper + lower + digits;

    public static String generateNewRandomString(int length) {
        char[] buf;
        Random random = new Random();
        if (length < 1) throw new IllegalArgumentException();
        buf = new char[length];
        for (int idx = 0; idx < buf.length; ++idx)
            buf[idx] = alphaNumeric.charAt(random.nextInt(alphaNumeric.length()));
        return new String(buf);
    }
}


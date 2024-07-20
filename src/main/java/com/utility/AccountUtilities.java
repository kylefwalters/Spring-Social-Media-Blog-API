package com.utility;

public class AccountUtilities {
    public static boolean isValidPassword(String password) {
        return password.length() >= 4;
    }
}

package com.utility;

public class MessageUtilities {
    public static boolean isValidMessage(String message) {
        return !message.isEmpty() && message.length() <= 255;
    }
}

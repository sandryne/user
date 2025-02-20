package com.awesome.userservice.util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {


    public static String hashPassword(String plainTextPassword) {
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt(12));  // 12 rounds of salting
    }


    public static boolean checkPassword(String plainTextPassword, String hashedPassword) {
        return BCrypt.checkpw(plainTextPassword, hashedPassword);
    }

}

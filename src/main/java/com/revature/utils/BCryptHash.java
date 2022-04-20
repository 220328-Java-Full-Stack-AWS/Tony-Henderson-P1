package com.revature.utils;

import at.favre.lib.crypto.bcrypt.BCrypt;
import at.favre.lib.crypto.bcrypt.BCrypt.Result;

public class BCryptHash {

    private BCryptHash(){}

    public static String hash(String string){
        return BCrypt.withDefaults().hashToString(12, string.toCharArray());
    }

    /**
     * Method returns true or false if the params match.
     * @param string Plain text string
     * @param hashedString Hashed string to check {@code string} against.
     */
    public static Boolean verify(String string, String hashedString){
        Result result = BCrypt.verifyer().verify(string.toCharArray(), hashedString);
        return result.verified;
    }
}

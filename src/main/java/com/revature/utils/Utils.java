package com.revature.utils;

public class Utils {

    private Utils(){}

    public static String stringToTitleCase(String string){
        StringBuilder sb = new StringBuilder();
        boolean convertNext = true;
        for(char c : string.toCharArray()){
            if(c == '_' || c == ' '){
                convertNext = true;
                sb.append(' ');
                continue;
            }
            if(convertNext){
                convertNext = false;
                sb.append(Character.toTitleCase(c));
            }
            else {
                sb.append(Character.toLowerCase(c));
            }
        }
        return sb.toString();
    }
}

package com.projeto.ads.util;

import java.util.UUID;

public class Util {
   
    
    public static String generateToken() {
        String token = UUID.randomUUID().toString();
        token = token.replaceAll("-", "").toLowerCase();
        return token;
    }
}

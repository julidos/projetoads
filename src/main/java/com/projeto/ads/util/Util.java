package com.projeto.ads.util;

import java.util.UUID;

public class Util {
   
    
    public static String generateToken() {
    	UUID uuid = UUID.randomUUID();
    	String token = uuid.toString().replaceAll("-", "").toLowerCase();
        return token;
    }
}

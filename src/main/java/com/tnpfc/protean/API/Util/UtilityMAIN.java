package com.tnpfc.protean.API.Util;

import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Service;

@Service
public class UtilityMAIN {
	
	public static String generateUniqueNumber() {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String timestampSegment = timestamp.substring(timestamp.length() - 8);
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        StringBuilder randomSegment = new StringBuilder();
        for (int i = 0; i < 12; i++) {
            int index = ThreadLocalRandom.current().nextInt(chars.length());
            randomSegment.append(chars.charAt(index));
        }
        
        return randomSegment + timestampSegment;
	}
}

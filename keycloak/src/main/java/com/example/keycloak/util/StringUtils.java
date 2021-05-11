package com.example.keycloak.util;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class StringUtils {
	public static String get6DigitNumber() {
		int number = new Random().nextInt(999999);
		return String.format("%06d", number);
	}
	
	public static boolean isNullOrEmpty(String val) {
		return val == null || "".equals(val.trim()) || val.trim().isEmpty();
	}
	
	public static boolean isOnlyNumber(String val) {
		if(isNullOrEmpty(val)) return false;
		
		return val.chars().allMatch(x -> Character.isDigit(x));
	}
	
	public static boolean isLength(int max, int min, String value) {
		return value.length() == min || value.length() == max;
	}
}

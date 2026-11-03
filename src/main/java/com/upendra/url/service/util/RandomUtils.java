package com.upendra.url.service.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.SecureRandom;
import java.util.Random;

public class RandomUtils {

	private static final Logger logger = LoggerFactory.getLogger(RandomUtils.class);
	
	
	private static final String LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final SecureRandom random = new SecureRandom();

    public static String generateLeadId() {
        // 4 random letters
        StringBuilder prefix = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            prefix.append(LETTERS.charAt(random.nextInt(LETTERS.length())));
        }

        // 6 random digits
        int number = random.nextInt(1_000_000); // 0 to 999999
        String digits = String.format("%06d", number);

        return prefix.toString() + digits;
    }

	public static String generateOtp(int length, boolean checkDigit) {
		logger.info("generate OTP");
		Random random = new Random();
		int otp = random.nextInt(1000000);
		String profileOtp = ("00000000000" + otp);
		// logger.info("Generated OTP " + profileOtp);

		profileOtp = profileOtp.substring(profileOtp.length() - length);
		if (checkDigit) {
			profileOtp = profileOtp + generateCheckDigit(profileOtp);
		}

		return profileOtp;
	}
	
	
		public static String generate4DigitOTP(int length) {  
		    String numbers = "0123456789";  
		    Random rndm_method = new Random();  
		    char[] otp = new char[length];  
		    for (int i = 0; i < length; i++) {  
		        otp[i] = numbers.charAt(rndm_method.nextInt(numbers.length()));  
		    }  
		    return new String(otp);  
		}
		
		public static String generateAgencyCode(int length) {  
		    String numbers = "0123456789";  
		    Random rndm_method = new Random();  
		    char[] otp = new char[length];  
		    
		    for (int i = 0; i < length; i++) {  
		        otp[i] = numbers.charAt(rndm_method.nextInt(numbers.length()));  
		    }  
		    
		    return "WT" + new String(otp);  
		}

	public static String generateCheckDigit(String pin) {
		logger.info("OTP, {}");

		int digit = 1;
		for (char ch : pin.toCharArray()) {
			logger.info("CHAR, {}");
			if (Character.getNumericValue(ch) == 0) {
				continue;
			}
			digit = digit * Character.getNumericValue(ch);
		}
		logger.info("Check digit, {}");

		String d = String.valueOf(digit);
		while (d.length() > 1) {
			digit = 0;
			for (char ch : d.toCharArray()) {
				digit = digit + Character.getNumericValue(ch);
			}
			d = String.valueOf(digit);
		}
		logger.info("Final check digit, {}", d);
		return d;
	}
	

	/*
	 * public void isValidTenancy(String tenancyCode, String username){
	 * logger.info("Check if user belongs to tenancy or not, {}, {}", tenancyCode,
	 * username); if( ! username.endsWith("@" + tenancyCode)) { throw new
	 * AuthenticationException() } return null; }
	 * 
	 */

}


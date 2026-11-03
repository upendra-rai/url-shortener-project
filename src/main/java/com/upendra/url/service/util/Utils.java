package com.upendra.url.service.util;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.Random;

import org.apache.poi.ss.usermodel.Cell;

public class Utils {

	private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	private static final int ID_LENGTH = 8;
	private static final SecureRandom random = new SecureRandom();

	public static String generateProductCode(String categoryCode, String productName) {
		// Normalize category code
		String normalizedCategoryCode = categoryCode.trim().toUpperCase();

		// Generate product short code
		String productShortCode = generateShortCode(productName);

		// Format sequence → 001, 002, 003
		int sequence = Integer.parseInt(RandomUtils.generate4DigitOTP(3));
		String formattedSequence = String.format("%03d", sequence);

		// Final code
		return normalizedCategoryCode + "-" + productShortCode + "-" + formattedSequence;
	}

	private static String generateShortCode(String productName) {
		String[] words = productName.trim().toUpperCase().split("\\s+");
		StringBuilder shortCode = new StringBuilder();
		for (String word : words) {
			if (word.length() >= 3) {
				shortCode.append(word.substring(0, 3));
				break;
			} else {
				shortCode.append(word.charAt(0));
			}
		}
		// Max length 3
		if (shortCode.length() > 3) {
			return shortCode.substring(0, 3);
		}
		return shortCode.toString();
	}

	public static String generateClientId() {
		StringBuilder sb = new StringBuilder(ID_LENGTH);
		for (int i = 0; i < ID_LENGTH; i++) {
			int index = random.nextInt(CHARACTERS.length());
			sb.append(CHARACTERS.charAt(index));
		}
		return sb.toString();
	}

	public static String generateCode() {
		Random random = new Random();
		String sixDigitCode = String.format("%06d", random.nextInt(1000000));
		return "DAV" + sixDigitCode;
	}

	public static String generateInvoiceNumber() {
		String prefix = "WWT";
		String year = String.valueOf(LocalDate.now().getYear());
		int randomNumber = new Random().nextInt(9000) + 1000; // ensures 4 digits
		return prefix + year + randomNumber;
	}

	public static String getNightsAndDays(LocalDate startDate, LocalDate endDate) {
		if (startDate == null || endDate == null || endDate.isBefore(startDate)) {
			return "Invalid Dates";
		}

		long nights = ChronoUnit.DAYS.between(startDate, endDate);
		long days = nights + 1;
		return nights + "N/" + days + "D";
	}

	public static String generateDigitOTP(int length) {
		String numbers = "0123456789";
		Random rndm_method = new Random();
		char[] otp = new char[length];
		for (int i = 0; i < length; i++) {
			otp[i] = numbers.charAt(rndm_method.nextInt(numbers.length()));
		}
		return new String(otp);
	}

	public static String getString(Cell cell) {
		return cell == null ? null : cell.toString().trim();
	}

	public static Long parseLong(Cell cell) {
		try {
			if (cell == null)
				return null;
			return Long.parseLong(cell.toString().split("\\.")[0]);
		} catch (Exception e) {
			return null;
		}
	}

	public static String generateEmail(String name) {
		return name + "@dav-school.com";
	}

	public static String getFirstName(String name) {
		return Optional.ofNullable(name).map(String::trim).filter(n -> !n.isEmpty())
				.map(n -> n.split("\\s+")[0].toLowerCase()).orElse(null);
	}

	public static String getPrefixBeforeYear(String input, String year) {
		// input = "1202627"
		// year ="2026"
		if (input == null || year == null || !input.contains(year)) {
			return null;
		}
		return input.substring(0, input.indexOf(year));
	}

	public static String generateRandomString(int length) {
		String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		Random random = new Random();
		StringBuilder result = new StringBuilder(length);

		for (int i = 0; i < length; i++) {
			result.append(characters.charAt(random.nextInt(characters.length())));
		}

		return result.toString();
	}

}

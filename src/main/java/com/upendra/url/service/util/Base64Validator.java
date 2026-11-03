package com.upendra.url.service.util;

import java.util.Base64;

import com.upendra.url.service.exceptions.ServiceException;

public class Base64Validator {
	private static final int MIN_SIZE_KB = 500;
	private static final int MAX_SIZE_MB = 2;
	private static final int MIN_SIZE_BYTES = MIN_SIZE_KB * 1024;
	private static final int MAX_SIZE_BYTES = MAX_SIZE_MB * 1024 * 1024;

	public static void validateBase64Data(String base64Data, String name) throws ServiceException {
		if (base64Data == null || base64Data.isEmpty()) {
			throw new ServiceException("Base64 data is null or empty " + name);
		}

		// Base64 encoding increases the size of the data by ~33%
		double estimatedSize = base64Data.length() * 0.75;

		// Check if estimated size is within the 500 KB to 2 MB range
//		if (estimatedSize < MIN_SIZE_BYTES || estimatedSize > MAX_SIZE_BYTES) {
//			throw new ServiceException(name + " image size is not within the 500 KB to 2 MB range ");
//		}

		try {
			byte[] decodedBytes = Base64.getDecoder().decode(base64Data);
			int decodedSize = decodedBytes.length;
			if (decodedSize < MIN_SIZE_BYTES || decodedSize > MAX_SIZE_BYTES) {
				throw new ServiceException(name + " image size is not within the 500 KB to 2 MB range ");
			}
		} catch (Exception e) {
			// If decoding fails, the input is not valid Base64
			// throw new ServiceException("Base64 data is not valid");
		}
	}

	public static void validateBase64DataNew(String base64Data, String name) throws ServiceException {
		if (base64Data == null || base64Data.isEmpty()) {
			throw new ServiceException("Base64 data is null or empty " + name);
		}

		// Base64 encoding increases the size of the data by ~33%
		double estimatedSize = base64Data.length() * 0.75;

		// Check if estimated size is within the 500 KB to 2 MB range
		if (estimatedSize < MIN_SIZE_BYTES || estimatedSize > MAX_SIZE_BYTES) {
			throw new ServiceException(name + " image size is not within the 500 KB to 2 MB range ");
		}

		try {
			byte[] decodedBytes = Base64.getDecoder().decode(base64Data);
			int decodedSize = decodedBytes.length;
			if (decodedSize < MIN_SIZE_BYTES || decodedSize > MAX_SIZE_BYTES) {
				throw new ServiceException(name + " image size is not within the 500 KB to 2 MB range ");
			}

			// Check MIME type
//			String mimeType = new MimetypesFileTypeMap().getContentType(new java.io.ByteArrayInputStream(decodedBytes));
//			if (!mimeType.equals("image/jpeg") && !mimeType.equals("image/png")
//					&& !mimeType.equals("application/msword")) {
//				throw new ServiceException(
//						name + " file type is not supported. Only .jpeg, .png, and .doc files are allowed.");
//			}
		} catch (Exception e) {
			// If decoding fails, the input is not valid Base64
			throw new ServiceException("Base64 data is not valid");
		}
	}

}

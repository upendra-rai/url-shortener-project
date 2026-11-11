package com.upendra.url.service.services;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.upendra.url.service.entities.ShortUrl;
import com.upendra.url.service.repositories.UrlRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QRCodeService {

	private final UrlRepository shortUrlRepository;

	@Value("${app.base-url}")
	private String baseUrl;

	public byte[] generateQRCode(String shortCode) {

		// Find existing short URL
		ShortUrl shortUrl = shortUrlRepository.findByShortCode(shortCode)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Short URL not found"));

		// Build short URL, not original URL
		String shortUrlValue = baseUrl.replaceAll("/+$", "") + "/" + shortUrl.getShortCode();

		try {

			QRCodeWriter qrCodeWriter = new QRCodeWriter();

			Map<EncodeHintType, Object> hints = new HashMap<>();

			hints.put(EncodeHintType.MARGIN, 2);
			hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

			BitMatrix bitMatrix = qrCodeWriter.encode(shortUrlValue, BarcodeFormat.QR_CODE, 350, 350, hints);

			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

			MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);

			return outputStream.toByteArray();

		} catch (WriterException | IOException e) {

			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to generate QR code", e);
		}
	}
}

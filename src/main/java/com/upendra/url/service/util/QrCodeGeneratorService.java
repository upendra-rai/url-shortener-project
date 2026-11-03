package com.upendra.url.service.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class QrCodeGeneratorService {

	private static final int QR_SIZE = 300;

	public MultipartFile generateQrCode(String content, String fileName) {

		log.info("Generating QR code | content={}", content);

		try {
			Map<EncodeHintType, Object> hints = new HashMap<>();
			hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

			BitMatrix bitMatrix = new MultiFormatWriter()
					.encode(content, BarcodeFormat.QR_CODE, QR_SIZE, QR_SIZE, hints);

			BufferedImage image = new BufferedImage(QR_SIZE, QR_SIZE, BufferedImage.TYPE_INT_RGB);
			for (int x = 0; x < QR_SIZE; x++) {
				for (int y = 0; y < QR_SIZE; y++) {
					image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
				}
			}

			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			ImageIO.write(image, "PNG", outputStream);

			return new ByteArrayMultipartFile(
					outputStream.toByteArray(),
					fileName,
					fileName + ".png",
					"image/png"
			);

		} catch (Exception e) {
			log.error("QR generation failed", e);
			throw new RuntimeException("QR code generation failed");
		}
	}
}

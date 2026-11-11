
package com.upendra.url.service.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.upendra.url.service.services.QRCodeService;

@RestController
@RequestMapping("/api/v1/qr")
public class QRCodeController {

	@Autowired
	private QRCodeService qrCodeService;

	@GetMapping("/{shortCode}")
	public ResponseEntity<byte[]> generateQRCode(@PathVariable String shortCode) {
		byte[] qrCode = qrCodeService.generateQRCode(shortCode);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + shortCode + ".png\"")
				.contentType(MediaType.IMAGE_PNG).body(qrCode);
	}
}

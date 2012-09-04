package com.n4systems.fieldid.service.images;


import net.coobird.thumbnailator.Thumbnails;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Transactional
public class ImageService {

	public byte[] generateThumbnail(byte[] imageData) throws ImageProcessingException {
		return scaleImage(imageData, 125, 125);
	}

	public byte[] generateMedium(byte[] imageData) throws ImageProcessingException {
		return scaleImage(imageData, 600, 600);
	}

	public byte[] scaleImage(byte[] imageData, int width, int height) throws ImageProcessingException {
		try {
			ByteArrayOutputStream newImageStream = new ByteArrayOutputStream();
			Thumbnails
					.of(new ByteArrayInputStream(imageData))
					.width(width)
					.height(height)
					.keepAspectRatio(true)
					.useOriginalFormat()
					.outputQuality(0.8)
					.toOutputStream(newImageStream);
			return newImageStream.toByteArray();
		} catch (IOException e) {
			throw new ImageProcessingException("Failed resizing image", e);
		}
	}

}

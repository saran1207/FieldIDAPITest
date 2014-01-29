package com.n4systems.fieldid.service.images;


import com.google.common.base.Preconditions;
import com.n4systems.fieldid.service.attachment.ImageOptions;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Transactional
public class ImageService {

	public byte[] generateThumbnail(byte[] imageData) throws ImageProcessingException {
		return scaleImage(imageData, 150, 150);
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

    public byte[] generateImage(byte[] bytes, ImageOptions options) {
        Preconditions.checkNotNull(options);
        try {
            ByteArrayOutputStream newImageStream = new ByteArrayOutputStream();
            Thumbnails
                    .of(new ByteArrayInputStream(bytes))
                    .width(options.getWidth())
                    .height(options.getHeight())
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

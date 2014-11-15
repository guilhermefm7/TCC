package br.com.recomusic.persistencia.utils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Method;
import org.imgscalr.Scalr.Mode;

/**
 * 
 * 
 * 
 */
public class ThumbnailGenerator {

	public static File generateThumbnail(File f, String thumbName,
			String mimetype) throws IOException {

		BufferedImage img = ImageIO.read(f); // load image


		BufferedImage thumbImg = Scalr.resize(img, Method.QUALITY,
				Mode.AUTOMATIC, 150, 150, Scalr.OP_ANTIALIAS);
		// convert bufferedImage to outpurstream
		ByteArrayOutputStream os = new ByteArrayOutputStream();

		String type = "";

		if (mimetype.trim().equals("image/jpeg"))
			type = "jpg";

		if (mimetype.trim().equals("image/png"))
			type = "png";

		if (mimetype.trim().equals("image/gif"))
			type = "gif";

		ImageIO.write(thumbImg, type, os);

		// or wrtite to a file
		String savePath = "C://Users//Guilherme//FT";
		File f2 = new File(savePath + "/" + thumbName + "." + type);

		ImageIO.write(thumbImg, type, f2);

		return f2;

	}

}
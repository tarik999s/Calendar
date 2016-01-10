package com.taras.my_puzzle.images;

import java.awt.*;
import java.io.*;
import java.util.*;

import javax.swing.*;


/**
 * Class for loading of the images
 * 
 * @author taras
 */
public final class PuzzleImages {

	/**
	 * load the image
	 * 
	 * @param name -
	 *            name of the image
	 * @return the image
	 */
	@SuppressWarnings("rawtypes")
	public static Image loadImage(String imageName) {
		Class thisClass = PuzzleImages.class;
		return new ImageIcon(thisClass.getResource(imageName)).getImage();
	}

	/**
	 * load all images.
	 * 
	 * @return the array of images
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Vector getImages() {
		Vector vector = new Vector();
		try {
			Class thisClass = PuzzleImages.class;
			ImageIcon imageIcon = null;
			ImageIcon imageIconS = null;

			// the list of the images to load
			// is stored in the puzzleImages.txt file
			LineNumberReader lnr = new LineNumberReader(new InputStreamReader(
					thisClass.getResourceAsStream("puzzleImages.txt")));
			String line = null;
			String file = null;
			String name = null;
			int sep = -1;
			while ((line = lnr.readLine()) != null) {
				sep = line.indexOf(' ');
				if (sep < 0) {
					file = line;
					name = "";
				} else {
					file = line.substring(0, sep);
					name = line.substring(sep + 1, line.length());
				}
				System.out.println(file + ":" + name);

				imageIcon = new ImageIcon(thisClass.getResource(file));
				imageIconS = scaleIcon(imageIcon, 80);
				vector.addElement(new ImageDescription(imageIcon, imageIconS, name));
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println( "Exception with loading images" );
		}
		return vector;
	}

	/**
	 * scale the image to the scaled version
	 * 
	 * @param imageIcon-
	 *            original image
	 * @param imageSize-
	 *            the scaled image must fit into the (imageSize x imageSize) rectangle
	 * @return the scaled image
	 */
	public static ImageIcon scaleIcon(ImageIcon imageIcon, int imageSize) {
		
		int imageW = imageIcon.getIconWidth();
		int imageH = imageIcon.getIconHeight();

		int newImageW = 0;
		int newImageH = 0;

		if (imageW >= imageH) {
			newImageW = imageSize;
			newImageH = (int) ((double) imageH * ((double) imageSize / (double) imageW));
		} else {
			newImageW = (int) ((double) imageW * ((double) imageSize / (double) imageH));
			newImageH = imageSize;
		}
		Image image_scaled = imageIcon.getImage().getScaledInstance(newImageW, newImageH,
				Image.SCALE_FAST);
		return new ImageIcon(image_scaled);
	}

	/**
	 * load a image as ImageIcon
	 * 
	 * @param name
	 *            name of the image
	 * @return the image as ImageIcon
	 */
	@SuppressWarnings("rawtypes")
	public static ImageIcon getIcon(String name) {
		Class thisClass = PuzzleImages.class;
		return new ImageIcon(thisClass.getResource(name));
	}
}

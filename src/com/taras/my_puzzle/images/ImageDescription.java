package com.taras.my_puzzle.images;

import javax.swing.*;

/**
 * This class contains the image, the scaled version of this image and the image name
 * 
 * @author taras
 * 
 */
public class ImageDescription {
	
	public ImageIcon imageIcon = null;
	
	public ImageIcon imageIcon_scaled = null;
	
	public String imageName = null;

	/**
	 * Constructor with parametrs
	 * 
	 * @param imageIcon
	 *            image itself
	 * @param imageIcon_scaled
	 *            scaled image version
	 * @param imageName
	 *            image name
	 */
	public ImageDescription(ImageIcon imageIcon, ImageIcon imageIcon_scaled,
			String imageName) {
		this.imageIcon = imageIcon;
		this.imageIcon_scaled = imageIcon_scaled;
		this.imageName = imageName;
	}

	/**
	 * @return the name of the image
	 */
	public String toString() {
		return imageName;
	}
}

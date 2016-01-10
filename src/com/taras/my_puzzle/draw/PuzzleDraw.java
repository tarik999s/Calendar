package com.taras.my_puzzle.draw;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;

/**
 * 
 * @author taras
 * 
 */
public class PuzzleDraw implements Paint, PaintContext {

	/**
	 * the reference to the whole image
	 */
	protected BufferedImage buffImage = null;
	/**
	 * the part of the image rectangle
	 */
	protected Rectangle viewRect = new Rectangle();
	/**
	 * help variable
	 */
	protected Rectangle userBounds = new Rectangle();

	/**
	 * Constructor with parametrs:
	 * 
	 * @param textur -
	 *            the puzzle image
	 * @param viewRect -
	 *            the rectangle for this piece
	 */
	public PuzzleDraw(BufferedImage textur, Rectangle viewRect) {
		this.buffImage = textur;
		this.viewRect.setRect(viewRect);
	}

	/**
	 * set a new rectangle for this piece, 
	 * needed if two pieces fit together
	 * 
	 * @param viewRect-
	 *            the new rectangle for this piece
	 */
	public void setViewRect(Rectangle viewRect) {
		this.viewRect.setRect(viewRect);
	}

	/**
	 * get the PaintContext of this Paint
	 * 
	 * @see java.awt.Paint
	 * @see java.awt.PaintContext
	 */
	public PaintContext createContext(ColorModel cm, Rectangle deviceBounds,
			Rectangle2D userBounds, AffineTransform xform, RenderingHints hints) {
		this.userBounds.setRect(deviceBounds);
		return this;
	}

	/**
	 * has this Paint transparent parts or not
	 * 
	 * @see java.awt.Paint
	 */
	public int getTransparency() {
		return OPAQUE;
	}

	/**
	 * destroy the Paint, not needed in Juzzle
	 */
	public void dispose() {
	}

	/**
	 * the ColorModel of this Paint, it's the same the puzzle image
	 * 
	 * @see java.awt.PaintContext
	 */
	public ColorModel getColorModel() {
		return buffImage.getColorModel();
	}

	/**
	 * return the part of the image to draw
	 * 
	 * @see java.awt.PaintContext
	 */
	public Raster getRaster(int x, int y, int w, int h) {
		int x2 = x - userBounds.x + viewRect.x;
		int y2 = y - userBounds.y + viewRect.y;

		return buffImage.getRaster().createChild(x2, y2, w, h, 0, 0, null);
	}

}

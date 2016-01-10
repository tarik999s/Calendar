package com.taras.my_puzzle.panel;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

import javax.swing.*;

import com.taras.my_puzzle.PicturePieces.PicturePieces;
import com.taras.my_puzzle.PicturePieces.impl.DummyPicturePiecesImpl;
import com.taras.my_puzzle.PicturePieces.impl.PicturePiecesImpl;

/**
 * This panel paints the pieces of the puzzle
 * 
 * @author taras
 * 
 */
@SuppressWarnings("serial")
public class PuzzlePanel extends JPanel implements MouseListener,
		MouseMotionListener {
	
	/**
	 * thicknes of the outline for the piece for repaint rect
	 */
	protected static final int PAINT_EXTEND = 2;
	
	/**
	 * thicknes of the shadow while dragging and for repaint rect
	 */
	protected static final int PAINT_SHADOW = 10;
	
	/**
	 * cursor while dragging
	 */
	protected static final Cursor CURSOR_HAND = new Cursor(Cursor.HAND_CURSOR);
	
	/**
	 * cursor normal
	 */
	protected static final Cursor CURSOR_DEFAULT = new Cursor(
			Cursor.DEFAULT_CURSOR);
	/**
	 * outline color
	 */
	protected static final Color outlineColor = new Color(0, 0, 0, 128);

	/**
	 * shadow color
	 */
	protected static final Color shadowColor = new Color(0, 0, 0, 128);

	/**
	 * program version
	 */
	public static String version = "1.0";

	/**
	 * Reference to the image
	 */
	protected BufferedImage buffImage = null;
	
	/**
	 * x division
	 */
	protected int x_parts = 0;
	
	/**
	 * y division
	 */
	protected int y_parts = 0;

	/**
	 * Reference to the ControlPanel to set the solved pieces count
	 * 
	 */
	protected CPanel controlPanel = null;

	/**
	 * drag modus 1 mean that the user clicks the mouse button and then move the
	 * piece and clicks again to release.<br>
	 * <br>
	 * <br>
	 * drag modus 2 mean that the user press the mouse button and then drag the
	 * piece and release the button.<br>
	 * <br>
	 * <br>
	 */
	protected boolean dragModus1, dragModus2 = false;
	
	/**
	 * last drag location for difference calculation
	 */
	protected Point lastDragLocation = new Point();
	
	/**
	 * source rect to repaint
	 */
	protected Rectangle repaintRect1 = new Rectangle();
	
	/**
	 * target rect to repaint
	 */
	protected Rectangle repaintRect2 = new Rectangle();
	
	/**
	 * needed to show only one time per game the win message
	 */
	protected boolean endMessagePrinted = false;

	/**
	 * reference to the pieces list model
	 * 
	 */
	protected PicturePieces picturePiecesList = null;


	/**
	 * outline on/off
	 */
	protected boolean outline = true;
	
	/**
	 * shadow on/off
	 */
	protected boolean shadow = true;

	/**
	 * Constructor with param
	 * 
	 * @param controlPanel
	 *            the reference to the CPanel
	 */
	public PuzzlePanel(CPanel controlPanel) {
		
		super(null);
		this.controlPanel = controlPanel;
		
		setOpaque(false);
		addMouseListener(this);
		addMouseMotionListener(this);
		setEmpty();
	}

	/**
	 * reset the current game
	 */
	public void resetCurrentGame() {
		if (buffImage != null) {
			setEmpty();
			
			picturePiecesList = new PicturePiecesImpl(buffImage, x_parts, y_parts,
					getSize());
			
			controlPanel.printPieces("" + picturePiecesList.getPartsCount());
			controlPanel.printSolved("0");
			controlPanel.startCounter();
			repaint();
		}
	}

	/**
	 * Clear all variables
	 * Set empty game
	 */
	public void setEmpty() {
		
		dragModus1 = false;
		dragModus2 = false;
		
		lastDragLocation = new Point();
		repaintRect1 = new Rectangle();
		repaintRect2 = new Rectangle();
		picturePiecesList = new DummyPicturePiecesImpl();
		endMessagePrinted = false;

		controlPanel.printPieces("");
		controlPanel.printSolved("");
		controlPanel.printTime("");

		repaint();
	}

	/**
	 * set the new parameters to play the game
	 */
	public void setGame(BufferedImage buffImage, int x_parts, int y_parts) {
		// clear all variables
		setEmpty();
		
		this.buffImage = buffImage;
		this.x_parts = x_parts;
		this.y_parts = y_parts;

		picturePiecesList = new PicturePiecesImpl(buffImage, x_parts, y_parts,
				getSize());
		controlPanel.printPieces("" + picturePiecesList.getPartsCount());
		controlPanel.printSolved("0");
		controlPanel.startCounter();
		repaint();
	}



	/**
	 * set outline on/off
	 */
	public void setOutline(boolean b) {
		outline = b;
		repaint();
	}

	/**
	 * set shadow on/off
	 */
	public void setShadow(boolean b) {
		shadow = b;
		repaint();
	}

	/**
	 * overwrite paintComponent to paint the pieces
	 */
	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);

		Graphics2D graphics2D = (Graphics2D) graphics;


		// get the first part for enumeration
		PicturePiecesImpl.PuzzlePart part = picturePiecesList.getFirstNode();

		int tx, ty;

		// outline size
		graphics2D.setStroke(new BasicStroke(3));

		while (part != null) {
		//	graphics2D.setColor(Color.black);

			tx = part.locationIn.x - part.boundsIn.x;
			ty = part.locationIn.y - part.boundsIn.y;

			// translate the graphics context to the piece location
			graphics2D.translate(tx, ty);

			// paint the shadow
			if (shadow && picturePiecesList.isSelectedPart(part)) {
				graphics2D.translate(PAINT_SHADOW, PAINT_SHADOW);
				graphics2D.setPaint(shadowColor);
				graphics2D.fill(part.generalPath);
				graphics2D.translate(-PAINT_SHADOW, -PAINT_SHADOW);
			}

			// set the paint
			graphics2D.setPaint(part.puzzlePaint);
			
			// fill the shape of the piece with the piece image part
			graphics2D.fill(part.generalPath);

			// paint the outline
			if (outline) {
				graphics2D.setColor(outlineColor);
				graphics2D.draw(part.generalPath);
			}

			graphics2D.translate(-tx, -ty);
			part = part.next;
		}
		
	}

	/**
	 * If the user clicks one of the pieces, start dragging
	 */
	protected void startDragging(MouseEvent e) {
		if (picturePiecesList.selectPart(e.getPoint())) {
			setCursor(CURSOR_HAND);
			lastDragLocation.setLocation(e.getPoint());
			dragModus1 = true;
			mouseMoved(e);
		}
	}

	/**
	 * The user put the piece on the desctop background
	 */
	protected void stopDragging(MouseEvent e) {

		picturePiecesList.setRedrawRect(repaintRect1);
		picturePiecesList.updatePart();
		picturePiecesList.setRedrawRect(repaintRect2);

		picturePiecesList.deselectPart();

		if (repaintRect1.intersects(repaintRect2)) {
			repaintRect1.add(repaintRect2);
			repaint(repaintRect1.x - PAINT_EXTEND, repaintRect1.y
					- PAINT_EXTEND, repaintRect1.width + PAINT_SHADOW
					+ PAINT_EXTEND + PAINT_EXTEND, repaintRect1.height
					+ PAINT_SHADOW + PAINT_EXTEND + PAINT_EXTEND);
		}
		else {
			repaint(repaintRect1.x - PAINT_EXTEND, repaintRect1.y
					- PAINT_EXTEND, repaintRect1.width + PAINT_SHADOW
					+ PAINT_EXTEND + PAINT_EXTEND, repaintRect1.height
					+ PAINT_SHADOW + PAINT_EXTEND + PAINT_EXTEND);
			repaint(repaintRect2.x - PAINT_EXTEND, repaintRect2.y
					- PAINT_EXTEND, repaintRect2.width + PAINT_SHADOW
					+ PAINT_EXTEND + PAINT_EXTEND, repaintRect2.height
					+ PAINT_SHADOW + PAINT_EXTEND + PAINT_EXTEND);
		}

		// update the solved count in ControlPanel
		controlPanel.printSolved("" + picturePiecesList.getSolvedPartsCount());

		// dragging end
		dragModus1 = false;
		setCursor(CURSOR_DEFAULT);

		// if solved the whole image, show the message one time
		if (!endMessagePrinted
				&& picturePiecesList.getSolvedPartsCount() >= picturePiecesList
						.getPartsCount()) {
			controlPanel.stopCounter();
			JOptionPane.showMessageDialog(
					this,
					"Congratulation!!! You solved the puzzle for "
							+ controlPanel.getGameTime() + ".");
			endMessagePrinted = true;
		}
	}

	/**
	 * From MouseListener
	 */
	public void mouseClicked(MouseEvent e) {
		// drag modus 1 on if not on
		if (dragModus1 == false) {
			startDragging(e);
		}
		// drag modus 1 off if on
		else {
			stopDragging(e);
		}
	}

	/**
	 * From MouseListener, not used
	 */
	public void mouseEntered(MouseEvent e) {
	}

	/**
	 * From MouseListener, not used
	 */
	public void mouseExited(MouseEvent e) {
	}

	/**
	 * From MouseListener, used
	 */
	public void mousePressed(MouseEvent e) {
		if (!dragModus2 && !dragModus1) {
			dragModus2 = true;
		}
	}

	/**
	 * From MouseListener
	 */
	public void mouseReleased(MouseEvent e) {
		if (dragModus2 && !dragModus1) {
			dragModus2 = false;
		}
		else if (dragModus2 && dragModus1) {
			dragModus2 = false;
			stopDragging(e);
		}
	}

	/**
	 * From MouseMotionListener
	 */
	public void mouseDragged(MouseEvent e) {
		// user use the modus 2
		if (dragModus2 && !dragModus1) {
			dragModus1 = true;
			startDragging(e);
		}
		mouseMoved(e);
	}

	/**
	 * From MouseMotionListener
	 */
	public void mouseMoved(MouseEvent e) {

		if (dragModus1) {
			Point point = e.getPoint();

			int dx = point.x - lastDragLocation.x;
			int dy = point.y - lastDragLocation.y;

			// ask the model for the part that has changed and should be
			// repainted
			picturePiecesList.setRedrawRect(repaintRect1);
			// move selected part
			picturePiecesList.moveLocation(dx, dy);
			// ask again
			picturePiecesList.setRedrawRect(repaintRect2);

			// if the repaintRect1 and repaintRect2 intersects repaint only one rectangle
			if (repaintRect1.intersects(repaintRect2)) {
				repaintRect1.add(repaintRect2);
				repaint(repaintRect1.x - PAINT_EXTEND, repaintRect1.y
						- PAINT_EXTEND, repaintRect1.width + PAINT_SHADOW
						+ PAINT_EXTEND + PAINT_EXTEND, repaintRect1.height
						+ PAINT_SHADOW + PAINT_EXTEND + PAINT_EXTEND);
			}
			// if the repaintRect1 and repaintRect2 don't intersects repaint two rectangles
			else {
				repaint(repaintRect1.x - PAINT_EXTEND, repaintRect1.y
						- PAINT_EXTEND, repaintRect1.width + PAINT_SHADOW
						+ PAINT_EXTEND + PAINT_EXTEND, repaintRect1.height
						+ PAINT_SHADOW + PAINT_EXTEND + PAINT_EXTEND);
				repaint(repaintRect2.x - PAINT_EXTEND, repaintRect2.y
						- PAINT_EXTEND, repaintRect2.width + PAINT_SHADOW
						+ PAINT_EXTEND + PAINT_EXTEND, repaintRect2.height
						+ PAINT_SHADOW + PAINT_EXTEND + PAINT_EXTEND);
			}
			lastDragLocation.setLocation(point);
		}
	}
}

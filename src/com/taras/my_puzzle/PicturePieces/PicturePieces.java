package com.taras.my_puzzle.PicturePieces;

import java.awt.Point;
import java.awt.Rectangle;

import com.taras.my_puzzle.PicturePieces.impl.PicturePiecesImpl;

/**
 * @author taras
 */
public interface PicturePieces {
	
	 /**
	  * @return the puzzle part count
	  */
	 public int getPartsCount();
	 
	 /**
	  * @return the solved puzzle part count
	  */
	 public int getSolvedPartsCount();
	 
	 /**
	  * @return the first node
	  */
	 public PicturePiecesImpl.PuzzlePart getFirstNode();
	 
	 /**
	  * gets the array with all parts, the array is never changing for a game, only the list is changing
	  *
	  * @return a array with all parts
	  */
	 public PicturePiecesImpl.PuzzlePart [] getPartArray();
	 
	 /**
	  * is the part the selected part or not
	  *
	  * @param part the part to compare
	  * @return true if the same part or false if not
	  */
	 public boolean isSelectedPart(PicturePiecesImpl.PuzzlePart part);
	 
	 /**
	  * select part at the point
	  *
	  * @param point point for selection
	  * @return true if selected or false if not
	  */
	 public boolean selectPart(Point point);
	 
	 /**
	  * deselect the selected part if one selected
	  */
	 public void deselectPart();
	 
	 /**
	  * ask the model for the part that has changed and should be repainted
	  *
	  * @param repaintRect reference to the Rectangle, that get the repaint rectangle
	  */
	 public void setRedrawRect(Rectangle repaintRect);
	 
	 /**
	  * move the selected part
	  *
	  * @param dx new x position
	  * @param dy new y position
	  */
	 public void moveLocation(int dx, int dy);
	 
	 /**
	  * if the piece is releasing, test if it pass to another part and if true create a new part
	  */
	 public void updatePart();

}

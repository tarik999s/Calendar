package com.taras.my_puzzle.PicturePieces.impl;

import java.awt.Point;
import java.awt.Rectangle;

import com.taras.my_puzzle.PicturePieces.PicturePieces;

/**
 * 
 * @author taras
 * 
 */
public class DummyPicturePiecesImpl implements PicturePieces {

	public DummyPicturePiecesImpl() {
		super();
	}

	public int getPartsCount() {
		return -1;
	}

	public int getSolvedPartsCount() {
		return -1;
	}

	public PicturePiecesImpl.PuzzlePart getFirstNode() {
		return null;
	}

	public PicturePiecesImpl.PuzzlePart[] getPartArray() {
		return null;
	}

	public boolean isSelectedPart(PicturePiecesImpl.PuzzlePart part) {
		return false;
	}

	public boolean selectPart(Point point) {
		return false;
	}

	public void deselectPart() {
	}

	public void setRedrawRect(Rectangle repaintRect) {
	}

	public void moveLocation(int dx, int dy) {
	}

	public void updatePart() {
	}

}

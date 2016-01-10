package com.taras.my_puzzle.PicturePieces.impl;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.util.*;

import com.taras.my_puzzle.PicturePieces.PicturePieces;
import com.taras.my_puzzle.draw.PuzzleDraw;

/**
 * The class divides a picture on puzzle pieces, 
 * creates the shapes and is responsible for assembling puzzle
 * 
 * @author taras
 * 
 */
public class PicturePiecesImpl implements PicturePieces {

	/**
	 * reference to the current image
	 */
	protected BufferedImage buffImage = null;
	/**
	 * current image width
	 */
	protected int imageWidth = -1;
	/**
	 * current image height
	 */
	protected int imageHeight = -1;
	/**
	 * anchor for first and last part of list
	 */
	protected PicturePiecesImpl.PuzzlePart first, last = null;
	/**
	 * array of parts
	 */
	protected PicturePiecesImpl.PuzzlePart pz_array[] = null;
	/**
	 * solved parts
	 */
	protected int puzzleCount = 1;
	/**
	 * selected part to drag. The part can be a member of a group or be self a
	 * group
	 */
	protected PicturePiecesImpl.PuzzlePart dragPart = null;
	/**
	 * selected group to drag. dragPart is a member of this group
	 */
	protected PicturePiecesImpl.PuzzlePart dragGroup = null;

	
	
	/**
	 * Default Constructor
	 */
	public PicturePiecesImpl() {
		super();
	}

	/**
	 * Constructor with parameters:
	 * 
	 * @param buffImage-
	 *            reference to the image
	 * @param x_parts-
	 *            horizontal divirion
	 * @param y_parts-
	 *            vertical division
	 * @param random_size-
	 *            max width and height for the random calculation
	 */
	public PicturePiecesImpl(BufferedImage buffImage, int x_parts, int y_parts,
			Dimension random_size) {
		super();

		this.buffImage = buffImage;

		imageWidth = buffImage.getWidth();
		imageHeight = buffImage.getHeight();

		Random random = new Random();

		// part width and height
		int partWidth = imageWidth / x_parts;
		int partHeight = imageHeight / y_parts;
		// last part width and height
		int lastPartWidth = imageWidth - partWidth * (x_parts - 1);
		int lastPartHeight = imageHeight - partHeight * (y_parts - 1);
		// help variables
		int currentImageWidth = 0;
		int currentImageHeight = 0;
		int i, j, k;

		int random_max_x = random_size.width - partWidth - 60;
		int random_max_y = random_size.height - partHeight - 60;

		// create the array for parts
		pz_array = new PicturePiecesImpl.PuzzlePart[x_parts * y_parts];

		// help variables
		PicturePiecesImpl.PuzzlePart node = null;
		PicturePiecesImpl.PuzzlePart node_prev = null;
		// creates the parts itself
		for (j = k = 0; j < y_parts; j++) {
			// if last vertical part or not
			if (j == y_parts - 1)
				currentImageHeight = lastPartHeight;
			else
				currentImageHeight = partHeight;

			for (i = 0; i < x_parts; i++, k++) {
				// if last horizontal part or not
				if (i == x_parts - 1)
					currentImageWidth = lastPartWidth;
				else
					currentImageWidth = partWidth;

				// creates the part
				node = new PicturePiecesImpl.PuzzlePart(buffImage,
						random.nextInt(random_max_x) + 30,
						random.nextInt(random_max_y) + 30, i * partWidth, j
								* partHeight, currentImageWidth,
						currentImageHeight);

				// add to array
				pz_array[k] = node;

				// reference the list anchors to the neighbourhood
				if (j > 0) {
					node.north = pz_array[k - x_parts];
					pz_array[k - x_parts].south = node;
				}

				// reference the list anchors to the neighbourhood
				if (i > 0) {
					node.west = pz_array[k - 1];
					pz_array[k - 1].east = node;
				}

				// set the first node
				if (node_prev == null) {
					first = node;
				}
				// link the list
				else {
					node_prev.next = node;
					node.prev = node_prev;
				}
				// save the previous node
				node_prev = node;
			}
		}
		// set the last node
		last = node;

		// this code part creates the shape for the puzzle parts
		Rectangle destDraw = null;
		Shape drawShape = null;
		AffineTransform at = null;
		for (i = 0; i < pz_array.length; i++) {
			node = pz_array[i];

			destDraw = new Rectangle(node.boundsIn.x, node.boundsIn.y,
					node.boundsIn.width, node.boundsIn.height);
			drawShape = destDraw;

			at = new AffineTransform();
			at.translate(node.boundsIn.x, node.boundsIn.y);

			if (node.west != null) {
				Area area = new Area(drawShape);
				GeneralPath generalPath = new GeneralPath();

				float ys = (float) (destDraw.height / 2.0 - 5.0);
				float ys2 = ys - 5;
				float center = ys + 5;
				float yf = (float) (destDraw.height / 2.0 + 5.0);
				float yf2 = yf + 5;

				generalPath.moveTo(0, ys);
				generalPath.curveTo(-6, ys, -6, ys2, -13, ys2);
				generalPath.curveTo(-16, ys2, -20, ys, -20, center);
				generalPath.curveTo(-20, yf, -16, yf2, -13, yf2);
				generalPath.curveTo(-6, yf2, -6, yf, 0, yf);

				generalPath.transform(at);

				area.add(new Area(generalPath));
				drawShape = area;

				node.boundsImage.translate(-20, 0);
				node.boundsImage.width += 20;
			}

			if (node.east != null) {
				Area area = new Area(drawShape);
				GeneralPath gp = new GeneralPath();

				float ys = (float) (destDraw.height / 2.0 - 5.0);
				float ys2 = ys - 5;
				float center = ys + 5;
				float yf = (float) (destDraw.height / 2.0 + 5.0);
				float yf2 = yf + 5;

				float x = destDraw.width;

				gp.moveTo(x, ys);
				gp.curveTo(x - 6, ys, x - 6, ys2, x - 13, ys2);
				gp.curveTo(x - 16, ys2, x - 20, ys, x - 20, center);
				gp.curveTo(x - 20, yf, x - 16, yf2, x - 13, yf2);
				gp.curveTo(x - 6, yf2, x - 6, yf, x, yf);

				gp.transform(at);

				area.subtract(new Area(gp));
				drawShape = area;
			}

			if (node.north != null) {
				Area area = new Area(drawShape);
				GeneralPath gp = new GeneralPath();

				float xs = (float) (destDraw.width / 2.0 - 5.0);
				float xs2 = xs - 5;
				float center = xs + 5;
				float xf = (float) (destDraw.width / 2.0 + 5.0);
				float xf2 = xf + 5;

				gp.moveTo(xs, 0);
				gp.curveTo(xs, -6, xs2, -6, xs2, -13);
				gp.curveTo(xs2, -16, xs, -20, center, -20);
				gp.curveTo(xf, -20, xf2, -16, xf2, -13);
				gp.curveTo(xf2, -6, xf, -6, xf, 0);

				gp.transform(at);

				area.add(new Area(gp));
				drawShape = area;

				node.boundsImage.translate(0, -20);
				node.boundsImage.height += 20;
			}

			if (node.south != null) {
				Area area = new Area(drawShape);
				GeneralPath gp = new GeneralPath();

				float xs = (float) (destDraw.width / 2.0 - 5.0);
				float xs2 = xs - 5;
				float center = xs + 5;
				float xf = (float) (destDraw.width / 2.0 + 5.0);
				float xf2 = xf + 5;

				float y = destDraw.height;

				gp.moveTo(xs, y);
				gp.curveTo(xs, y - 6, xs2, y - 6, xs2, y - 13);
				gp.curveTo(xs2, y - 16, xs, y - 20, center, y - 20);
				gp.curveTo(xf, y - 20, xf2, y - 16, xf2, y - 13);
				gp.curveTo(xf2, y - 6, xf, y - 6, xf, y);

				gp.transform(at);

				area.subtract(new Area(gp));
				drawShape = area;
			}

			// creates the GeneralPath
			node.generalPath = new GeneralPath(drawShape);
			// out bounds for part
			node.boundsOut = new Rectangle(node.generalPath.getBounds());
			// out bounds for geometry
			node.shapeBounds = new Rectangle(node.generalPath.getBounds());
			// location of out bounds
			node.updateLocationOut();
			// set the Paint rect of the image
			node.puzzlePaint.setViewRect(node.boundsImage);

		}
	}

	/**
	 * return the puzzle part count
	 * 
	 * @return the puzzle part count
	 */
	public int getPartsCount() {
		return pz_array.length;
	}

	/**
	 * return the solved puzzle part count
	 * 
	 * @return the solved puzzle part count
	 */
	public int getSolvedPartsCount() {
		return puzzleCount;
	}

	/**
	 * return the first node
	 * 
	 * @return the first node
	 */
	public PicturePiecesImpl.PuzzlePart getFirstNode() {
		return first;
	}

	/**
	 * get the array with all parts, the array is never changing for a game,
	 * only the list is changing
	 * 
	 * @return a array with all parts
	 */
	public PicturePiecesImpl.PuzzlePart[] getPartArray() {
		return pz_array;
	}

	/**
	 * is the part the selected part or not
	 * 
	 * @param part
	 *            the part to compare
	 * @return true if the same part or false if not
	 */
	public boolean isSelectedPart(PicturePiecesImpl.PuzzlePart part) {
		return (dragGroup == part);
	}

	/**
	 * select part at the point
	 * 
	 * @param point
	 *            point for selection
	 * @return true if selected or false if not
	 */
	public boolean selectPart(Point point) {
		Rectangle target = new Rectangle();

		PicturePiecesImpl.PuzzlePart nodeGroup = null, nodeGroupPrev = null;
		@SuppressWarnings("unused")
		PicturePiecesImpl.PuzzlePart bestHitGroup = null, bestHitGroupPrev = null;

		PicturePiecesImpl.PuzzlePart node = null, nodePrev = null;
		@SuppressWarnings("unused")
		PicturePiecesImpl.PuzzlePart bestHit = null, bestHitPrev = null;

		// select the part and group of this part
		nodeGroup = first;
		while (nodeGroup != null) {
			nodePrev = null;
			node = nodeGroup;
			while (node != null) {
				target.setFrame(node.locationIn, node.boundsIn.getSize());
				if (target.contains(point)) {
					bestHitPrev = nodePrev;
					bestHit = node;

					bestHitGroupPrev = nodeGroupPrev;
					bestHitGroup = nodeGroup;
				}

				nodePrev = node;
				node = node.nextInGroup;
			}

			nodeGroupPrev = nodeGroup;
			nodeGroup = nodeGroup.next;
		}

		// if found, move group on end of the list, what mean on top on the
		// screen
		if (bestHitGroup != null) {
			nodeGroup = bestHitGroup;

			if (nodeGroup != last) {
				if (nodeGroup.prev != null) {
					nodeGroup.prev.next = nodeGroup.next;
					nodeGroup.next.prev = nodeGroup.prev;
				} else {
					first = nodeGroup.next;
					nodeGroup.next.prev = null;
				}
				nodeGroup.next = null;
				nodeGroup.prev = last;
				last.next = nodeGroup;
				last = nodeGroup;
			}

			dragPart = bestHit;
			dragGroup = nodeGroup;

			return true;
		}

		return false;
	}

	/**
	 * another version only for part, don't used
	 */
	public boolean selectPart__(Point point) {
		Rectangle target = new Rectangle();
		PicturePiecesImpl.PuzzlePart node = null, prevNode = null, bestHit = null, bestHitPrev = null;

		for (int i = 0; i < pz_array.length; i++) {
			node = pz_array[i];
			target.setFrame(node.locationIn, node.boundsIn.getSize());
			if (target.contains(point)) {
				bestHitPrev = prevNode;
				bestHit = node;
			}
		}

		if (bestHit != null) {
			node = bestHit;

			if (node != last) {
				if (bestHitPrev != null) {
					bestHitPrev.next = node.next;
					node.next = null;
					last.next = node;
					last = node;
				} else {
					first = node.next;
					node.next = null;
					last.next = node;
					last = node;
				}
			}
			dragPart = node;
			return true;
		}
		return false;
	}

	/**
	 * deselect the selected part if one selected
	 */
	public void deselectPart() {
		dragPart = null;
		dragGroup = null;
	}

	/**
	 * ask the model for the part that has changed and should be repainted
	 * 
	 * @param repaintRect
	 *            reference to the Rectangle, that get the repaint rectangle
	 */
	public void setRedrawRect(Rectangle repaintRect) {
		if (dragGroup == null)
			repaintRect.setRect(0, 0, 0, 0);
		else
			repaintRect
					.setRect(
							dragGroup.locationOut.x
									- (dragGroup.boundsOut.x - dragGroup.shapeBounds.x),
							dragGroup.locationOut.y
									- (dragGroup.boundsOut.y - dragGroup.shapeBounds.y),
							dragGroup.shapeBounds.width,
							dragGroup.shapeBounds.height);

	}

	/**
	 * move the selected part
	 * 
	 * @param dx
	 *            new x position
	 * @param dy
	 *            new y position
	 */
	public void moveLocation(int dx, int dy) {
		moveLocationGroup(dragGroup, dx, dy);
	}

	/**
	 * move the selected group
	 * 
	 * @param group
	 *            group to move
	 * @param dx
	 *            new x position
	 * @param dy
	 *            new y position
	 */
	protected void moveLocationGroup(PicturePiecesImpl.PuzzlePart group,
			int dx, int dy) {
		PicturePiecesImpl.PuzzlePart node = group;
		// move all parts of group
		while (node != null) {
			node.locationIn.setLocation(node.locationIn.x + dx,
					node.locationIn.y + dy);
			node.updateLocationOut();
			node = node.nextInGroup;
		}
	}

	/**
	 * if the piece is releasing, test if it pass to another part and if true
	 * create a new part
	 */
	public void updatePart() {
		PicturePiecesImpl.PuzzlePart node = dragGroup;

		// for all parts of group
		while (node != null) {
			if (((node.lock & PicturePiecesImpl.PuzzlePart.WEST) != PicturePiecesImpl.PuzzlePart.WEST)
					&& node.west != null) {
				// check if pass
				if ((node.locationIn.x >= (node.west.locationIn.x
						+ node.west.boundsIn.width - 5) && node.locationIn.x <= (node.west.locationIn.x
						+ node.west.boundsIn.width + 5))
						&& (node.locationIn.y >= (node.west.locationIn.y - 5) && node.locationIn.y <= (node.west.locationIn.y + 5))) {
					// move to pass exactly
					moveLocationGroup(
							dragGroup,
							-(node.locationIn.x - (node.west.locationIn.x + node.west.boundsIn.width)),
							-(node.locationIn.y - node.west.locationIn.y));

					// this pieces a locked
					node.lock |= PicturePiecesImpl.PuzzlePart.WEST;
					node.west.lock |= PicturePiecesImpl.PuzzlePart.EAST;

					// update the list and shape of group
					updateGroup(dragGroup, node, node.west);
				}
			}

			if (((node.lock & PicturePiecesImpl.PuzzlePart.EAST) != PicturePiecesImpl.PuzzlePart.EAST)
					&& node.east != null) {
				if ((node.locationIn.x + node.boundsIn.width >= (node.east.locationIn.x - 5) && node.locationIn.x
						+ node.boundsIn.width <= (node.east.locationIn.x + 5))
						&& (node.locationIn.y >= (node.east.locationIn.y - 5) && node.locationIn.y <= (node.east.locationIn.y + 5))) {
					moveLocationGroup(
							dragGroup,
							-(node.locationIn.x - (node.east.locationIn.x - node.boundsIn.width)),
							-(node.locationIn.y - node.east.locationIn.y));

					node.lock |= PicturePiecesImpl.PuzzlePart.EAST;
					node.east.lock |= PicturePiecesImpl.PuzzlePart.WEST;

					updateGroup(dragGroup, node, node.east);
				}
			}

			if (((node.lock & PicturePiecesImpl.PuzzlePart.NORTH) != PicturePiecesImpl.PuzzlePart.NORTH)
					&& node.north != null) {
				if ((node.locationIn.y >= (node.north.locationIn.y
						+ node.north.boundsIn.height - 5) && node.locationIn.y <= (node.north.locationIn.y
						+ node.north.boundsIn.height + 5))
						&& (node.locationIn.x >= (node.north.locationIn.x - 5) && node.locationIn.x <= (node.north.locationIn.x + 5))) {
					moveLocationGroup(
							dragGroup,
							-(node.locationIn.x - node.north.locationIn.x),
							-(node.locationIn.y - (node.north.locationIn.y + node.north.boundsIn.height)));

					node.lock |= PicturePiecesImpl.PuzzlePart.NORTH;
					node.north.lock |= PicturePiecesImpl.PuzzlePart.SOUTH;

					updateGroup(dragGroup, node, node.north);
				}
			}

			if (((node.lock & PicturePiecesImpl.PuzzlePart.SOUTH) != PicturePiecesImpl.PuzzlePart.SOUTH)
					&& node.south != null) {
				if ((node.locationIn.y + node.boundsIn.height >= (node.south.locationIn.y - 5) && node.locationIn.y
						+ node.boundsIn.height <= (node.south.locationIn.y + 5))
						&& (node.locationIn.x >= (node.south.locationIn.x - 5) && node.locationIn.x <= (node.south.locationIn.x + 5))) {
					moveLocationGroup(
							dragGroup,
							-(node.locationIn.x - node.south.locationIn.x),
							-(node.locationIn.y - (node.south.locationIn.y - node.boundsIn.height)));

					node.lock |= PicturePiecesImpl.PuzzlePart.SOUTH;
					node.south.lock |= PicturePiecesImpl.PuzzlePart.NORTH;

					updateGroup(dragGroup, node, node.south);
				}
			}

			// next in group
			node = node.nextInGroup;
		}
	}

	/**
	 * update the list and shape of group
	 * 
	 * @param dragGroup
	 *            group to change
	 * @param node
	 *            node of dragGroup that pass
	 * @param node2
	 *            node of another group that pass
	 */
	protected void updateGroup(PicturePiecesImpl.PuzzlePart dragGroup,
			PicturePiecesImpl.PuzzlePart node,
			PicturePiecesImpl.PuzzlePart node2) {
		// last node of dragGroup
		PicturePiecesImpl.PuzzlePart lastNodeOfGroup = null;
		// first node of another group
		PicturePiecesImpl.PuzzlePart firstNodeOfOtherGroup = null;

		// get the first node of another group
		firstNodeOfOtherGroup = node2;
		while (firstNodeOfOtherGroup.prevInGroup != null)
			firstNodeOfOtherGroup = firstNodeOfOtherGroup.prevInGroup;

		// if the same group
		if (dragGroup == firstNodeOfOtherGroup)
			return;

		// get the last node of dragGroup
		lastNodeOfGroup = dragGroup;
		while (lastNodeOfGroup.nextInGroup != null)
			lastNodeOfGroup = lastNodeOfGroup.nextInGroup;

		// update list
		lastNodeOfGroup.nextInGroup = firstNodeOfOtherGroup;
		firstNodeOfOtherGroup.prevInGroup = lastNodeOfGroup;

		if (firstNodeOfOtherGroup.prev != null)
			firstNodeOfOtherGroup.prev.next = firstNodeOfOtherGroup.next;
		else
			first = firstNodeOfOtherGroup.next;

		if (firstNodeOfOtherGroup.next != null)
			firstNodeOfOtherGroup.next.prev = firstNodeOfOtherGroup.prev;
		else
			last = firstNodeOfOtherGroup.prev;

		// not more needed, this part is in the group
		firstNodeOfOtherGroup.prev = firstNodeOfOtherGroup.next = null;

		// update the geometricaly shape
		dragGroup.generalPath.append(firstNodeOfOtherGroup.generalPath, false);
		dragGroup.shapeBounds = new Rectangle(dragGroup.generalPath.getBounds());
		// update the image rect
		dragGroup.boundsImage.add(firstNodeOfOtherGroup.boundsImage);
		dragGroup.puzzlePaint.setViewRect(dragGroup.boundsImage);

		// for solved count
		if (puzzleCount == 0)
			puzzleCount += 1;
		puzzleCount += 1;
	}

	/**
	 * Static nested class for one puzzle piece
	 */
	public static class PuzzlePart {
		/**
		 * for locking if two pieces pass together
		 */
		public static final int WEST = 0x01;
		/**
		 * for locking if two pieces pass together
		 */
		public static final int EAST = 0x02;
		/**
		 * for locking if two pieces pass together
		 */
		public static final int NORTH = 0x04;
		/**
		 * for locking if two pieces pass together
		 */
		public static final int SOUTH = 0x08;

		/**
		 * location of inner bounds
		 */
		public Point locationIn = new Point();
		/**
		 * location of outer bounds
		 */
		public Point locationOut = new Point();
		/**
		 * inner bounds
		 */
		public Rectangle boundsIn = new Rectangle();
		/**
		 * outer bounds
		 */
		public Rectangle boundsOut = new Rectangle();
		/**
		 * image bounds
		 */
		public Rectangle boundsImage = new Rectangle();
		/**
		 * geometricaly shape
		 */
		public GeneralPath generalPath = null;
		/**
		 * bounds of geometricaly shape
		 */
		public Rectangle shapeBounds = new Rectangle();
		/**
		 * paint for this piece
		 */
		public PuzzleDraw puzzlePaint = null;

		/**
		 * next for the group (horizontal linking)
		 */
		public PuzzlePart next = null;
		/**
		 * prev for the group (horizontal linking)
		 */
		public PuzzlePart prev = null;
		/**
		 * next in group (vertical linking)
		 */
		public PuzzlePart nextInGroup = null;
		/**
		 * prev in group (vertical linking)
		 */
		public PuzzlePart prevInGroup = null;

		/**
		 * reference to the neighbourhood
		 */
		public PuzzlePart north = null;
		/**
		 * reference to the neighbourhood
		 */
		public PuzzlePart south = null;
		/**
		 * reference to the neighbourhood
		 */
		public PuzzlePart west = null;
		/**
		 * reference to the neighbourhood
		 */
		public PuzzlePart east = null;

		/**
		 * if locked to WEST, EAST, NORTH, SOUTH or not
		 */
		public int lock = 0;


		/**
		 * simply constructor
		 * 
		 * @param bimage
		 *            reference to the image
		 * @param lx
		 *            location x of piece
		 * @param ly
		 *            location y of piece
		 * @param plx
		 *            pixel location x of image
		 * @param ply
		 *            pixel location y of image
		 * @param width
		 *            width of piece and image part
		 * @param height
		 *            height of piece and image part
		 */
		public PuzzlePart(BufferedImage bimage, int lx, int ly, int pix,
				int piy, int width, int height) {
			locationIn.setLocation(lx, ly);
			boundsImage.setRect(pix, piy, width, height);
			boundsIn.setRect(pix, piy, width, height);
			puzzlePaint = new PuzzleDraw(bimage, boundsImage);
		}

		/**
		 * update the outer location from inner
		 */
		public void updateLocationOut() {
			locationOut.setLocation(locationIn.x - (boundsIn.x - boundsOut.x),
					locationIn.y - (boundsIn.y - boundsOut.y));
		}

		/**
		 * create a description of this part for debugging
		 * 
		 * @return a string representation of this piece
		 */
		public String toString() {
			return "PuzzlePart[location: " + locationIn.x + ", " + locationIn.y
					+ "; rect: " + boundsIn.x + ", " + boundsIn.y + ", "
					+ boundsIn.width + ", " + boundsIn.height + "]";
		}
	}

}

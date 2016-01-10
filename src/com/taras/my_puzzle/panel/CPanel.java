package com.taras.my_puzzle.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;



/**
 * ControlPanel class
 * 
 * @author taras
 *
 */
@SuppressWarnings("serial")
public class CPanel extends JPanel {


	/**
	 * background color for time
	 */
	protected static final Color textBackground = new Color(120, 0, 0);
	/**
	 * text color for time
	 */
	protected static final Color textForeground = new Color(255, 255, 0);
	/**
	 * the maximal image side length to fit it into the image label
	 */
	protected static final int imageSize = 200;

	/**
	 * image label
	 */
	protected JLabel imagePanel = null;
	/**
	 * all pieces label
	 */
	protected JLabel piecesLabel = null;
	/**
	 * solved pieces label
	 */
	protected JLabel solvedLabel = null;
	/**
	 * time label
	 */
	protected JLabel timeLabel;

	/**
	 * counter for time
	 */
	protected int gameTime = 0;
	/**
	 * timer for the game time. It is a thread.
	 */
	protected Timer gameTimeCounter = new Timer(1000, new TimeCounterListener());
	/**
	 * help variables for time calculation.
	 */
	protected int sec, min, hour, time;
	/**
	 * help variables for time calculation.
	 */
	protected String ssec, smin;

	/**
	 * simply constructor
	 */
	public CPanel() {
		super(new BorderLayout());

		imagePanel = new JLabel();
		imagePanel.setHorizontalAlignment(JLabel.CENTER);

		// panel for centering of the image label
		JPanel center = new JPanel(new GridLayout(1, 1));
		center.setPreferredSize(new Dimension(220, 220));
		CompoundBorder border1 = new CompoundBorder(
				new EmptyBorder(5, 5, 5, 5), new EtchedBorder());
		center.setBorder(new CompoundBorder(border1,
				new EmptyBorder(5, 5, 5, 5)));
		center.add(imagePanel);

		// panel pieces an time labels
		JPanel statistic = new JPanel(new GridBagLayout());
		statistic.setBorder(new CompoundBorder(new EmptyBorder(10, 10, 10, 10),
				new BevelBorder(BevelBorder.LOWERED)));

		GridBagConstraints gridBagConst = new GridBagConstraints();

		gridBagConst.gridx = 0;
		gridBagConst.gridy = 0;
		gridBagConst.fill = GridBagConstraints.BOTH;
		statistic.add(createLabel("Pieces:", JLabel.RIGHT), gridBagConst);

		gridBagConst.gridx = 0;
		gridBagConst.gridy = 1;
		gridBagConst.fill = GridBagConstraints.BOTH;
		statistic.add(createLabel("Solved:", JLabel.RIGHT), gridBagConst);

		gridBagConst.gridx = 0;
		gridBagConst.gridy = 2;
		gridBagConst.fill = GridBagConstraints.BOTH;
		statistic.add(createLabel("Time:", JLabel.RIGHT), gridBagConst);

		gridBagConst.gridx = 1;
		gridBagConst.gridy = 0;
		gridBagConst.fill = GridBagConstraints.BOTH;
		gridBagConst.weightx = 1.0;
		statistic.add(piecesLabel = createLabel("", JLabel.LEFT), gridBagConst);

		gridBagConst.gridx = 1;
		gridBagConst.gridy = 1;
		gridBagConst.fill = GridBagConstraints.BOTH;
		gridBagConst.weightx = 1.0;
		statistic.add(solvedLabel = createLabel("", JLabel.LEFT), gridBagConst);
		gridBagConst.gridx = 1;
		gridBagConst.gridy = 2;
		gridBagConst.fill = GridBagConstraints.BOTH;
		gridBagConst.weightx = 1.0;
		statistic.add(timeLabel = createLabel("", JLabel.LEFT), gridBagConst);

		add(BorderLayout.CENTER, center);
		add(BorderLayout.SOUTH, statistic);
	}

	/**
	 * set the current image, create a scaled version and set it into the
	 * image label
	 * 
	 * @param image - current image
	 */
	public void setImage(Image image) {

		int imageW = image.getWidth(null);
		int imageH = image.getHeight(null);

		int newImageW = 0;
		int newImageH = 0;

		// the image must fit into 200x200 rectangle
		if (imageW >= imageH) {
			newImageW = imageSize;
			newImageH = (int) ((double) imageH * ((double) imageSize / (double) imageW));
		} else {
			newImageW = (int) ((double) imageW * ((double) imageSize / (double) imageH));
			newImageH = imageSize;
		}

		// scaled and set
		Image image_scaled = image
				.getScaledInstance(newImageW, newImageH, Image.SCALE_FAST);
		imagePanel.setIcon(new ImageIcon(image_scaled));
	}

	/**
	 * Creates label and set all properties
	 * 
	 * @param text - label text
	 * @param layout - JLabel.LEFT or JLabel.RIGHT
	 */
	protected JLabel createLabel(String text, int layout) {
		JLabel jLabel = new JLabel(text, layout);
		jLabel.setBorder(new EmptyBorder(0, 5, 0, 2));
		jLabel.setOpaque(true);
		jLabel.setBackground(textBackground);
		jLabel.setForeground(textForeground);
		jLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
		return jLabel;
	}

	/**
	 * Starts the counter at the begin
	 */
	public void startCounter() {
		gameTime = 0;
		printTime("00:00");
		if (gameTimeCounter.isRunning())
			gameTimeCounter.restart();
		else
			gameTimeCounter.start();
	}

	/**
	 * Starts the counter at the end
	 */
	public void stopCounter() {
		gameTimeCounter.stop();
	}

	/**
	 * Convert the millisecond time into a string
	 * 
	 * @return the time as string
	 */
	public String getGameTime() {
		
		time = gameTime;
		sec = time % 60;
		ssec = (sec < 10 ? "0" + sec : "" + sec);
		time = time / 60;
		
		if (time < 1)
			return "00:" + ssec;

		min = time % 60;
		smin = (min < 10 ? "0" + min : "" + min);
		time = time / 60;
		
		if (time < 1)
			return smin + ":" + ssec;

		hour = time;
		return hour + ":" + smin + ":" + ssec;
	}

	/**
	 * Set the text for all pieces label
	 * 
	 * @param str - text for label
	 */
	public void printPieces(String str) {
		if (piecesLabel != null)
			piecesLabel.setText(str);
	}

	/**
	 * Set the text for solved pieces label
	 * 
	 * @param str - text for label
	 */
	public void printSolved(String str) {
		if (solvedLabel != null)
			solvedLabel.setText(str);
	}

	/**
	 * Set the text for time label
	 * 
	 * @param str - text for label
	 */
	public void printTime(String str) {
		if (timeLabel != null)
			timeLabel.setText(str);
	}

	/**
	 * Callback function for Timer
	 */
	protected class TimeCounterListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			gameTime++;
			printTime(getGameTime());
		}
	}

}

package com.taras.my_puzzle.main;

import java.awt.*;

import javax.swing.*;

import com.taras.my_puzzle.panel.PuzzlePanel;

/**
 * Frame class for the Calendar project. Calendar can be a applet, so MainFrame
 * is not important and has no important code.
 * 
 * @author taras
 */
@SuppressWarnings("serial")
public class MainFrame extends JFrame {
	/**
	 * the main and only panel in this frame
	 */
	protected MainPuzzlePanel puzzlePanel = null;

	/**
	 * Main constructor for MainFrame.
	 */
	public MainFrame() {
		super("Calendar [version " + PuzzlePanel.version + "]");

		// get the screen resolution
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();

		int width = screen.width - 50;
		int height = screen.height - 50;

		// set the frame on center of the screen
		setSize(width, height);
		setLocation((screen.width - width) / 2, (screen.height - height) / 2);

		// create the main puzzle panel
		puzzlePanel = new MainPuzzlePanel();

		Container contentPane = getContentPane();
		contentPane.add(puzzlePanel);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);

		// resize
		puzzlePanel.revalidate();
		puzzlePanel.startGame();
	}
}

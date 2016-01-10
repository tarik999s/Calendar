package com.taras.my_puzzle.main;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.border.BevelBorder;

import com.taras.my_puzzle.images.ImageDescription;
import com.taras.my_puzzle.images.PuzzleImages;
import com.taras.my_puzzle.panel.CPanel;
import com.taras.my_puzzle.panel.PuzzlePanel;
import com.taras.my_puzzle.panel.StartDialog;

/**
 * the main panel for Puzzle, it containt the CPanel and the PuzzlePanel
 * 
 * @author taras
 */
@SuppressWarnings("serial")
public class MainPuzzlePanel extends JDesktopPane {
	
	/**
	 * current verion of the program
	 */
	public static final String version = "1.0";
	/**
	 * program description
	 */
	public static String DESCRIPTION = "Puzzle game";
	/**
	 * counter for the user images. Used for names of the images:<br>
	 * <br>
	 * image 1<br>
	 * image 2<br>
	 * image 3<br>
	 * ...
	 * <br>
	 */
	protected static int userImage = 1;
	/**
	 * reference to the ScrollPanel of the MainPuzzlePanel
	 */
	protected JScrollPane pzp_jsp = null;
	/**
	 * reference to the PuzzlePanel
	 */
	protected PuzzlePanel puzzlePanel = null;
	/**
	 * reference to the Frame of the CPanel
	 */
	protected JInternalFrame controlFrame = null;
	/**
	 * reference to the ControlPanel
	 */
	protected CPanel controlPanel = null;
	/**
	 * reference to the StartDialog
	 */
	protected StartDialog startDialog = null;
	/**
	 * reference to the callback function for the menu
	 */
	protected MenuListener menuListener = new MenuListener();
	/**
	 * reference to file chooser to load user images
	 */
	protected JFileChooser jFileChooser = null;
	
	
	/**
	 * Constructor
	 */
	public MainPuzzlePanel() {
		super();

		// create the control panel
		controlPanel = new CPanel();

		// create puzzle panel and the scroller for it
		puzzlePanel = new PuzzlePanel(controlPanel);
		pzp_jsp = new JScrollPane(puzzlePanel);
		pzp_jsp.setBorder(new BevelBorder(BevelBorder.LOWERED));
		pzp_jsp.setOpaque(false);
		pzp_jsp.getViewport().setOpaque(false);

		// set the puzzle panel scroller as default layer, that mean under the
		// CPanel frame
		add(pzp_jsp, JDesktopPane.DEFAULT_LAYER);
		addComponentListener(new PanelResizer());

		// creates the frame for the CPanel
		controlFrame = new JInternalFrame("test", false, false, false, false);
		
		controlFrame.putClientProperty("JInternalFrame.isPalette", Boolean.TRUE);
		controlFrame.getContentPane().add(controlPanel);
		controlFrame.setLocation(10, 10);
		controlFrame.pack();
		add(controlFrame);
		controlFrame.setVisible(true);

		// creates the menu bar for the CPanel
		JMenuBar jMenuBar = new JMenuBar();
		JMenu jMenu = null;

		jMenu = new JMenu("Game");
		jMenu.add(createMenuItem("New", "icons/document.png"));
		jMenu.add(createMenuItem("Open", "icons/folder.png"));
		jMenu.add(createMenuItem("Reset", "icons/reload.png"));
		jMenu.addSeparator();
		jMenu.add(createMenuItem("About", "icons/puzzle.png"));
		jMenu.addSeparator();
		jMenu.add(createMenuItem("Quit", "icons/home.png"));
		jMenuBar.add(jMenu);

		controlFrame.setJMenuBar(jMenuBar);
		startDialog = new StartDialog();
	}
	
	
	/**
	 * starts a new game and reset the system with new parameters
	 */
	public void startGame() {
		if (startDialog.showDialog(this)) {
			Dimension div = startDialog.getSelectedDivision();
			ImageIcon imageIcon = startDialog.getSelectedImage().imageIcon;
			Image image = imageIcon.getImage();
			BufferedImage buffImage = new BufferedImage(imageIcon.getIconWidth(),
					imageIcon.getIconHeight(), BufferedImage.TYPE_INT_RGB);
			buffImage.getGraphics().drawImage(image, 0, 0, null);

			puzzlePanel.setGame(buffImage, div.width, div.height);
			controlPanel.setImage(image);
			puzzlePanel.repaint();
		}
	}
	

	/**
	 * Help function.
	 * Creates a menu item and adds the callback function to it.
	 * 
	 * @param name-
	 *            label for menu item
	 * @param icon-
	 *            icon for menu item
	 * @return the menu item
	 */
	protected JMenuItem createMenuItem(String name, String icon) {
		JMenuItem jMenuItem = new JMenuItem(name, PuzzleImages.getIcon(icon));
		jMenuItem.addActionListener(menuListener);
		return jMenuItem;
	}

	/**
	 * Help function.
	 * Creates a checked menu item and adds the callback function
	 * to it.
	 * 
	 * @param name-
	 *            label for menu item
	 * @param set-
	 *            checked or not
	 * @return the checked menu item
	 */
	protected JCheckBoxMenuItem createMenuItem2(String name, boolean set) {
		JCheckBoxMenuItem jMenuItem = new JCheckBoxMenuItem(name, set);
		jMenuItem.addActionListener(menuListener);
		return jMenuItem;
	}

	/**
	 * load the use image and add it to the StartDialog list
	 */
	public void loadImageForGame() {
		if (jFileChooser == null) {
			jFileChooser = new JFileChooser();
			jFileChooser.addChoosableFileFilter(new ImageFileFilter());
		}

		// show modal dialog
		int state = jFileChooser.showOpenDialog(this);
		File file = jFileChooser.getSelectedFile();

		if (file != null && state == JFileChooser.APPROVE_OPTION) {
			// create the ImageDescription
			ImageIcon imageIcon = new ImageIcon(file.getPath());
			ImageIcon imageIconS = PuzzleImages.scaleIcon(imageIcon, 80);
			ImageDescription id = new ImageDescription(imageIcon, imageIconS, "User image "
					+ (userImage++));
			startDialog.addToImageList(id);
			// start a new game
			startGame();
		}
	}



	
	/**
	 * callback function for the menu items action
	 */
	public class MenuListener implements ActionListener {
		
		public void actionPerformed(ActionEvent e) {
			// start a new game
			if (e.getActionCommand().equals("New")) {
				startGame();
			}
			// load user image
			else if (e.getActionCommand().equals("Open")) {
				loadImageForGame();
			}
			// reset the current game
			else if (e.getActionCommand().equals("Reset")) {
				puzzlePanel.resetCurrentGame();
			}
			// shows the about dialog
			else if (e.getActionCommand().equals("About")) {
				JOptionPane.showMessageDialog(MainPuzzlePanel.this, DESCRIPTION);
			}
			// exit the program
			else if (e.getActionCommand().equals("Quit")) {
				System.exit(0);
			}

			// enable outlines
			puzzlePanel.setOutline(((JCheckBoxMenuItem) e.getSource()).isSelected());

			// enable shadow
			puzzlePanel.setShadow(((JCheckBoxMenuItem) e.getSource()).isSelected());
		}
	}

	/**
	 * needed to resize the puzzle panel scroller, because the layout for the
	 * JDesktopPane is null.
	 */
	public class PanelResizer extends ComponentAdapter {
		
		public void componentShown(ComponentEvent e) {
			pzp_jsp.setSize(getSize());
			pzp_jsp.revalidate();
		}

		public void componentResized(ComponentEvent e) {
			pzp_jsp.setSize(getSize());
			pzp_jsp.revalidate();
		}
	}

	/**
	 * file filter for image loading. Loads only JPEG and GIF
	 */
	public class ImageFileFilter extends javax.swing.filechooser.FileFilter {
		/**
		 * every directory and files with endings *.jpg, *.jpeg, *.gif are
		 * accepted
		 * 
		 * @param f
		 *            the file
		 * @return accepted or not
		 */
		public boolean accept(File f) {
			return (f.getName().endsWith(".jpg")
					|| f.getName().endsWith(".jpeg")
					|| f.getName().endsWith(".png")
					|| f.getName().endsWith(".gif") || f.isDirectory());
		}

		/**
		 * return the string to display it in the file chooser dialog
		 * 
		 * @return the description string
		 */
		public String getDescription() {
			return "Images (*.jpg;*.jpeg;*.gif;*.png)";
		}
	}
}

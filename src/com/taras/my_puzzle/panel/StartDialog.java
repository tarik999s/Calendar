package com.taras.my_puzzle.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;

import com.taras.my_puzzle.images.ImageDescription;
import com.taras.my_puzzle.images.PuzzleImages;

/**
 * StartDialog shows a select dialog for the image choose.
 * 
 * @author taras
 */
public class StartDialog {
	/**
	 * the panel that contains the list and the divide text fields
	 */
	protected JPanel panel = null;
	/**
	 * the list with images
	 */
	@SuppressWarnings("rawtypes")
	protected JList imageList = null;

	/**
	 * value field for x division
	 */
	protected JTextField x_div = null;
	/**
	 * value field for y division
	 */
	protected JTextField y_div = null;

	/**
	 * array with ImageDescription's
	 */
	@SuppressWarnings("rawtypes")
	protected Vector imagesList = null;

	/**
	 * creates the panel only, but don't show it.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public StartDialog() {
		super();

		// create panel
		panel = new JPanel(new BorderLayout());
		panel.setBorder(new EtchedBorder());
		panel.setPreferredSize(new Dimension(400, 400));

		JLabel caption = new JLabel("Please select a image:");
		caption.setBorder(new EmptyBorder(2, 10, 2, 2));
		panel.add(BorderLayout.NORTH, caption);

		// get the images
		imagesList = PuzzleImages.getImages();

		// create list
		imageList = new JList(imagesList);
		imageList.setCellRenderer(new ImageListRenderer());
		JScrollPane jsp_list = new JScrollPane(imageList);
		jsp_list.setBorder(new CompoundBorder(new EmptyBorder(2, 10, 2, 10),
				new BevelBorder(BevelBorder.LOWERED)));
		panel.add(BorderLayout.CENTER, jsp_list);
		imageList.setSelectedIndex(0);

		// create divide fields
		JPanel sizePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		sizePanel.add(new JLabel("Input Divide:"));
		sizePanel.add(x_div = new JTextField("4"));
		sizePanel.add(new JLabel("x"));
		sizePanel.add(y_div = new JTextField("4"));

		x_div.setPreferredSize(new Dimension(50, 20));
		y_div.setPreferredSize(new Dimension(50, 20));

		panel.add(BorderLayout.SOUTH, sizePanel);
	}

	/**
	 * add new images to list
	 * 
	 * @param imageDescription - image to add
	 */
	@SuppressWarnings("unchecked")
	public void addToImageList(ImageDescription imageDescription) {
		imagesList.addElement(imageDescription);
		imageList.setListData(imagesList);
	}

	/**
	 * create a modal dialog and show the panel
	 * 
	 * @param parent
	 *            the parent Component of this dialog
	 * @return true if user select ok or false if cancel
	 */
	public boolean showDialog(Component parent) {
		int ret = JOptionPane.showOptionDialog(parent, panel,
				"Please select a image", JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.QUESTION_MESSAGE, null, null, null);
		return (ret == JOptionPane.OK_OPTION);
	}

	/**
	 * after the dialog is closed return the selected image
	 * 
	 * @return the selected image
	 */
	public ImageDescription getSelectedImage() {
		return (ImageDescription) imageList.getSelectedValue();
	}

	/**
	 * after the dialog is closed return deivision of the image, it is always x
	 * > 1 and y > 1
	 * 
	 * @return the division of the selected image
	 */
	public Dimension getSelectedDivision() {
		int dx = 2;
		int dy = 2;
		try {
			dx = Integer.parseInt(x_div.getText());
			dy = Integer.parseInt(x_div.getText());
			if (dx < 2)
				dx = 2;
			if (dy < 2)
				dy = 2;
		} catch (Exception e) {
		}
		return new Dimension(dx, dy);
	}

	/**
	 * this class renders the image with text line in the selection dialog
	 */
	@SuppressWarnings("rawtypes")
	protected class ImageListRenderer implements ListCellRenderer {

		protected JLabel text = null;
		protected JLabel image = null;

		protected JPanel jpanel = null;

		protected Border selBorder = new LineBorder(new Color(120, 0, 0), 5);
		protected Border normBorder = new EmptyBorder(5, 5, 5, 5);

		protected Color selColor = new Color(254, 254, 204);
		protected Color normColor = Color.white;

		public ImageListRenderer() {

			text = new JLabel();
			text.setOpaque(false);
			text.setBorder(new EmptyBorder(2, 10, 2, 2));

			image = new JLabel();
			image.setHorizontalAlignment(JLabel.CENTER);
			image.setOpaque(false);
			image.setBorder(new EmptyBorder(5, 5, 5, 5));
			image.setPreferredSize(new Dimension(100, 0));

			// creates panel
			jpanel = new JPanel(new BorderLayout());
			jpanel.setBackground(normColor);
			jpanel.setBorder(normBorder);

			jpanel.add(BorderLayout.WEST, image);
			jpanel.add(BorderLayout.CENTER, text);
			jpanel.setPreferredSize(new Dimension(0, 100));
		}

		/**
		 * set image and set text, set borders and background colors and return
		 * the main panel
		 * 
		 * @return the main panel with image and text
		 */
		public Component getListCellRendererComponent(JList jlist,
				Object value, int index, boolean isSelected,
				boolean cellHasFocus) {
			// background color
			if (isSelected)
				jpanel.setBackground(selColor);
			else
				jpanel.setBackground(normColor);
			// border
			if (cellHasFocus)
				jpanel.setBorder(selBorder);
			else
				jpanel.setBorder(normBorder);

			ImageDescription id = (ImageDescription) value;
			image.setIcon(id.imageIcon_scaled);
			text.setText(id.imageName + " (" + id.imageIcon.getIconWidth()
					+ "x" + id.imageIcon.getIconHeight() + " Pixel)");

			return jpanel;
		}
	}
}

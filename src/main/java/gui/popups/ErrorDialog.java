package main.java.gui.popups;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

public class ErrorDialog extends JDialog{

	private static final Logger logger = LoggerFactory.getLogger(ErrorDialog.class.getName());
	private static final long serialVersionUID = 1L;

	public ErrorDialog(String title, String message) {
		logger.info("Initializing ErrorDialog");
		int x = (int) getOwner().getLocationOnScreen().getX() + (getOwner().getWidth() / 2 + getWidth());
		int y = (int) getOwner().getLocationOnScreen().getY() + (getOwner().getHeight() / 2 + getHeight());
		setLocation(x, y);
		JOptionPane.showMessageDialog(this, message,
				title, JOptionPane.ERROR_MESSAGE);
	}
}

package main.io.github.trencmivront.dontforget.gui.popups;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import main.io.github.trencmivront.dontforget.gui.Main;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

public class ErrorDialog extends JDialog{

	private static final Logger logger = LoggerFactory.getLogger(ErrorDialog.class.getName());
	private static final long serialVersionUID = 1L;
	private static final Main main = Main.getMain();

	public ErrorDialog(String title, String message) {
		logger.info("Initializing ErrorDialog");
		int x = (int) main.getLocationOnScreen().getX() + (getOwner().getWidth() / 2 + getWidth());
		int y = (int) main.getLocationOnScreen().getY() + (getOwner().getHeight() / 2 + getHeight());
		setLocation(x, y);
		JOptionPane.showMessageDialog(this, message,
				title, JOptionPane.ERROR_MESSAGE);
	}
}

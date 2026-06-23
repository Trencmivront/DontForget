package main.gui.popup;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

public class ErrorDialog extends JDialog{
	private static final long serialVersionUID = 1L;

	public ErrorDialog(String title, String message) {
		JOptionPane.showMessageDialog(this, message,
				title, JOptionPane.ERROR_MESSAGE);
	}
}

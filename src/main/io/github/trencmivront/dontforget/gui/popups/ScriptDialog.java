package main.io.github.trencmivront.dontforget.gui.popups;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import main.io.github.trencmivront.dontforget.custom.ScriptWriter;
import main.io.github.trencmivront.dontforget.enums.Script;
import main.io.github.trencmivront.dontforget.gui.Main;

public class ScriptDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(ScriptDialog.class.getName());

	private static final Main main = Main.getMain();
	private static final int WIDTH = (int) (main.getWidth() * 0.35);
	private static final int HEIGHT = (int) (main.getHeight() * 0.35);

	private static final Path SCRIPTS_DIR = Path.of(
			System.getProperty("user.home"), ".local", "share", "DontForget", "scripts");

	private final Long taskId;

	/** Notified with {@code true} when script is set, {@code false} when removed. */
	private final Consumer<Boolean> onScriptChanged;

	/** The currently selected OS type ("linux" or "win"). */
	private String selectedSystem = Script.getCurrentOsScript().getValue();
	private JButton osButton;
	private JTextArea scriptTextArea;
	private JButton setButton;
	private JButton removeButton;

	/** Whether a script file already exists for this task when the dialog opens. */
	private boolean scriptExists;

	public ScriptDialog(Long taskId, Consumer<Boolean> onScriptChanged) {
		super(main, "Script", false);

		this.taskId = taskId;
		this.onScriptChanged = onScriptChanged;

		setUndecorated(true);
		setResizable(false);
		setAlwaysOnTop(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		getContentPane().setLayout(new BorderLayout(0, 0));
		getContentPane().add(createTopPanel(), BorderLayout.NORTH);
		getContentPane().add(createScrollableTextArea(), BorderLayout.CENTER);
		getContentPane().add(createFooterPanel(), BorderLayout.SOUTH);

		setSize(new Dimension(WIDTH, HEIGHT));

		// Load existing script content if file is present
		loadExistingScript();

		updateFooterState();

		centerWindow();
		
		setVisible(true);
		revalidate();
		repaint();
	}

	private void centerWindow() {
		int x = (int) main.getLocationOnScreen().getX() + (main.getWidth() / 2 - WIDTH / 2);
		int y = (int) main.getLocationOnScreen().getY() + (main.getHeight() / 2 - HEIGHT / 2);
		setLocation(x, y);
	}

	// ─── Helpers ─────────────────────────────────────────────────────────────

	/**
	 * Searches for an existing script file for this task (any known extension).
	 * Returns its {@link Path} if found, {@code null} otherwise.
	 */
	private Path resolveExistingScript() {
			String ext = Script.getCurrentOsScript().getExtension();
			Path candidate = SCRIPTS_DIR.resolve(taskId + "_script" + ext);
			if (Files.exists(candidate)) {
				return candidate;
			}
		return null;
	}

	/** Loads existing script content and detects the OS type from the file extension. */
	private void loadExistingScript() {
		Path existing = resolveExistingScript();
		if (existing == null) return;

		String fileName = existing.getFileName().toString();
		if (fileName.endsWith(".bat")) {
			selectedSystem = Script.WINDOWS.getValue();
		} else {
			selectedSystem = Script.LINUX.getValue();
		}

		try {
			String content = Files.readString(existing);
			scriptTextArea.setText(content);
		} catch (IOException e) {
			logger.error("Failed to read existing script: {}", e.getMessage());
		}

		if (osButton != null) {
			osButton.setText(selectedSystem);
		}
	}

	// ─── Panels ──────────────────────────────────────────────────────────────
	private JPanel createTopPanel() {
		JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
		topPanel.setBorder(new EmptyBorder(4, 4, 0, 4));

		osButton = new JButton(selectedSystem);
		osButton.putClientProperty("JButton.buttonType", "roundRect");
		osButton.addActionListener(_ -> createOsPopupMenu().show(osButton, 0, osButton.getHeight()));

		topPanel.add(osButton);
		return topPanel;
	}

	private JScrollPane createScrollableTextArea() {
		scriptTextArea = new JTextArea();
		scriptTextArea.setLineWrap(true);
		scriptTextArea.setWrapStyleWord(true);
		scriptTextArea.putClientProperty("JTextArea.placeholderText", "Write terminal commands...");
		scriptTextArea.putClientProperty("JTextField.margin", new Insets(6, 8, 6, 8));

		addTextAreaCaretListener(scriptTextArea);

		JScrollPane scrollPane = new JScrollPane(scriptTextArea);
		scrollPane.setBorder(new EmptyBorder(4, 8, 4, 8));
		return scrollPane;
	}

	private JPanel createFooterPanel() {
		JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 8));
		footerPanel.setBorder(new EmptyBorder(0, 4, 4, 4));

		removeButton = new JButton("Remove");
		removeButton.putClientProperty("JButton.buttonType", "roundRect");
		removeButton.addActionListener(_ -> onRemove());

		setButton = new JButton("Set");
		setButton.putClientProperty("JButton.buttonType", "roundRect");
		setButton.addActionListener(_ -> onSet());

		footerPanel.add(removeButton);
		footerPanel.add(setButton);
		return footerPanel;
	}

	/** Shows/hides the Remove button based on whether a script already exists. */
	private void updateFooterState() {
		removeButton.setVisible(scriptExists);
	}

	// ─── OS popup menu ───────────────────────────────────────────────────────
	
	private JPopupMenu createOsPopupMenu() {
		JPopupMenu popupMenu = new JPopupMenu();

		for (Script s : Script.getScripts()) {
			JMenuItem item = new JMenuItem(s.getValue());
			item.addActionListener(_ -> {
				selectedSystem = s.getValue();
				osButton.setText(selectedSystem);
				logger.info("OS selection changed to: {}", selectedSystem);
			});
			popupMenu.add(item);
		}

		return popupMenu;
	}

	// ─── Actions ─────────────────────────────────────────────────────────────

	private void onSet() {
		String content = scriptTextArea.getText();
		if (content.isBlank()) {
			logger.warn("Script content is empty — aborting set.");
			return;
		}

		try {
			ScriptWriter writer = new ScriptWriter(taskId, content, selectedSystem);
			writer.write();
			logger.info("Script saved for task ID {}", taskId);
			scriptExists = true;
			updateFooterState();
			if (onScriptChanged != null) onScriptChanged.accept(true);
			dispose();
		} catch (IOException e) {
			logger.error("Failed to write script: {}", e.getMessage());
			new ErrorDialog("Script Error", "Could not save script: " + e.getMessage());
		}
	}

	private void onRemove() {
		Path existing = resolveExistingScript();
		if (existing != null) {
			try {
				Files.delete(existing);
				logger.info("Script file deleted: {}", existing);
			} catch (IOException e) {
				logger.error("Failed to delete script file: {}", e.getMessage());
				new ErrorDialog("Script Error", "Could not remove script: " + e.getMessage());
				return;
			}
		}
		scriptExists = false;
		scriptTextArea.setText("");
		updateFooterState();
		if (onScriptChanged != null) onScriptChanged.accept(false);
		dispose();
	}

	// ─── Caret listener (mirrors ProjectWindow) ───────────────────────────────

	private void addTextAreaCaretListener(JTextArea area) {
		area.addCaretListener(e -> {
			try {
				int caretPos = e.getDot();
				int line = area.getLineOfOffset(caretPos);
				int lineStart = area.getLineStartOffset(line);
				int lineEnd = area.getLineEndOffset(line);

				if (caretPos == lineStart || caretPos == lineEnd) {
					refresh();
				}

			} catch (BadLocationException _) {
				logger.warn("Bad caret location");
			}
		});
	}
	
	private void refresh() {
		ScriptDialog.this.revalidate();
		ScriptDialog.this.repaint();
	}

}

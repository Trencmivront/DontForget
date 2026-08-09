package main.io.github.trencmivront.dontforget.gui.windows;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;

import main.io.github.trencmivront.dontforget.custom.SettingsManager;
import main.io.github.trencmivront.dontforget.gui.Main;

public class SettingsWindow extends JDialog {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(SettingsWindow.class.getName());
	private static final Main main = Main.getMain();

	private static SettingsWindow settingsWindow;

//	Live pending values — only written to disk on Apply
	private String pendingTheme;
	private String pendingAAFont;
	private boolean pendingSwingAA;
	private long pendingScale;
	private String pendingFontFamily;
	private int pendingFontSize;
	private boolean pendingRunOnBackground;

//	Selector buttons shown in the UI
	private JButton themeBtn;
	private JButton aaFontBtn;
	private JButton swingAABtn;
	private JButton scaleBtn;
	private JButton fontFamilyBtn;
	private JButton fontSizeBtn;
	private JButton runOnBackgroundBtn;

	public static SettingsWindow getSettingsWindow() {
		return settingsWindow;
	}

	public SettingsWindow() {
//		single-instance: close any existing window first
		if (settingsWindow != null) {
			settingsWindow.dispose();
			settingsWindow = null;
		}

		super(main, "Settings", false);
		logger.info("Initializing SettingsWindow.");
		settingsWindow = this;

		setResizable(false);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(480, 420);
		setLocationRelativeTo(main);

//		Load current values from disk as the starting point
		loadCurrentValues();

//		─── Root content panel ───────────────────────────────────────────────
		JPanel content = new JPanel(new BorderLayout(0, 0));
		content.setBorder(new EmptyBorder(16, 16, 16, 16));
		setContentPane(content);

//		─── Scrollable settings area ─────────────────────────────────────────
		JPanel settingsPanel = new JPanel(new GridBagLayout());
		settingsPanel.setBorder(new EmptyBorder(0, 0, 8, 0));

		GridBagConstraints labelGbc = new GridBagConstraints();
		labelGbc.anchor = GridBagConstraints.WEST;
		labelGbc.insets  = new Insets(6, 4, 6, 12);
		labelGbc.fill    = GridBagConstraints.NONE;
		labelGbc.weightx = 0;

		GridBagConstraints btnGbc = new GridBagConstraints();
		btnGbc.anchor  = GridBagConstraints.EAST;
		btnGbc.insets  = new Insets(6, 0, 6, 4);
		btnGbc.fill    = GridBagConstraints.HORIZONTAL;
		btnGbc.weightx = 1;

		int row = 0;

//		── Section: Appearance ──────────────────────────────────────────────
		addSectionHeader(settingsPanel, "Appearance", row++);

		themeBtn = new JButton();
		themeBtn.putClientProperty("JButton.buttonType", "roundRect");
		addRow(settingsPanel, "UI Theme", themeBtn, row++, labelGbc, btnGbc);
		setupThemeMenu(themeBtn);

		scaleBtn = new JButton();
		scaleBtn.putClientProperty("JButton.buttonType", "roundRect");
		addRow(settingsPanel, "UI Scale", scaleBtn, row++, labelGbc, btnGbc);
		setupScaleMenu(scaleBtn);

		fontFamilyBtn = new JButton();
		fontFamilyBtn.putClientProperty("JButton.buttonType", "roundRect");
		addRow(settingsPanel, "Font Family", fontFamilyBtn, row++, labelGbc, btnGbc);
		setupFontFamilyMenu(fontFamilyBtn);

		fontSizeBtn = new JButton();
		fontSizeBtn.putClientProperty("JButton.buttonType", "roundRect");
		addRow(settingsPanel, "Font Size", fontSizeBtn, row++, labelGbc, btnGbc);
		setupFontSizeMenu(fontSizeBtn);

//		── Separator ────────────────────────────────────────────────────────
		addSeparator(settingsPanel, row++);

//		── Section: Rendering ───────────────────────────────────────────────
		addSectionHeader(settingsPanel, "Rendering", row++);

		aaFontBtn = new JButton();
		aaFontBtn.putClientProperty("JButton.buttonType", "roundRect");
		addRow(settingsPanel, "System AA Font (AWT)", aaFontBtn, row++, labelGbc, btnGbc);
		setupOnOffMenu(aaFontBtn, val -> pendingAAFont = val);

		swingAABtn = new JButton();
		swingAABtn.putClientProperty("JButton.buttonType", "roundRect");
		addRow(settingsPanel, "AA Text (Swing)", swingAABtn, row++, labelGbc, btnGbc);
		setupTrueFalseMenu(swingAABtn, val -> pendingSwingAA = val);

//		── Separator ────────────────────────────────────────────────────────
		addSeparator(settingsPanel, row++);

//		── Section: Behaviour ───────────────────────────────────────────────
		addSectionHeader(settingsPanel, "Behaviour", row++);

		runOnBackgroundBtn = new JButton();
		runOnBackgroundBtn.putClientProperty("JButton.buttonType", "roundRect");
		addRow(settingsPanel, "Run on Background", runOnBackgroundBtn, row++, labelGbc, btnGbc);
		setupTrueFalseMenu(runOnBackgroundBtn, val -> pendingRunOnBackground = val);

//		── Push remaining space down ─────────────────────────────────────────
		GridBagConstraints fillerGbc = new GridBagConstraints();
		fillerGbc.gridx = 0; fillerGbc.gridy = row;
		fillerGbc.gridwidth = 2; fillerGbc.weighty = 1;
		fillerGbc.fill = GridBagConstraints.VERTICAL;
		settingsPanel.add(new JPanel(), fillerGbc);

		JScrollPane scrollPane = new JScrollPane(settingsPanel);
		
		content.add(scrollPane, BorderLayout.CENTER);

//		─── Footer: Revert (left) + Apply (right) ───────────────────────────
		JPanel footer = new JPanel(new BorderLayout());
		footer.setBorder(new EmptyBorder(8, 0, 0, 0));

		JButton revertBtn = new JButton("Revert Changes");
		revertBtn.putClientProperty("JButton.buttonType", "roundRect");
		revertBtn.addActionListener(_ -> {
			loadCurrentValues();
			syncButtonLabels();
		});
		footer.add(revertBtn, BorderLayout.WEST);

		JButton applyBtn = new JButton("Apply");
		applyBtn.putClientProperty("JButton.buttonType", "roundRect");
		applyBtn.setFont(applyBtn.getFont().deriveFont(Font.BOLD));
		applyBtn.addActionListener(_ -> applyAndSave());
		footer.add(applyBtn, BorderLayout.EAST);

		content.add(footer, BorderLayout.SOUTH);

//		Sync button labels to the loaded pending values
		syncButtonLabels();

		revalidate();
		repaint();
		setVisible(true);
		logger.info("SettingsWindow display complete.");
	}

// ─── Helpers: row / header builders ──────────────────────────────────────────

	private void addRow(JPanel panel, String labelText, JButton button,
			int row, GridBagConstraints labelGbc, GridBagConstraints btnGbc) {
		labelGbc.gridx = 0;
		labelGbc.gridy = row;
		JLabel label = new JLabel(labelText);
		label.setFont(label.getFont().deriveFont(Font.PLAIN, 13f));
		panel.add(label, labelGbc);

		btnGbc.gridx = 1;
		btnGbc.gridy = row;
		panel.add(button, btnGbc);
	}

	private void addSectionHeader(JPanel panel, String title, int row) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(10, 0, 2, 0);

		JLabel header = new JLabel(title.toUpperCase());
		header.setFont(header.getFont().deriveFont(Font.BOLD, 11f));
		header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0,
				header.getForeground().darker()));
		header.setHorizontalAlignment(SwingConstants.LEFT);
		panel.add(header, gbc);
	}

	private void addSeparator(JPanel panel, int row) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(4, 0, 4, 0);
		panel.add(new JSeparator(), gbc);
	}

// ─── Menu setup helpers ───────────────────────────────────────────────────────

	private void setupThemeMenu(JButton button) {
		JPopupMenu menu = new JPopupMenu();
		JMenuItem dark  = new JMenuItem("dark");
		JMenuItem light = new JMenuItem("light");
		menu.add(dark);
		menu.add(light);
		button.addActionListener(_ -> menu.show(button, 0, button.getHeight()));
		dark .addActionListener(_ -> { pendingTheme = "dark";  button.setText("dark");  });
		light.addActionListener(_ -> { pendingTheme = "light"; button.setText("light"); });
	}

	private void setupScaleMenu(JButton button) {
		JPopupMenu menu = new JPopupMenu();
		for (long s : new long[]{1, 2, 3, 4}) {
			JMenuItem item = new JMenuItem(String.valueOf(s));
			item.addActionListener(_ -> { pendingScale = s; button.setText(String.valueOf(s)); });
			menu.add(item);
		}
		button.addActionListener(_ -> menu.show(button, 0, button.getHeight()));
	}

	private void setupFontFamilyMenu(JButton button) {
		JPopupMenu menu = new JPopupMenu();
		String[] families = {"Times New Roman", "Dialog", "SansSerif", "Serif", "Monospaced"};
		for (String f : families) {
			JMenuItem item = new JMenuItem(f);
			item.addActionListener(_ -> { pendingFontFamily = f; button.setText(f); });
			menu.add(item);
		}
		button.addActionListener(_ -> menu.show(button, 0, button.getHeight()));
	}

	private void setupFontSizeMenu(JButton button) {
		JPopupMenu menu = new JPopupMenu();
		for (int sz : new int[]{10, 11, 12, 13, 14, 16, 18, 20}) {
			JMenuItem item = new JMenuItem(String.valueOf(sz));
			item.addActionListener(_ -> { pendingFontSize = sz; button.setText(String.valueOf(sz)); });
			menu.add(item);
		}
		button.addActionListener(_ -> menu.show(button, 0, button.getHeight()));
	}

//	Generic on/off popup — calls back with the chosen String
	@FunctionalInterface
	private interface StringConsumer { void accept(String val); }

	private void setupOnOffMenu(JButton button, StringConsumer onChoice) {
		JPopupMenu menu = new JPopupMenu();
		JMenuItem on  = new JMenuItem("on");
		JMenuItem off = new JMenuItem("off");
		menu.add(on);
		menu.add(off);
		button.addActionListener(_ -> menu.show(button, 0, button.getHeight()));
		on .addActionListener(_ -> { onChoice.accept("on");  button.setText("on");  });
		off.addActionListener(_ -> { onChoice.accept("off"); button.setText("off"); });
	}

//	Generic true/false popup
	@FunctionalInterface
	private interface BoolConsumer { void accept(boolean val); }

	private void setupTrueFalseMenu(JButton button, BoolConsumer onChoice) {
		JPopupMenu menu = new JPopupMenu();
		JMenuItem trueItem  = new JMenuItem("true");
		JMenuItem falseItem = new JMenuItem("false");
		menu.add(trueItem);
		menu.add(falseItem);
		button.addActionListener(_ -> menu.show(button, 0, button.getHeight()));
		trueItem .addActionListener(_ -> { onChoice.accept(true);  button.setText("true");  });
		falseItem.addActionListener(_ -> { onChoice.accept(false); button.setText("false"); });
	}

// ─── Load / sync / apply ─────────────────────────────────────────────────────

	private void loadCurrentValues() {
		SettingsManager sm = SettingsManager.getSettingsManager();

		JsonNode theme = sm.get("uiTheme");
		pendingTheme = (theme != null && theme.isTextual()) ? theme.asText() : "dark";

		JsonNode scale = sm.get("uiScale");
		pendingScale = (scale != null && scale.isIntegralNumber()) ? scale.asLong() : 2L;

		JsonNode aaFont = sm.get("awtUseSystemAAFontSettings");
		pendingAAFont = (aaFont != null && aaFont.isTextual()) ? aaFont.asText() : "on";

		JsonNode swingAA = sm.get("swingAAText");
		pendingSwingAA = (swingAA != null) ? swingAA.asBoolean() : true;

		JsonNode family = sm.get("fontFamily");
		pendingFontFamily = (family != null && family.isTextual()) ? family.asText() : "Times New Roman";

		JsonNode size = sm.get("fontSize");
		pendingFontSize = (size != null && size.isIntegralNumber()) ? size.asInt() : 14;

		JsonNode runOnBg = sm.get("runOnBackground");
		pendingRunOnBackground = (runOnBg != null && runOnBg.isBoolean()) ? runOnBg.asBoolean() : false;
	}

	private void syncButtonLabels() {
		if (themeBtn          != null) themeBtn.setText(pendingTheme);
		if (scaleBtn          != null) scaleBtn.setText(String.valueOf(pendingScale));
		if (aaFontBtn         != null) aaFontBtn.setText(pendingAAFont);
		if (swingAABtn        != null) swingAABtn.setText(String.valueOf(pendingSwingAA));
		if (fontFamilyBtn     != null) fontFamilyBtn.setText(pendingFontFamily);
		if (fontSizeBtn       != null) fontSizeBtn.setText(String.valueOf(pendingFontSize));
		if (runOnBackgroundBtn!= null) runOnBackgroundBtn.setText(String.valueOf(pendingRunOnBackground));
	}

	private void applyAndSave() {
		SettingsManager sm = SettingsManager.getSettingsManager();
		sm.set("uiTheme",                    pendingTheme);
		sm.set("uiScale",                    pendingScale);
		sm.set("awtUseSystemAAFontSettings", pendingAAFont);
		sm.set("swingAAText",                pendingSwingAA);
		sm.set("fontFamily",                 pendingFontFamily);
		sm.set("fontSize",                   pendingFontSize);
		sm.set("runOnBackground",            pendingRunOnBackground);
		logger.info("Settings applied and saved.");
//		Inform the user that a restart is required for some settings to take effect
		javax.swing.JOptionPane.showMessageDialog(
				this,
				"Settings saved. Some changes (scale, font, theme, background run) take effect on the next launch.",
				"Settings Applied",
				javax.swing.JOptionPane.INFORMATION_MESSAGE);
	}

}

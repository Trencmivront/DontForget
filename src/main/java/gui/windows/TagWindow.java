package main.java.gui.windows;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.text.AbstractDocument;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

import main.java.controllers.TagController;
import main.java.custom.DocumentFilterFactory;
import main.java.custom.SpringContext;
import main.java.dto.TagDTO;
import main.java.gui.Main;
import main.java.gui.panels.IconColorPanel;

public class TagWindow extends JDialog {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(TagWindow.class.getName());

	private static final int TAG_NAME_MAX_LENGTH = 50;

	private static final Main main = Main.getMain();
	private final TagController tagController = SpringContext.getBean(TagController.class);

	private JTextField tagNameField;
	private IconColorPanel iconColorPanel;

	public TagWindow() {
		super(main, "Create Tag");
		logger.info("Initializing TagWindow.");

		setResizable(false);
		setUndecorated(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		Dimension size = main.getSize();
		int w = Math.min(360, (int) (size.getWidth() * 0.5));
		setSize(new Dimension(w, 100));
		setLocationRelativeTo(main);

		// Content panel
		JPanel contentPanel = new JPanel();
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
		contentPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
		getContentPane().add(contentPanel, BorderLayout.CENTER);

		// Tag name text field
		tagNameField = new JTextField();
		tagNameField.setFont(new Font("Dialog", Font.PLAIN, 14));
		tagNameField.putClientProperty("JTextField.placeholderText", "Tag name");
		tagNameField.putClientProperty("JTextField.margin", new Insets(6, 8, 6, 8));
		((AbstractDocument) tagNameField.getDocument())
				.setDocumentFilter(DocumentFilterFactory.getDocumentFilter(TAG_NAME_MAX_LENGTH));
		contentPanel.add(tagNameField);

//		Color panel
		iconColorPanel = new IconColorPanel();
		contentPanel.add(iconColorPanel);
		
		// Button panel
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));

		JButton addButton = new JButton("Add");
		addButton.putClientProperty("JButton.buttonType", "roundRect");
		addButton.addActionListener(_ -> createTag());

		// Allow submitting with Enter key
		tagNameField.addActionListener(_ -> createTag());

		buttonPanel.add(addButton);
		contentPanel.add(buttonPanel);

		addWindowCloseListener();
		pack();
		setVisible(true);
		tagNameField.requestFocusInWindow();
	}

	private void createTag() {
		String name = tagNameField.getText().trim();

		if (name.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Tag name cannot be empty.", "Validation Error",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		TagDTO tagDTO = new TagDTO();
		tagDTO.setTagName(name);
		tagDTO.setIconColorId(iconColorPanel.getSelectedColor().getIconColorId());

		logger.info("Creating tag: {}", name);
		ResponseEntity<Long> response = tagController.createTag(tagDTO);

		if (response != null && response.getStatusCode().is2xxSuccessful()) {
			logger.info("Tag created successfully with id: {}", response.getBody());
			dispose();
		} else {
			logger.warn("Failed to create tag: {}", name);
			JOptionPane.showMessageDialog(this, "Failed to create tag. Please try again.", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private void addWindowCloseListener() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				if(SearchWindow.getSearchWindow() != null) {
					SearchWindow.getSearchWindow().clear();
				}
			}
		});
	}
	
}

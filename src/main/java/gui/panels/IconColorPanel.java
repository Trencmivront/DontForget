package main.java.gui.panels;

import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.EmptyBorder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

import main.java.controllers.IconColorController;
import main.java.custom.SpringContext;
import main.java.dto.IconColorDTO;

public class IconColorPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(IconColorPanel.class.getName());

	private final IconColorController iconColorController = SpringContext.getBean(IconColorController.class);
	private ButtonGroup buttonGroup = new ButtonGroup();

	public IconColorPanel() {
		// FlowLayout wraps children to the next row when they don't fit horizontally
		setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		setBorder(new EmptyBorder(5, 0, 5, 0));

		JLabel colorLabel = new JLabel("Color: ");
		colorLabel.setFont(new Font("Dialog", Font.BOLD, 14));
		add(colorLabel);
		
		JPanel colorRadioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
		
		listColors(colorRadioPanel);
		add(colorRadioPanel);
	}

	/**
	 * Returns the selected IconColorDTO, or null if none is selected.
	 */
	public IconColorDTO getSelectedColor() {
		Enumeration<AbstractButton> buttons = buttonGroup.getElements();
		while (buttons.hasMoreElements()) {
			JRadioButton rb = (JRadioButton) buttons.nextElement();
			if (rb.isSelected()) {
				Object dto = rb.getClientProperty("iconColorDTO");
				if (dto instanceof IconColorDTO) {
					return (IconColorDTO) dto;
				}
			}
		}
		return null;
	}

	/**
	 * Pre-selects the radio button matching the given iconColorId.
	 */
	public void setSelectedColor(Long iconColorId) {
		if (iconColorId == null) return;
		Enumeration<AbstractButton> buttons = buttonGroup.getElements();
		while (buttons.hasMoreElements()) {
			JRadioButton rb = (JRadioButton) buttons.nextElement();
			Object dto = rb.getClientProperty("iconColorDTO");
			if (dto instanceof IconColorDTO) {
				IconColorDTO color = (IconColorDTO) dto;
				if (iconColorId.equals(color.getIconColorId())) {
					rb.setSelected(true);
					buttonGroup.setSelected(rb.getModel(), true);
					break;
				}
			}
		}
	}

	private void listColors(Container container) {
		logger.info("Listing icon colors.");

		List<IconColorDTO> colors = Collections.emptyList();
		try {
			ResponseEntity<List<IconColorDTO>> response = iconColorController.getIconColors();
			if (response.getBody() != null) {
				colors = response.getBody();
			}
		} catch (Exception e) {
			logger.warn("Failed to fetch icon colors: {}", e.getMessage());
		}

		buttonGroup = new ButtonGroup();
		container.removeAll();

		for (IconColorDTO color : colors) {
			JRadioButton rb = new JRadioButton();
			rb.setBackground(new Color(color.getRed(), color.getGreen(), color.getBlue()));
			rb.setOpaque(true);
			rb.setActionCommand(Long.toString(color.getIconColorId()));
			rb.putClientProperty("iconColorDTO", color);

			buttonGroup.add(rb);
			container.add(rb);
		}

		// Select the first color by default if any exist
		Enumeration<AbstractButton> buttons = buttonGroup.getElements();
		if (buttons.hasMoreElements()) {
			JRadioButton first = (JRadioButton) buttons.nextElement();
			first.setSelected(true);
			buttonGroup.setSelected(first.getModel(), true);
		}
	}
}

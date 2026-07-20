package main.java.custom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.Icon;

public class CustomIcon implements Icon{

	private static final Logger logger = LoggerFactory.getLogger(CustomIcon.class.getName());
	
	private Color color;
	private int height;
	private int width;

	public CustomIcon(Color color, int height, int width) {
		logger.info("Initializing CustomIcon");
		this.color = color;
		this.height = height;
		this.width = width;
	}
	
	@Override
	public int getIconHeight() {
		return height;
	}

	@Override
	public int getIconWidth() {
		return width;
	}
	
	public Color getColor() {
		return color;
	}

	@Override
	public void paintIcon(Component c, Graphics g, int x, int y) {
		Graphics2D g2d = (Graphics2D) g.create();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);                                                                                          
        g2d.setColor(color);                                                                                                                                                               
        g2d.fillOval(x, y, height, width);                                                                                                                                                    
        g2d.dispose();		
	}

}

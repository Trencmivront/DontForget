package main.io.github.trencmivront.dontforget.custom;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;

public class SettingsManager {
	
	private static final Logger logger = LoggerFactory.getLogger(SettingsManager.class.getName());
	
	private final ObjectMapper mapper = new ObjectMapper();
	private File settingsFile = Path.of(
			System.getenv().getOrDefault("XDG_CONFIG_HOME", System.getProperty("user.home") + "/.config"),
			"DontForget", "settings.json").toFile();
	private JsonNode node;
	private static SettingsManager settingsManager;
    
	public ObjectMapper getMapper() {
		return mapper;
	}

	public File getSettingsfile() {
		return settingsFile;
	}
	
	public static SettingsManager getSettingsManager() {
		return settingsManager;
	}
	
	public SettingsManager() {
		settingsManager = this;
		try {
//			initialize the settings file before starting
			createSettingsFile();
			node = mapper.readTree(settingsFile);
		} catch (Exception e) {
			settingsFile = null;
			node = null;
			logger.warn("Exception: {} thrown from {} at constructor.", e.getClass(), this.getClass());
		}
	}
	
	public void validateAndSet() {
	//    	read and insert values
		settingsManager.checkIconSet();
		settingsManager.checkSystemAAFontSet();
		settingsManager.checkSwingAATextSet();
	//    	apply saved scale BEFORE AWT initializes
		settingsManager.checkScaleSet();
	//    	apply theme (dark/light) before AWT initializes
		settingsManager.checkThemeSet();
	//    	apply font after theme so UIManager overrides take effect
		settingsManager.checkFontSet();
	//    	validate and persist run-on-background (value consumed by App.java)
		settingsManager.checkRunOnBackground();
	}
	
	private void checkIconSet() {
		JsonNode raw = settingsManager.get("isIconSet");
//		If missing or not a boolean, treat as false and persist the corrected value
		boolean isIconSet = (raw != null && raw.isBoolean()) ? raw.asBoolean() : false;
		if (!isIconSet) {
			try {
				Path src = Path.of("src/main/resources/dontforget.png");
				Path dest = Paths.get(System.getProperty("user.home"), ".local/share/icons/hicolor/32x32/apps/dontforget.png");
				Files.copy(src, dest, StandardCopyOption.REPLACE_EXISTING);
				logger.info("Icon copied to system icons directory.");
				settingsManager.set("isIconSet", true);
			} catch (Exception e) {
				logger.warn("Could not copy icon to system icons directory, skipping: {}", e.getMessage());
				settingsManager.set("isIconSet", false);
			}
		}
	}
	
//  Returns true if a valid scale was found in settings and applied
    private boolean applyScaleFromSettings() {
    	JsonNode raw = settingsManager.get("uiScale");
    	if (raw == null || !raw.isIntegralNumber()) {
    		logger.warn("uiScale is missing or not an integral number.");
    		return false;
    	}
    	long scaleLong = raw.asLong();
//		check if it is a valid positive scale
    	if (scaleLong <= 0l) {
    		logger.warn("Invalid scale value detected: {}", scaleLong);
    		return false;
    	}
//		Must be set on the main thread BEFORE AWT initializes
    	System.setProperty("sun.java2d.uiScale", String.valueOf(scaleLong));
    	logger.info("Applied uiScale: {}", scaleLong);
    	return true;
    }
    
    private void checkScaleSet() {
    	if (!applyScaleFromSettings()) {
//    		no saved scale — detect after AWT starts and save for next launch
    		SwingUtilities.invokeLater(this::detectAndSaveScale);
    	}
    }
    
//  Runs in invokeLater — safe to use Toolkit here
    private void detectAndSaveScale() {
    	Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
    	final long scale = Math.round(screen.getWidth() / screen.getHeight());
    	settingsManager.set("uiScale", scale);
//  	now apply the freshly detected scale for this session
    	applyScaleFromSettings();
    }
    
    private void checkSystemAAFontSet() {
    	final String defaultValue = "on";
    	JsonNode raw = settingsManager.get("awtUseSystemAAFontSettings");
    	String aaFont = (raw != null && raw.isTextual()) ? raw.asText() : defaultValue;
//    	If it is not a valid value, reset to default
    	if (!(aaFont.equals("on") || aaFont.equals("off"))) {
    		logger.warn("Invalid awtUseSystemAAFontSettings value '{}', resetting to default.", aaFont);
    		aaFont = defaultValue;
    	}
    	System.setProperty("awt.useSystemAAFontSettings", aaFont);
    	settingsManager.set("awtUseSystemAAFontSettings", aaFont);
    }
    
    private void checkSwingAATextSet() {
    	JsonNode raw = settingsManager.get("swingAAText");
    	boolean aaText = (raw != null) ? raw.asBoolean() : true;
    	System.setProperty("swing.aatext", Boolean.toString(aaText));
    	settingsManager.set("swingAAText", aaText);
    }

//  Checks saved font family and size; validates and applies to UIManager, then persists
    private void checkFontSet() {
    	final String defaultFamily = "Times New Roman";
    	final int defaultSize = 14;

    	JsonNode rawFamily = settingsManager.get("fontFamily");
    	JsonNode rawSize   = settingsManager.get("fontSize");

    	String family = (rawFamily != null && rawFamily.isTextual() && !rawFamily.asText().isBlank())
    			? rawFamily.asText() : defaultFamily;
    	int size = (rawSize != null && rawSize.isIntegralNumber()) ? rawSize.asInt() : defaultSize;

//  	Clamp size to a sane range
    	if (size < 8 || size > 72) {
    		logger.warn("Invalid fontSize value '{}', resetting to default.", size);
    		size = defaultSize;
    	}

//  	Validate that the font family is available on this system
    	String[] availableFonts = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
    	boolean familyFound = false;
    	for (String f : availableFonts) {
    		if (f.equalsIgnoreCase(family)) { familyFound = true; break; }
    	}
    	if (!familyFound) {
    		logger.warn("Font family '{}' not found on this system, resetting to default.", family);
    		family = defaultFamily;
    	}

    	UIManager.put("Label.font",  new Font(family, Font.PLAIN, size));
    	UIManager.put("Button.font", new Font(family, Font.BOLD,  size));
    	logger.info("Applied font: {} size {}", family, size);

    	settingsManager.set("fontFamily", family);
    	settingsManager.set("fontSize",   size);
    }

//  Checks saved uiTheme value; switches between FlatMacDarkLaf and FlatMacLightLaf
    private void checkThemeSet() {
    	final String defaultTheme = "dark";
    	JsonNode raw = settingsManager.get("uiTheme");
    	String theme = (raw != null && raw.isTextual()) ? raw.asText() : defaultTheme;
//  	Only "dark" and "light" are valid
    	if (!(theme.equals("dark") || theme.equals("light"))) {
    		logger.warn("Invalid uiTheme value '{}', resetting to default.", theme);
    		theme = defaultTheme;
    	}
    	if (theme.equals("light")) {
    		FlatMacLightLaf.setup();
    	} else {
    		FlatMacDarkLaf.setup();
    	}
    	logger.info("Applied uiTheme: {}", theme);
    	settingsManager.set("uiTheme", theme);
    }

//  Validates and persists the runOnBackground setting (default: false)
    private void checkRunOnBackground() {
    	JsonNode raw = settingsManager.get("runOnBackground");
    	boolean value = (raw != null && raw.isBoolean()) ? raw.asBoolean() : false;
    	settingsManager.set("runOnBackground", value);
    	logger.info("runOnBackground: {}", value);
    }

//  Returns true if the app should keep running when the main window is closed
    public boolean isRunOnBackground() {
    	JsonNode raw = settingsManager.get("runOnBackground");
    	return (raw != null && raw.isBoolean()) ? raw.asBoolean() : false;
    }
	
    public void set(String key, Object value) {
        try {
            ObjectNode rootNode = settingsFile.exists() ?
            		(ObjectNode) mapper.readTree(settingsFile) : 
            			mapper.createObjectNode();
            // put object and let json cast it
            rootNode.putPOJO(key, value);
            mapper.writerWithDefaultPrettyPrinter().writeValue(settingsFile, rootNode);
            logger.info("Saved {} => {}", key, value);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public JsonNode get(String field) {
    	if(node != null)
    		return Objects.requireNonNullElse(node.get(field), NullNode.getInstance());
    	return null;
    }
    
    public void createSettingsFile() {
		if(!settingsFile.exists()) {
			settingsFile.getParentFile().mkdirs();
			try {
				mapper.writerWithDefaultPrettyPrinter().writeValue(settingsFile, mapper.createObjectNode());
			} catch (IOException e) {
				logger.warn("Exception: {} thrown at {} class {} ", e.getClass(), "createSettingsFile", this.getClass());
			}
		}
    }
	
}
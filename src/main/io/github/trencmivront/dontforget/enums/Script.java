package main.io.github.trencmivront.dontforget.enums;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import main.io.github.trencmivront.dontforget.custom.ScriptWriter;

public enum Script {
	NONE(""),
	LINUX("linux"),
	WINDOWS("win");

	private static final Logger logger = LoggerFactory.getLogger(NONE.getClass());
	String value;

	Script(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
	
	public String getExtension() {
		if(this == NONE) return null;
		return value.equals("win") ? ".bat" : ".bs";
	}

	public static List<Script> getScripts(){
		return List.of(Script.NONE, Script.LINUX, Script.WINDOWS);
	}
	
	public static Script getCurrentOsScript() {
		String osName = System.getProperty("os.name");
		if(osName.startsWith("win")) {
			return WINDOWS;
		}
		else {
			return LINUX;
		}
	}
	
	public static Path getTaskScript(Long taskId) {
		// So that we don't get null_script file accidentally
		if(taskId == null) return null;
		Path scriptDir = ScriptWriter.getScriptsDir();
		Path taskScriptPath = scriptDir.resolve(taskId + "_script" + getCurrentOsScript().getExtension());
		if(Files.exists(taskScriptPath)) {
			return taskScriptPath;
		}
		return null;
	}
	
	public static boolean deleteTaskScriptFile(Long taskId){
		Path taskScriptPath = getTaskScript(taskId);
		if(taskScriptPath != null) {
			try {
				Files.delete(taskScriptPath);
				logger.info("Deleted script: {}", taskScriptPath.getFileName());
				return true;
			} catch (IOException _) {
				logger.warn("Error while deleting script: {}", taskScriptPath.getFileName());
			}
		}
		return false;
	}
	
	public static Path getNullScript() {
		Path scriptPath = ScriptWriter.getScriptsDir();
		Path nullFile = scriptPath.resolve("null_script" + getCurrentOsScript().getExtension());
		if(Files.exists(nullFile)) {
			return nullFile;
		}
		return null;
	}
	
	public static boolean deleteNullScriptFile() {
		Path scriptFilePath = getNullScript();
		if(scriptFilePath != null) {
			try {
				Files.delete(scriptFilePath);
				logger.info("null_script file deleted");
				return true;
			} catch (IOException _) {
				logger.warn("Error while deleting null_script file.");
			}
		}
		return false;
	}
}

package main.io.github.trencmivront.dontforget.custom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ScriptExecutor {

    private static final Logger logger = LoggerFactory.getLogger(ScriptExecutor.class.getName());

    private static final Path SCRIPTS_DIR = Path.of(
            System.getProperty("user.home"), ".local", "share", "DontForget", "scripts");

    private static final boolean IS_WINDOWS =
            System.getProperty("os.name").toLowerCase().contains("win");

    private final long taskId;
    private final Path scriptPath;

    /**
     * Constructs a ScriptExecutor for the given task ID.
     * Resolves the script file at {@code ~/.local/share/DontForget/scripts/<id>_script.bs}.
     *
     * @param taskId the ID of the task whose script should be executed
     */
    public ScriptExecutor(Long taskId) {
        this.taskId = taskId;
        this.scriptPath = SCRIPTS_DIR.resolve(taskId + "_script.bs");
    }

    /**
     * Searches for the script file and executes it using a new process.
     * On Windows, runs via {@code cmd.exe /c}; on Linux/macOS, runs via {@code bash}.
     *
     * @return the {@link Process} that was started, or {@code null} if the script was not found
     * @throws IOException if the process could not be started
     */
    public Process execute() throws IOException {
        if (!Files.exists(scriptPath)) {
            logger.warn("Script not found for task ID {}: {}", taskId, scriptPath);
            return null;
        }

        if (!Files.isRegularFile(scriptPath)) {
            logger.warn("Script path is not a regular file for task ID {}: {}", taskId, scriptPath);
            return null;
        }

        logger.info("Executing script for task ID {}: {}", taskId, scriptPath);

        ProcessBuilder builder;
        if (IS_WINDOWS) {
            builder = new ProcessBuilder("cmd.exe", "/c", scriptPath.toAbsolutePath().toString());
        } else {
//        	For Linux systems. MacOS can wait for now.
        	// TODO: Tell MacOS to stop being different.
            builder = new ProcessBuilder("bash", scriptPath.toAbsolutePath().toString());
        }
        builder.redirectErrorStream(false);

        Process process = builder.start();
        logger.info("Script process started for task ID {} with PID {}", taskId, process.pid());
        return process;
    }

}

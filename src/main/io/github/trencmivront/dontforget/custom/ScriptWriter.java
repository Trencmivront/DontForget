package main.io.github.trencmivront.dontforget.custom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.PosixFilePermission;
import java.util.HashSet;
import java.util.Set;

public class ScriptWriter {

    private static final Logger logger = LoggerFactory.getLogger(ScriptWriter.class.getName());

    private static final Path SCRIPTS_DIR = Path.of(
            System.getProperty("user.home"), ".local", "share", "DontForget", "scripts");

    private final Long taskId;
    public static Path getScriptsDir() {
		return SCRIPTS_DIR;
	}

	private final String scriptContent;
    private final String systemType;

    /**
     * Creates a ScriptWriter that will write a script file for the given task.
     *
     * @param taskId        the ID of the task this script belongs to
     * @param scriptContent the script body to write into the file
     * @param systemType    the target system type: {@code "win"} or {@code "linux"}
     */
    public ScriptWriter(Long taskId, String scriptContent, String systemType) {
        this.taskId = taskId;
        this.scriptContent = scriptContent;
        this.systemType = systemType.toLowerCase().trim();
    }

    /**
     * Writes the script to {@code ~/.local/share/DontForget/scripts/<id>_script.<ext>},
     * creating the directory if it does not exist, then verifies the script can be executed.
     *
     * @return the {@link Path} of the written script file
     * @throws IllegalArgumentException if {@code systemType} is not recognised
     * @throws IOException              if writing fails or the script cannot be run
     */
    public Path write() throws IOException {
        String extension = resolveExtension();

        // Create scripts directory if missing
        if (!Files.exists(SCRIPTS_DIR)) {
            Files.createDirectories(SCRIPTS_DIR);
            logger.info("Created scripts directory: {}", SCRIPTS_DIR);
        }

        // Write script content to file
        Path scriptFile = SCRIPTS_DIR.resolve(taskId + "_script" + extension);
        Files.writeString(scriptFile, scriptContent);
        logger.info("Script written for task ID {} at: {}", taskId, scriptFile);

        // Verify the script is runnable before returning
        verifyCanRun(scriptFile);

        return scriptFile;
    }

    /**
     * Maps the {@code systemType} value to a file extension.
     */
    private String resolveExtension() {
        return switch (systemType) {
            case "win", "windows" -> ".bat";
            case "linux", "mac", "macos" -> ".bs";
            default -> throw new IllegalArgumentException(
                    "Unrecognised system type '" + systemType + "'. Use 'win' or 'linux'.");
        };
    }

    /**
     * Verifies the script can actually be executed.
     * <ul>
     *   <li>On Windows: checks that {@code cmd.exe} is reachable.</li>
     *   <li>On Linux/macOS: checks that {@code bash} is reachable and sets
     *       execute permission on the file.</li>
     * </ul>
     *
     * @throws IOException if the interpreter is unavailable or the file is not executable
     */
    private void verifyCanRun(Path scriptFile) throws IOException {
        switch (systemType) {
            case "win", "windows" -> verifyWindowsCanRun(scriptFile);
            default               -> verifyLinuxCanRun(scriptFile);
        }
        logger.info("Script verification passed for task ID {}: {}", taskId, scriptFile);
    }

    private void verifyWindowsCanRun(Path scriptFile) throws IOException {
        // cmd.exe is always available on Windows; just confirm the file is readable
        if (!Files.isReadable(scriptFile)) {
            throw new IOException(
                    "Script file is not readable, cmd.exe cannot execute it: " + scriptFile);
        }
        logger.info("Windows script is readable and ready to run via cmd.exe");
    }

    private void verifyLinuxCanRun(Path scriptFile) throws IOException {
        // Confirm bash interpreter is available
        try {
            Process check = new ProcessBuilder("bash", "--version")
                    .redirectErrorStream(true)
                    .start();
            int exitCode = check.waitFor();
            if (exitCode != 0) {
                throw new IOException("bash interpreter returned a non-zero exit code during verification");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("Interrupted while verifying bash availability", e);
        } catch (IOException e) {
            throw new IOException("bash interpreter not found or cannot be executed: " + e.getMessage(), e);
        }

        // Set execute permission on the script file
        try {
            Set<PosixFilePermission> perms = new HashSet<>(Files.getPosixFilePermissions(scriptFile));
            perms.add(PosixFilePermission.OWNER_EXECUTE);
            perms.add(PosixFilePermission.GROUP_EXECUTE);
            Files.setPosixFilePermissions(scriptFile, perms);
            logger.info("Execute permission set on: {}", scriptFile);
        } catch (UnsupportedOperationException _) {
            logger.warn("POSIX permissions not supported on this filesystem: {}", scriptFile);
        }

        if (!Files.isExecutable(scriptFile)) {
            throw new IOException("Script file is not executable after permission set: " + scriptFile);
        }
    }

}

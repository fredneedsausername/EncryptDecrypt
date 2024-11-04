package fredver.util;

import java.io.File;

public class FailedToCreateDirectoryException extends Exception {

	private static final long serialVersionUID = 3854989969204409561L;

	private File failedToCreateDirectory;
	
	@Override
	public String getMessage() {
		return "Failed to create directory: " + failedToCreateDirectory.getName();
	}
	
	public FailedToCreateDirectoryException(File f) {
		failedToCreateDirectory = f;
	}
	
}

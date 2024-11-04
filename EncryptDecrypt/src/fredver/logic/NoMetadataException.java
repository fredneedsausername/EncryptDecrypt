package fredver.logic;

import java.io.File;

public class NoMetadataException extends Exception {

	private static final long serialVersionUID = -2313431178341452900L;

	private File metadataMissingFile;
	
	@Override
	public String getMessage() {
		return "No metadata in file: " + metadataMissingFile.getName();
	}
	
	public NoMetadataException(File f) {
		metadataMissingFile = f;
	}
	
	
}

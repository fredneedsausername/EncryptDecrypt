package fredver.io.filereading;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

import fredver.constants.Constants;

public class EncryptedFileReader extends FileReader {

	public EncryptedFileReader(File input) throws FileNotFoundException {
		super(input);
	}


	// Here i should use Optional, but, in this case, i prefer efficiency to code safety
	@Override
	public byte[] readChunk() throws IOException {
		
		byte[] read = new byte[Constants.chunkSize];
		
		int numOfBytesRead = inputReader.read(read);
		
		for(int i = 0; i < numOfBytesRead; i++) {
			read[i]++;
		}
		
		if (numOfBytesRead == -1) return null; // Here i should use Optional
		
		return Arrays.copyOf(read, numOfBytesRead);
	}

	
	
}

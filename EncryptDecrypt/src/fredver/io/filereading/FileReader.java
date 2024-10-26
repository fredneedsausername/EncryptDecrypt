package fredver.io.filereading;

import java.io.BufferedInputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

import fredver.constants.Constants;

public class FileReader implements Closeable, AutoCloseable {
	
	protected BufferedInputStream inputReader;
	
	public FileReader(String fileName) throws FileNotFoundException {
		inputReader = new BufferedInputStream(new FileInputStream(new File(fileName)), Constants.chunkSize);
	}
	
	public FileReader(File file) throws FileNotFoundException {
		inputReader = new BufferedInputStream(new FileInputStream(file), Constants.chunkSize);
	}
	
	// Here i should use Optional, but, in this case, i prefer efficiency to code safety
	public byte[] readChunk() throws IOException {
		byte[] read = new byte[Constants.chunkSize];
		int numOfBytesRead = inputReader.read(read);
		if (numOfBytesRead == -1) return null; // Here i should use Optional
		return Arrays.copyOf(read, numOfBytesRead);
	}
	
	public void skipChars(long numCharsSkipped) throws IOException {
		inputReader.skip(numCharsSkipped);
	}

	@Override
	public void close() throws IOException {
		inputReader.close();
	}

}

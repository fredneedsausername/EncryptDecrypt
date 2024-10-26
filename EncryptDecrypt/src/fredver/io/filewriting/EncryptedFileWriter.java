package fredver.io.filewriting;

import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.Flushable;
import java.io.IOException;

import fredver.constants.Constants;

public class EncryptedFileWriter implements Flushable, Closeable, AutoCloseable {

	protected BufferedOutputStream outputStream;
	
	public EncryptedFileWriter(File output) throws FileNotFoundException {
		this.outputStream = new BufferedOutputStream(new FileOutputStream(output), Constants.chunkSize);
	}
	
	public EncryptedFileWriter(FileOutputStream output) {
		this.outputStream = new BufferedOutputStream(output, Constants.chunkSize);
	}
	
	public void writeChunk(byte[] input) throws IOException {
		for(int i = 0; i < input.length; i++) {
			input[i]--;
		}
		outputStream.write(input);
	}
	
	@Override
	public void flush() throws IOException {
		outputStream.flush();
	}
	
	@Override
	public void close() throws IOException {
		outputStream.close();
	}
	
}


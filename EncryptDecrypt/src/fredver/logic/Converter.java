package fredver.logic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import fredver.util.*;
import fredver.constants.Constants;
import fredver.io.filereading.*;
import fredver.io.filewriting.EncryptedFileWriter;

public class Converter {
	
	public static void decrypt(File decryptee, File destination) throws FailedToCreateDirectoryException, NoMetadataException, IOException, FileNotFoundException {
				
		if(!destination.exists()) {
			boolean res = destination.mkdirs();
			if(!res) throw new FailedToCreateDirectoryException(destination);
		}
		
		String metadata = readMetadata(decryptee);
		
		int metadatalen = metadata.length() + Constants.metadataSeparator.length();
		
		File created = new File(destination, metadata);
		
		FileOutputStream output = new FileOutputStream(created);
		
		try(EncryptedFileReader encFileReader = new EncryptedFileReader(decryptee)) {
			try {
				encFileReader.skipChars(metadatalen);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			byte[] bytesArray;
			while(true) {
				if((bytesArray = encFileReader.readChunk()) == null) break;
				output.write(bytesArray);
			}
			output.close();
		}
		
	}
	
	public static void encrypt(File encryptee, File destination, String encryptedFileName) throws FailedToCreateDirectoryException, IOException {
		
		try(FileOutputStream encryptedOutput = new FileOutputStream(new File(destination, encryptedFileName + ".fredenc"))) {
			writeMetadata(encryptee, encryptedOutput);
			try(EncryptedFileWriter outputWriter = new EncryptedFileWriter(encryptedOutput)) {
				try(FileReader fileReaderEncryptee = new FileReader(encryptee)) {
					byte[] bytesArray;
					
					while(true) {
						if((bytesArray = fileReaderEncryptee.readChunk()) == null) break;
						outputWriter.writeChunk(bytesArray);
					}
					
				}
				
			}			
		}
	}
	
	protected static String readMetadata(File input) throws NoMetadataException, IOException {
		
		BufferedReader reader = new BufferedReader(new java.io.FileReader(input));
		
		String read = new String();
		
		for(int i = 0; i < 500; i++) {
			int check = reader.read();
			if(check == -1) {
				reader.close();
				throw new NoMetadataException(input);
			}
			read += (char)check; 
			if(read.contains(Constants.metadataSeparator)) {
				StringBuilder temp = new StringBuilder(read);
				temp.delete(read.length() - Constants.metadataSeparator.length(), read.length());
				
				reader.close();
				return temp.toString();
			}
		}
		reader.close();
		throw new NoMetadataException(input);
	}
	
	protected static void writeMetadata(File metadataProvider, FileOutputStream writer) throws IOException {
		
		char[] charArray = metadataProvider.getName().toCharArray();
		byte[] byteMetadata = new byte[charArray.length];
		for(int i = 0; i < charArray.length; i++) {
			byteMetadata[i] = (byte) charArray[i];
		}
		writer.write(byteMetadata);
		
		char[] charArray2 = Constants.metadataSeparator.toCharArray();
		byte[] byteMetadata2 = new byte[charArray2.length];
		for(int i = 0; i < charArray2.length; i++) {
			byteMetadata2[i] = (byte) charArray2[i];
		}
		writer.write(byteMetadata2);
	}

}

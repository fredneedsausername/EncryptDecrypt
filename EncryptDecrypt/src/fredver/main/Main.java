package fredver.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import fredver.util.FailedToCreateDirectoryException;
import fredver.logic.Converter;
import fredver.logic.NoMetadataException;

public class Main {

	public static void main(String[] args) {
		mainMenu();
	}

	private static void mainMenu() {
		
		@SuppressWarnings("resource") // used for terminal input
		Scanner scanner = new Scanner(System.in);
		
		System.out.println("Welcome to the main menu");
		
		while(true) {
			System.out.println("0: Exit program");
			System.out.println("1: Encrypt file or all files in a directory");
			System.out.println("2: Decrypt file or all files in a directory");
			
			String answer = scanner.nextLine();
			try {
				switch(answer.charAt(0)) {
				case '0':
					System.exit(0);
				case '1': 
					encryptFiles();
					break;
				case '2':
					try {
						decryptFiles();
					} catch (NoMetadataException e) {
						System.out.println("A file does not have metadata");
						System.out.println("Are you sure it was a file encrypted with this program?");
						System.out.println("Anyways, you cannot recover it in this state");
						System.out.println("I'm sorry, but if you had wanted a reliable encryptor you wouldn't have");
						System.out.println("gotten it from a random teenager");
					}
					break;
					
				default:
					continue;
				}
			} catch (FailedToCreateDirectoryException e) {
				System.out.println("Could not create a directory, killing program...");
				System.exit(1);
			} catch (IOException e) {
				System.out.println("I/O error occurred, killing program...");
				System.out.println(e.getMessage());
				System.exit(1);
			}
			
		}
		
	}
	
	private static void encryptFiles() throws FailedToCreateDirectoryException, IOException {
		
		@SuppressWarnings("resource") // used for terminal input
		Scanner scanner = new Scanner(System.in);
		
		while(true) {
			System.out.println("Write the path to the directory or file to encrypt");
			System.out.println("or write 0 to return to previous menu");
			String answer = scanner.nextLine();
			
			if(answer.charAt(0) == '0') return;
			
			File toBeEncrypted = new File(answer);
			
			if(!toBeEncrypted.exists()) {
				System.out.println("File or directory not found");
				System.out.println("Try again");
				continue;
			}
			
			if(toBeEncrypted.isDirectory()) {
				System.out.println("I see it is a directory");
				System.out.println("Where would you like your encrypted files to go?");
				System.out.println("Write the path to the destination folder");
				System.out.println("It will be created for you in case it doesn't exist");
				answer = scanner.nextLine();
				
				File destinationFolder = new File(answer);
				if(destinationFolder.exists()) {
					if(!destinationFolder.isDirectory()) {
						System.out.println("Oh, that isn't a folder");
						System.out.println("Try again");
						continue;
					}
				} else {
					System.out.println("Creating the folder...");
					destinationFolder.mkdirs();
				}
				System.out.println("Encrypting files...");
				File[] arrayToBeEncrypted = toBeEncrypted.listFiles(file -> file.isFile());
				for(File f : arrayToBeEncrypted) {
					Converter.encrypt(f, destinationFolder, f.getName());
				}
				System.out.println("Encrypted!");
				continue;
				
			} 
			if(toBeEncrypted.isFile()) {
				System.out.println("I see it is a file");
				System.out.println("Where would you like your encrypted file to go?");
				System.out.println("Write the path to the destination folder");
				System.out.println("It will be created for you in case it doesn't exist");
				answer = scanner.nextLine();
				
				File destinationFolder = new File(answer);
				if(destinationFolder.exists()) {
					if(!destinationFolder.isDirectory()) {
						System.out.println("Oh, that isn't a folder");
						System.out.println("Try again");
						continue;
					}
				} else {
					System.out.println("Creating the folder...");
					destinationFolder.mkdirs();
				}
				System.out.println("Encrypting file...");
				Converter.encrypt(toBeEncrypted, destinationFolder, toBeEncrypted.getName());
				System.out.println("Encrypted!");
			}
		}
		
		
		
	}
	
	private static void decryptFiles() throws FileNotFoundException, FailedToCreateDirectoryException, NoMetadataException, IOException {
		
		@SuppressWarnings("resource") // used for terminal input
		Scanner scanner = new Scanner(System.in);
		
		while(true) {
			System.out.println("Write the path to the directory or file to decrypt");
			System.out.println("or write 0 to return to previous menu");
			String answer = scanner.nextLine();
			
			if(answer.charAt(0) == '0') return;
			
			File toBeDecrypted = new File(answer);
			
			if(!toBeDecrypted.exists()) {
				System.out.println("File or directory not found");
				System.out.println("Try again");
				continue;
			}
			
			if(toBeDecrypted.isDirectory()) {
				System.out.println("I see it is a directory");
				System.out.println("Where would you like your decrypted files to go?");
				System.out.println("Write the path to the destination folder");
				System.out.println("It will be created for you in case it doesn't exist");
				answer = scanner.nextLine();
				
				File destinationFolder = new File(answer);
				if(destinationFolder.exists()) {
					if(!destinationFolder.isDirectory()) {
						System.out.println("Oh, that isn't a folder");
						System.out.println("Try again");
						continue;
					}
				} else {
					System.out.println("Creating the folder...");
					destinationFolder.mkdirs();
				}
				System.out.println("Decrypting files...");
				File[] arrayToBeDecrypted = toBeDecrypted.listFiles(file -> file.isFile());
				for(File f : arrayToBeDecrypted) {
					Converter.decrypt(f, destinationFolder);
				}
				System.out.println("Decrypted!");
				continue;
				
			} 
			if(toBeDecrypted.isFile()) {
				System.out.println("I see it is a file");
				System.out.println("Where would you like your decrypted file to go?");
				System.out.println("Write the path to the destination folder");
				System.out.println("It will be created for you in case it doesn't exist");
				answer = scanner.nextLine();
				
				File destinationFolder = new File(answer);
				if(destinationFolder.exists()) {
					if(!destinationFolder.isDirectory()) {
						System.out.println("Oh, that isn't a folder");
						System.out.println("Try again");
						continue;
					}
				} else {
					System.out.println("Creating the folder...");
					destinationFolder.mkdirs();
				}
				System.out.println("Decrypting file...");
				Converter.decrypt(toBeDecrypted, destinationFolder);
				System.out.println("Decrypted!");
			}
		}
		
		
		
	}
	
}

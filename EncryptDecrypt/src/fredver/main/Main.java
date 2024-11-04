package fredver.main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;

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
			switch(answer.charAt(0)) {
			case '0':
				System.exit(0);
			case '1': 
				encryptFiles();
				break;
			case '2':
				decryptFiles();
				break;
			default:
				continue;
			}
			
		}
		
	}
	
	private static void encryptFiles() {
		
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
				if(arrayToBeEncrypted == null) handleExceptions(new IOException("I/O Exception while listing files"));
				List<CompletableFuture<Void>> processes = new ArrayList<>();
				
				for(File f : arrayToBeEncrypted) {
					
					CompletableFuture<Void> current = CompletableFuture.runAsync( () -> {
						try {
							Converter.encrypt(f, destinationFolder, f.getName());
						} catch (IOException | FailedToCreateDirectoryException e) {
							throw new RuntimeException(e);
						}
					}).exceptionally(Main::handleExceptions);
					processes.add(current);
				}
				
				CompletableFuture.allOf(processes.toArray(new CompletableFuture[processes.size()])).join();
				
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
				try {
					Converter.encrypt(toBeEncrypted, destinationFolder, toBeEncrypted.getName());
				} catch (IOException | FailedToCreateDirectoryException e) { handleExceptions(e);}
				
				System.out.println("Encrypted!");
			}
		}
		
		
		
	}
	
	private static void decryptFiles() {
		
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
				if(arrayToBeDecrypted == null) handleExceptions(new IOException("I/O Exception while listing files"));
				List<CompletableFuture<Void>> processes = new ArrayList<>();
				
				for(File f : arrayToBeDecrypted) {
					CompletableFuture<Void> current = CompletableFuture.runAsync( () -> {
						try {
							Converter.decrypt(f, destinationFolder);
						} catch (IOException | FailedToCreateDirectoryException | NoMetadataException e ) {
							throw new RuntimeException(e);
						}
					}).exceptionally(Main::handleExceptions);
					processes.add(current);
				}
				CompletableFuture.allOf(processes.toArray(new CompletableFuture[processes.size()])).join();
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
				try {
					Converter.decrypt(toBeDecrypted, destinationFolder);
				} catch (FailedToCreateDirectoryException | NoMetadataException | IOException e) {
					handleExceptions(e);
				}
				System.out.println("Decrypted!");
			}
		}
		
		
		
	}
	
	private static Void handleExceptions(Throwable ex) {
		if(ex instanceof IOException) {
			System.out.println("I/O Exception");
		}
		System.out.println(ex.getMessage());
		return null;
	}
}

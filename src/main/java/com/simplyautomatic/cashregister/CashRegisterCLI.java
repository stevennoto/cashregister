package com.simplyautomatic.cashregister;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * CashRegisterCLI: Main class of cash register coding exercise. Command line
 * interface for cash register.
 * @author stevennoto
 */
public class CashRegisterCLI {
	private final InputStream in;
	private final PrintStream out;
	private final CashRegister cashRegister;
	
	private final static String VALID_COMMAND_REGEX = "([a-zA-Z]+)";
	
	/**
	 * Construct a new Cash Register Command Line Interface
	 * @param inStream input stream to use for user input of commands
	 * @param outStream output stream to print results and errors from commands
	 */
	public CashRegisterCLI(InputStream inStream, PrintStream outStream) {
		in = inStream;
		out = outStream;
		cashRegister = new CashRegister();
	}
	
	/**
	 * Start the cash register, listening to input stream
	 */
	public void start() {
		Scanner scanner = new Scanner(in);
		Pattern commandPattern = Pattern.compile(VALID_COMMAND_REGEX);
		out.println("ready");
		// Main IO loop
		while (true) {
			// Read and validate command
			String input = scanner.nextLine();
			if (input == null) {
				break;
			}
			Matcher commandMatcher = commandPattern.matcher(input.trim());
			if (!commandMatcher.matches()) {
				out.println("Invalid command. Type 'usage' for help.");
				continue;
			}
			
			// Parse and process command
			String command = commandMatcher.group(0);
			switch (command.toLowerCase()) {
				case "help":
				case "usage":
					printUsage();
					break;
				case "show":
					out.println(cashRegister.showInventory());
					break;
				case "exit":
				case "quit":
					out.println("Bye");
					return;
				default:
					out.println("Invalid command. Type 'usage' for help.");
			}
		}
	}
	
	/**
	 * Print usage help for user.
	 */
	private void printUsage() {
		out.println("Usage: type one of the following commands:");
		out.println("\tshow - shows cash register inventory");
		out.println("\tusage - shows usage help");
		out.println("\tquit - exits the program");
	}
	
	/**
	 * Main method, called to start program
	 * @param args 
	 */
	public static void main(String[] args) {
		CashRegisterCLI cashRegCli = new CashRegisterCLI(System.in, System.out);
		cashRegCli.start();
	}
}

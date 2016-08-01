package com.simplyautomatic.cashregister;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
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
	
	/**
	 * Valid command regex: allows lines like "command", "command 1", "command 1 2 3 4 5".
	 */
	private final static String VALID_COMMAND_REGEX = 
			"([a-zA-Z]+)((?:\\s+\\d+)*)";
	
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
			
			// Parse and process command/arguments
			String command = commandMatcher.group(1);
			String argumentsString = commandMatcher.group(2);
			List<Integer> arguments = new ArrayList<>();
			if (argumentsString != null) {
				String[] args = argumentsString.trim().split("\\s+");
				for (String arg : args) {
					try {
						arguments.add(Integer.parseInt(arg));
					} catch (NumberFormatException e) {} // Won't happen since regex matched only digits
				}
			}
			try {
				switch (command.toLowerCase()) {
					case "help":
					case "usage":
						printUsage();
						break;
					case "show":
						out.println(cashRegister.showInventory());
						break;
					case "put":
					case "add":
						cashRegister.putMoney(arguments);
						out.println(cashRegister.showInventory());
						break;
					case "take":
					case "subtract":
						cashRegister.takeMoney(arguments);
						out.println(cashRegister.showInventory());
						break;
					case "change":
						out.println(cashRegister.provideChange(arguments.get(0)));
						break;
					case "exit":
					case "quit":
						out.println("Bye");
						return;
					case "zork":
						out.println("You are standing in an open field west of a white house, with a boarded front door.");
						break;
					default:
						out.println("Invalid command. Type 'usage' for help.");
				}
			} catch (IllegalArgumentException e) {
				out.println("Invalid command. " + e.getMessage());
			} catch (InsufficientMoneyException e) {
				out.println("Insufficient money. " + e.getMessage());
			}
		}
	}
	
	/**
	 * Print usage help for user.
	 */
	private void printUsage() {
		out.println("Usage: type one of the following commands:");
		out.println("\tshow - shows cash register inventory");
		out.println("\tput x y z - puts money in register (x, y, z... are amounts per denomination)");
		out.println("\ttake x y z - takes money from register (x, y, z... are amounts per denomination)");
		out.println("\tchange x - shows change necessary to provide x, and takes money from register.");
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

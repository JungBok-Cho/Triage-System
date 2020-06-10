/*
 * JungBok Cho
 * Triage System
 */
package triage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;


/**
 * This is a program to test a hospital triage system implemented using a heap.
 * 
 * @author JungBok Cho
 * @version 1.0
 */
public class TriageSystem {
	
	// Constant to do this program
	private static boolean keepAsking = true;
		
	// Constant for welcome message
	private static final String MSG_WELCOME = "Welcome to Triage System!" 
						  + "\nEnter help to display commands.";
	
	// Constant for goodbye message
	private static final String MSG_GOODBYE = "\nThank you for using this " + "program!";
	
	// Constant for help message
	private static final String MSG_HELP = "add <priority-code> <patient-name>\r\n"
			+ "            Adds the patient to the triage system.\r\n"
			+ "            <priority-code> must be one of the " + "4 accepted priority codes:\r\n"
			+ "                1. immediate 2. emergency 3. urgent 4. minimal\r\n"
			+ "            <patient-name>: patient's full " + "legal name (may contain spaces)\r\n"
			+ "change <arrivalID> <newPriority>\r\n" + "            Change patient's priority\r\n"
			+ "            <arrivalID>: patient's arrival number\r\n"
			+ "            <newPriority>: changed priority code\r\n"
			+ "next        Announces the patient to be seen next. " + "Takes into account the\r\n"
			+ "            type of emergency and the " + "patient's arrival order.\r\n"
			+ "peek        Displays the patient that is next " + "in line, but keeps in queue\r\n"
			+ "list        Displays the list of all patients " + "that are still waiting\r\n"
			+ "            in the order that they have arrived.\r\n"
			+ "save <file> Save the patient list to a file\r\n"
			+ "load <file> Reads the file and executes " + "the command on each line\r\n"
			+ "help        Displays this menu\r\n" + "quit        Exits the program";

	

	/**
	 * Entry point of the program
	 * 
	 * @param args not used
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		System.out.println(MSG_WELCOME);

		Scanner console = new Scanner(System.in);
		PatientPriorityQueue priQueue = new PatientPriorityQueue();
		while (keepAsking) {
			System.out.print("\ntriage> ");
			String line = console.nextLine();
			processLine(line, priQueue);
		}
		console.close();
		System.out.println(MSG_GOODBYE);
	}
	

	/**
	 * Process the line entered from the user or read from the file
	 * 
	 * @param line      String command to execute
	 * @param priQueue  Priority Queue to operate on
	 * @throws IOException
	 */
	private static void processLine(String line, PatientPriorityQueue priQueue) throws IOException {
		if (!line.isEmpty()) {
			Scanner lineScanner = new Scanner(line);  // Scanner to extract words
			String cmd = lineScanner.next(); 	  // The first is user's command

			// A switch statement could be used on strings, but not all have JDK7
			if (cmd.equals("help")) {
				System.out.println(MSG_HELP);
			} else if (cmd.equals("add")) {
				addPatient(lineScanner, priQueue);
			} else if (cmd.equals("peek")) {
				peekNextPatient(priQueue);
			} else if (cmd.equals("next")) {
				dequeueNextPatient(priQueue);
			} else if (cmd.equals("list")) {
				showPatientList(priQueue);
			} else if (cmd.equals("load")) {
				executeCommandsFromFile(lineScanner, priQueue);
			} else if (cmd.equals("debug")) {
				System.out.println(priQueue.toString());
			} else if (cmd.equals("change")) {
				executeChange(lineScanner, priQueue);
			} else if (cmd.equals("save")) {
				executeSave(lineScanner, priQueue);
			} else if (cmd.equals("quit")) {
				keepAsking = false;
			// Error message
			} else {
				System.out.println("Error: unrecognized command: " + line);
			}
		// Error message
		} else {
			System.out.println("Error: unrecognized command: " + line);
		}
	}
	

	/**
	 * Reads a text file with each command on a separate line and executes the lines
	 * as if they were typed into the command prompt.
	 * 
	 * @param lineScanner  Scanner remaining characters after the command `load`
	 * @param priQueue     priority queue to operate on
	 * @throws IOException
	 */
	private static void executeCommandsFromFile(Scanner lineScanner, PatientPriorityQueue priQueue) throws IOException {
		// read the rest of the line into a single string
		String fileName = lineScanner.nextLine().trim();

		try {
			Scanner file = new Scanner(new File(fileName));
			while (file.hasNext()) {
				final String line = file.nextLine();
				System.out.println("\ntriage> " + line);
				processLine(line, priQueue);
			}
			file.close();
		} catch (FileNotFoundException e) {
			System.out.printf("File %s was not found.%n", fileName);
		}
	}

	
	/**
	 * Save patients information in the file
	 * 
	 * @param lineScanner  Scanner remaining characters after the command `load`
	 * @param priQueue     priority queue to operate on
	 * @throws IOException
	 */
	private static void executeSave(Scanner lineScanner, PatientPriorityQueue priQueue) throws IOException {
		// Check if the line has next
		if (lineScanner.hasNext()) {
			// Scanner to extract words
			String priorityCode = lineScanner.nextLine();
			
			// Create String array of priorityCode
			String[] arrName = priorityCode.split(" ");
			
			// File name
			String fileName = arrName[1];
			
			// ArrayLists to sort
			ArrayList<Patient> arr = priQueue.getCopyList();
			
			// To change priority number to word
			String priorityWord;

			if (arrName.length == 2) {
				Collections.sort(arr);
				try {
					PrintWriter printFile = new PrintWriter(fileName);
					for (int i = 0; i < arr.size(); i++) {
						if (arr.get(i).getPriorityCode() == 1) {
							priorityWord = "immediate";
							printFile.println("add " + priorityWord + " " + arr.get(i).getName());
						} else if (arr.get(i).getPriorityCode() == 2) {
							priorityWord = "emergency";
							printFile.println("add " + priorityWord + " " + arr.get(i).getName());
						} else if (arr.get(i).getPriorityCode() == 3) {
							priorityWord = "urgent";
							printFile.println("add " + priorityWord + " " + arr.get(i).getName());
						} else if (arr.get(i).getPriorityCode() == 4) {
							priorityWord = "minimal";
							printFile.println("add " + priorityWord + " " + arr.get(i).getName());
						}
					}
					printFile.close();
					System.out.println("Saved " + arr.size() + " patients to file " + fileName);
				// throwing exception
				} catch (FileNotFoundException e) {
					System.out.printf("File %s was not found.%n", fileName);
				}
			// Error message
			} else {
				System.out.println("Error: not valid file name");
			}
		// Error message
		} else {
			System.out.println("Error: not valid file name");
		}
	}

	
	/**
	 * Displays the next patient in the waiting room that will be called.
	 * 
	 * @param priQueue priority queue to operate on
	 */
	private static void peekNextPatient(PatientPriorityQueue priQueue) {
		if (priQueue.peek() == null) {
			System.out.println("There are no patients in the waiting area.");
		} else {
			System.out.println("Highest priority patient to be called next: "
							    + priQueue.peek().getName());
		}
	}
	

	/**
	 * Displays the list of patients in the waiting room.
	 * 
	 * @param priQueue priority queue to operate on
	 */
	private static void showPatientList(PatientPriorityQueue priQueue) {
		String priorityWord = ""; // To change priority number to word
		System.out.println("# patients waiting: " + priQueue.size() + "\n");
		System.out.println("  Arrival #   Priority Code   Patient Name\n" 
				    + "+-----------+---------------+--------------+");
		ArrayList<Patient> temp = priQueue.getPatientList();

		// Change priority codes to words
		for (int i = 0; i < temp.size(); i++) {
			if (temp.get(i).getPriorityCode() == 1) {
				priorityWord = "immdediate";
			} else if (temp.get(i).getPriorityCode() == 2) {
				priorityWord = "emergency";
			} else if (temp.get(i).getPriorityCode() == 3) {
				priorityWord = "urgent";
			} else if (temp.get(i).getPriorityCode() == 4) {
				priorityWord = "minimal";
			}
			System.out.printf("     %-2d       %-10s      %-50s\n", 
					   temp.get(i).getArrivalOrder(), priorityWord,
					   temp.get(i).getName());
		}
	}

	
	/**
	 * Removes a patient from the waiting room and displays the name on the screen.
	 * 
	 * @param priQueue  Priority queue to operate on
	 */
	private static void dequeueNextPatient(PatientPriorityQueue priQueue) {
		if (priQueue.size() == 0) {
			System.out.println("There are no patients in the waiting area.");
		} else {
			System.out.println("This patient will now be seen: " 
					   + priQueue.dequeue().getName());
		}
	}

	
	/**
	 * Adds the patient to the waiting room.
	 * 
	 * @param lineScanner  Scanner with remaining chars after the command
	 * @param priQueue     priority queue to operate on
	 */
	private static void addPatient(Scanner lineScanner, PatientPriorityQueue priQueue) {
		if (lineScanner.hasNext()) {
			// Get a String line
			String priorityCode = lineScanner.nextLine();
			
			// Put the line in the array
			String[] arr = priorityCode.split(" ");
			
			// To store patient name
			String patientName = "";
			
			boolean printName = false;

			if (arr.length >= 3) {
				// Get patient name
				for (int i = 2; i < arr.length; i++) {
					if (i != arr.length - 1) {
						patientName += arr[i] + " ";
					} else {
						patientName += arr[i];
					}
				}
				// Add new patients
				if (arr[1].toLowerCase().equals("immediate")) {
					priQueue.addPatient(1, patientName);
					printName = true;
				} else if (arr[1].toLowerCase().equals("emergency")) {
					priQueue.addPatient(2, patientName);
					printName = true;
				} else if (arr[1].toLowerCase().equals("urgent")) {
					priQueue.addPatient(3, patientName);
					printName = true;
				} else if (arr[1].toLowerCase().equals("minimal")) {
					priQueue.addPatient(4, patientName);
					printName = true;
				// Error Message
				} else {
					System.out.println("Error: invalid priority level code");
					printName = false;
				}
				if (printName) {
					System.out.println("Added patient \"" + patientName 
							    + "\" to the priority system");
				}
			// Error Message
			} else {
				System.out.println("Error: No patient name provided");
			}
		// Error Message
		} else {
			System.out.println("Error: No priority level code provided");
		}
	}

	
	/**
	 * Change priorityCode of a Patient object
	 * 
	 * @param lineScanner  Scanner with remaining chars after the command
	 * @param priQueue     Priority queue to operate on
	 */
	private static void executeChange(Scanner lineScanner, PatientPriorityQueue priQueue) {
		if (lineScanner.hasNext()) {
			// Get a String line
			String priorityCode = lineScanner.nextLine();
			
			// Put the line in the array
			String[] arr = priorityCode.split(" ");
			
			// Get shallow copy
			ArrayList<Patient> temp = priQueue.getPatientList();

			if (arr.length >= 3) {
				// Get the target's index in the ArrayList
				int patientNum = getPatientToUpdate(arr, temp);
				
				// Change the object
				if (patientNum != -1) {
					// Get the arrival number
					int arrivalNum = Integer.parseInt(arr[1]);
					if (arr[2].toLowerCase().equals("immediate")) {
						priQueue.change(patientNum, 1, arrivalNum);
					} else if (arr[2].toLowerCase().equals("emergency")) {
						priQueue.change(patientNum, 2, arrivalNum);
					} else if (arr[2].toLowerCase().equals("urgent")) {
						priQueue.change(patientNum, 3, arrivalNum);
					} else if (arr[2].toLowerCase().equals("minimal")) {
						priQueue.change(patientNum, 4, arrivalNum);
					}
				}
			// Error message
			} else {
				System.out.println("Error: No priority code given.");
			}
		// Error message
		} else {
			System.out.println("Error: No patient id provided");
		}
	}

	
	/**
	 * Get the index of patient that needs to update
	 * 
	 * @param arr   Clue to find the patient that needs to update
	 * @param temp  Shallow copy of patients arraylist
	 * @return Return the index of patient that needs to update, Return -1 if cannot
	 *         find
	 */
	private static int getPatientToUpdate(String[] arr, ArrayList<Patient> temp) {
		boolean stopLoop = true;  // To stop the loop
		int arrivalOrder;		  // Arrival order
		int targetPatient = -1;   // index of the target patient

		// Check error
		if (Character.isLetter(arr[1].charAt(0))) {
			System.out.println("Error: no patient with the given id was found");
			return -1;
		}
		
		// Get the arrival order
		arrivalOrder = Integer.parseInt(arr[1]);
		
		// Find the target patient
		for (int i = 0; i < temp.size() && stopLoop; i++) {
			if (arrivalOrder == temp.get(i).getArrivalOrder()) {
				stopLoop = false;
				targetPatient = i;
			}
		}
		
		// Return the index of the target patient
		if (stopLoop == false) {
			if (arr[2].toLowerCase().equals("immediate")) {
				System.out.println("Changed patient \"" + temp.get(targetPatient).getName() 
									+ "\"\'s priority to immediate");
				return targetPatient;
			} else if (arr[2].toLowerCase().equals("emergency")) {
				System.out.println("Changed patient \"" + temp.get(targetPatient).getName() 
									+ "\"\'s priority to emergency");
				return targetPatient;
			} else if (arr[2].toLowerCase().equals("urgent")) {
				System.out.println("Changed patient \"" + temp.get(targetPatient).getName() 
									+ "\"\'s priority to urgent");
				return targetPatient;
			} else if (arr[2].toLowerCase().equals("minimal")) {
				System.out.println("Changed patient \"" + temp.get(targetPatient).getName() 
									+ "\"\'s priority to minimal");
				return targetPatient;
			// Error message
			} else {
				System.out.println("Error: invalid priority level code");
				return -1;
			}
		// Error message
		} else {
			System.out.println("Error: no patient with the given id was found");
			return -1;
		}
	}

}

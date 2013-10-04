/***************************************************************
 * Name: Sebastian Demian
 * 
 * About the class:
 * This class parses a csv file (either default or specified by the user)
 * and builds a Sybmol Table with the dates as keys and the Closing Price as values.
 * It then asks the user to input a date and retrieves the corresponding value if it exists.
 * If it does not exist, it offers different alternatives.
 * To exit the application press Ctrl + Z (Win) or Ctrl + D (Mac)
 *
 ***************************************************************/


import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class DijaLookup {

	private static String defaultFileName = "data/DJIA.csv";

	//output format for printing date
	private static SimpleDateFormat ft = new SimpleDateFormat("MM/dd/yyyy"); //format for output
	private static SimpleDateFormat sdf = new SimpleDateFormat("M-d-yyyy"); //format for input

	private static BinarySearchST<Date, Double> table = new BinarySearchST<Date, Double>();

	//convert csv string to Date object
	private static Date convert2Date(String a) throws ParseException, RuntimeException{ 
		Date s = new SimpleDateFormat("dd-MMM-yyyy").parse(a);
		return s;
	}
	//convert input string to Date object
	private static Date convertInput2Date(String a) throws ParseException, RuntimeException{
		Date s = new SimpleDateFormat("MM-dd-yyyy").parse(a);
		if (!sdf.format(s).equals(a)) System.out.println("No such date: "+a);
		return s;
	}
	//returns false if date is invalid or if date is in the future
	private static boolean verifyDate(String a) throws ParseException, RuntimeException{
		Date s = new SimpleDateFormat("MM-dd-yyyy").parse(a);
		Date currentDate = new Date();
		if (/*<magic>*/!sdf.format(s).equals(a)/*</magic>*/ || s.after(currentDate)){
			System.out.println("No such date: "+a);
			return false;
		}
		return true;
	}


	private static void printInstructions(){
		System.out.println("Get the Dow Jones Industrial Average Adjusted Closing Values");
		System.out.println("Input name of a data file whose input lines are of the form");
		System.out.println("date, open, hi, lo, closing, volume, adjusted closing");
		System.out.println();
		System.out.println("Then enter any date to get the adjusted closing value or ctrl-z to exit");
		System.out.println();
	}

	private static void chooseFile(){
		Scanner scan = new Scanner(System.in);
		System.out.println("Input file (default is ../algs4/data/DJIA.csv): ");
		String input = scan.nextLine();
		if(!input.equals("")){ defaultFileName = input; } 
	}

	//parses entire file and builds the table
	private static void buildTable() throws FileNotFoundException, Exception {
		File file = new File(defaultFileName);
		Scanner in = new Scanner(file);

		System.out.println("Building Table ... ");
		Stopwatch timer = new Stopwatch();

		while(in.hasNextLine()){
			String[] values = in.nextLine().split(",");
			Date a = convert2Date(values[0]);	
			Double b = Double.parseDouble(values[6]);
			table.put(a, b);
		}
		double time = timer.elapsedTime();
		System.out.println("Done (Table built in " + time + " seconds).");
	}

	private static void start(){
		Scanner loop = new Scanner(System.in);
		System.out.println("Enter a date (mm-dd-yyyy):");

		while(loop.hasNextLine()){
			String input = loop.nextLine();

			try {
				
				if(verifyDate(input)==false){
					System.out.println("Enter a date (mm-dd-yyyy):");
					continue;}
				
				Date k = convertInput2Date(input);

				if(table.get(k) != null){
					System.out.println("Date: "+ ft.format(k)+", Closing DJIA: "+ table.get(k));
				} 
				else {
					System.out.print("No data for the date: "+ ft.format(k) +". ");
					if(table.ceiling(k) != null){
						System.out.println("Next date is: "+ ft.format(table.ceiling(k)));
						System.out.println("Date: "+ ft.format(table.ceiling(k))+", Closing DJIA: "+ table.get(table.ceiling(k)));
					} else {
						System.out.println("The date is later than the last date in the file ("+ft.format(table.max())+").");
					}
				}
			} catch (ParseException e) {
				System.out.println("Invalid Date Format: "+ input);
			
				//exit with Control + Z or Control + D
			} catch (NoSuchElementException e){
				break;
			}

			System.out.println("Enter a date (mm-dd-yyyy):");
		}
		System.out.println("Good bye !");
	}



	public static void main(String[] args) throws FileNotFoundException, Exception{

		printInstructions();
		chooseFile();
		buildTable();
		start();
	}
}
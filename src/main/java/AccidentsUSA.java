import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import com.vividsolutions.jts.io.ParseException;

import core.load_data.DataStoreInfo;

public class AccidentsUSA {

	public static Map<String, Integer> commonHeader;

	public static void readFile(String fileName) {
		// Location of file to read
		File file = new File(fileName);

		try {
			Scanner scanner = new Scanner(file);

			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				String[] parts = line.split(";");

				System.out.println(parts[0] +  ": " + parts.length);
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void addAccidentFile(String inFile, String outFile) {

//		System.out.println("starting...");
		
		PrintWriter out;
		try {
			out = new PrintWriter(new BufferedWriter(new FileWriter(outFile, true))); 

			File file = new File(inFile);
			Scanner scanner = new Scanner(file);
			String line = scanner.nextLine();

			//Header
			String[] header = line.split(";");
			
			
			String[] toAdd = new String[commonHeader.size()];

			System.out.println(header.length);
			
			int j=0;
			
			//Records
			while(scanner.hasNext()) {
				String[] raw = scanner.nextLine().split(";");

//				j++;
//				System.out.println(j);

				for(int i = 0; i < raw.length; i++) {
					
					
					int pos;
					if(commonHeader.get(header[i].toLowerCase())!= null)
						pos = commonHeader.get(header[i].toLowerCase());
					else continue;
						
					toAdd[pos] = raw[i];
				}

				String newLine = "";
				for(int i = 0; i < toAdd.length; i++) {				
					newLine+= (i == toAdd.length - 1)  ? toAdd[i] : toAdd[i] + ";";
				}
				out.println(newLine);
			}
			scanner.close();
			out.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		
	}

	private static void fillHeader(String fileName) {
		commonHeader = new HashMap<String, Integer>(); //2007 porque era o que continha todos os outros headers

		File file = new File(fileName);

		try {
			Scanner scanner = new Scanner(file);

			String line = scanner.nextLine();
			scanner.close();

			String[] parts = line.split(";");
			for(int i=0; i < parts.length; i++)
				commonHeader.put(parts[i].toLowerCase(), i);


		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	
	public static void cleanFile(String fileToClean, String fileCleaned) {
		
		PrintWriter out;
		int dropped = 0;
		try {
			out = new PrintWriter(new BufferedWriter(new FileWriter(fileCleaned, true))); 
		
		// Location of file to read
		File file = new File(fileToClean);

			Scanner scanner = new Scanner(file);

			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				String[] parts = line.split(";");
				
				if(parts.length == 55)
					out.println(line);
				else dropped++;

			}
			scanner.close();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("Registos esquecidos: " + dropped);
	}
	
	public static void removeZeros(String fileToClean, String fileCleaned) {
		PrintWriter out;
		int dropped = 0;
		try {
			out = new PrintWriter(new BufferedWriter(new FileWriter(fileCleaned, true))); 
		
			int i = 0;
			
		// Location of file to read
		File file = new File(fileToClean);

			Scanner scanner = new Scanner(file);

			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				line = line.replaceAll("\\.0000", "");
				
				if (i== 100000)
					System.out.println("...");
				
				i++;
				out.println(line);
				
			}
			scanner.close();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("Done " + dropped);
	}
	
	public static void removeInvalidCoordinates(String fileToClean, String fileCleaned) {
		PrintWriter out;
		int dropped = 0;
		try {
			out = new PrintWriter(new BufferedWriter(new FileWriter(fileCleaned, true))); 
		
			int i = 0;
			
		// Location of file to read
		File file = new File(fileToClean);

			Scanner scanner = new Scanner(file);

			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				line = line.replaceAll("\\.0000", "");
				
				if (i== 100000)
					System.out.println("...");
				
				i++;
				out.println(line);
				
			}
			scanner.close();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("Done " + dropped);
	}
	
	public static void fromDBtoFile(String outFile) {
		
		PrintWriter out;
		String template = "%d;Point;POINT(%s %s);%s;%d;%d;%d;%d;%d";
		
		
		try {
			int id = 0;
			out = new PrintWriter(new BufferedWriter(new FileWriter(outFile, true))); 
			
			out.println("Autor:Ricardo Silva");
			out.println("Data:2015/01/10");
			out.println("coordinates: latLong");
			out.println();
			
			out.println("#DATA#");
			out.println("Resolution;0.00000001");
			out.println("ID;SpatialType;SpatialExp.WKT;Atrb;Count;CountPersons;CountFatals;CountDrunks;Density");
			
			Connection connection = DataStoreInfo.getMetaStore();
			Statement st = connection.createStatement();
	
			String sql = "select longitud, latitude, weather, persons, fatals, drunk_dr	from all_accidents";
			ResultSet resultSet = st.executeQuery(sql);

			while(resultSet.next()) {

				String longitude = Double.toString(resultSet.getDouble(1));
				String latitude = Double.toString(resultSet.getDouble(2));
				
				int weather = resultSet.getInt(3);
				int persons = resultSet.getInt(4);
				int fatals = resultSet.getInt(5);
				int drunkers = resultSet.getInt(6);
				
				String line = String.format(template, id, latitude, longitude, weather, 1, persons, fatals, drunkers, 1);
								
				out.println(line);
				id++;
			}
			out.close();
			
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		
	}
	
	
	
	public static void main(String[] args) {

//		readFile("D:/Programming/workspace/relevant_changes/data/AccidentsUSA/headers.txt");
		
//		String allAccidents = "D:/Programming/workspace/relevant_changes/data/AccidentsUSA/accidents_2001_2013.csv";
//		fillHeader(allAccidents);
//
//		System.out.println("Header added");
//		
//		String accidents_year = "D:/Programming/workspace/relevant_changes/data/AccidentsUSA/accident_%d.csv";
//		
//		for(int i=2001; i <=2013; i++) {
//			System.out.println("Year " + i + " starts");
//			String outFile = String.format(accidents_year, i);
//			addAccidentFile(outFile, allAccidents);
//			
//		}
		
//		String toClean = "D:/Programming/workspace/relevant_changes/data/AccidentsUSA/cleaned_accidents_2001_2013.csv";
//		String cleaned = "D:/Programming/workspace/relevant_changes/data/AccidentsUSA/__cleaned_accidents_2001_2013.csv";
		 
//		removeZeros(toClean, cleaned);
		

		String spatial_core = "D:/Programming/SpatialCore/accidents_2001_2013_in_prototype.csv";
		fromDBtoFile(spatial_core);
	}







}

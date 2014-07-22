/**
 * @author hiteshgarg
 * className CsvHandler
 * This class is used to perform various operations on the csv file like parsing,
 *  and finally adding data to data store. 
 */
package com.nagarro.training.assignment3.csvServices;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nagarro.training.assignment3.Constants.Constants;
import com.nagarro.training.assignment3.DAO.FlightData;
import com.nagarro.training.assignment3.customException.NewCustomException;
import com.nagarro.training.assignment3.flight.Flight;
import com.nagarro.training.assignment3.validators.StringDateConverter;

public class CsvHandler{

	private Map<String, FileTime> csvListWithTime;

//	public void run() {
//		// CsvFilesDTO csvDto = new CsvFilesDTO();
//		csvListWithTime = new HashMap<String, FileTime>();
//
//		while (true) {
//			try {
//				/*
//				 * Search the Directory after a duration of 1 minute for the
//				 * updated or newly added files
//				 */
//				searchCSVinDirectory();
//				Thread.sleep(60 * 1000);
//			} catch (InterruptedException e) {
//				System.out.println("Unexpected Error. Please try again");
//			} catch (NewCustomException exception) {
//				exception.printMessage();
//			}
//		}
//	}

	public void searchCSVinDirectory() throws NewCustomException {
		csvListWithTime = new HashMap<String, FileTime>();
		try {
			File file = new File(Constants.CSV_FILES_URL);
			List<String> updatedFiles = new ArrayList<String>();

			String[] filenames = file.list();

			/*
			 * Filter out all the csv files newly added csv files and setting
			 * their last modified time as null. Then creating a separate list
			 * for all the updates Csv files so that it can be used by other
			 * functions.
			 */

			for (int i = 0; i < filenames.length; i++) {
				if (filenames[i].endsWith(".csv")) {
					if (!csvListWithTime.containsKey(filenames[i])) {
						csvListWithTime.put(filenames[i], null);
					}

					Path path = Paths
							.get(Constants.CSV_FILES_URL, filenames[i]);
					BasicFileAttributes fileAttributes = Files.readAttributes(
							path, BasicFileAttributes.class);

					if (csvListWithTime.get(filenames[i]) == null
							|| !csvListWithTime.get(filenames[i]).equals(
									fileAttributes.lastModifiedTime())) {
						updatedFiles.add(filenames[i]);
						csvListWithTime.put(filenames[i],
								fileAttributes.lastModifiedTime());
					}
				}
			}
			/*
			 * If updated files are found then they are added to the Data
			 * Structure
			 */
			if (updatedFiles.size() > 0) {
				addUpdatedFilesData(updatedFiles);
			}
		} catch (IOException e) {
			throw new NewCustomException(
					"Problem in Input output operations of File..Reading File Attributes");
		} catch (Exception e) {
			throw new NewCustomException(
					"Unexpected Error while reading CSV file attributes");
		}
	}

	public void addUpdatedFilesData(List<String> updatedFiles) {

		for (int i = 0; i < updatedFiles.size(); i++) {
			try {
				Map<String, Set<Flight>> flightCSVData = readCsvAddData(updatedFiles
						.get(i));
				/*
				 * Sends every set related to a particular location along with
				 * the Departure-Arrival key to insert it into the database
				 */
				for (Map.Entry<String, Set<Flight>> flight : flightCSVData
						.entrySet()) {
					FlightData.insertCsvFileData(flight.getKey(), flight.getValue());
				}

			} catch (NewCustomException exception) {
				exception.printMessage();
			}
		}
	}

	/*
	 * This Method receives filename . Read it Line by Line. Create an object of
	 * Flight class and stores it in the map. For the sub map it creates a key
	 * by concatenating Departure Location and Arrival Location and checks if it
	 * exists If the Key exists already the object is added to the List
	 * otherwise a new entry is created in the Outer Map using Departure
	 * Location and Arrival Location as Key.
	 */
	public Map<String, Set<Flight>> readCsvAddData(String csvFile)
			throws NewCustomException {
		BufferedReader reader;
		Map<String, Set<Flight>> flightData = new HashMap<String, Set<Flight>>();
		csvFile = Constants.CSV_FILES_URL + "/" + csvFile;

		try {
			reader = new BufferedReader(new FileReader(csvFile));
			String inputLine = "";
			reader.readLine();

			while ((inputLine = reader.readLine()) != null) {
				String data[] = inputLine.split(Constants.CSV_SPLIT_DELIMITTER);
				Flight flight = new Flight();
				flight.setFlightNo(data[0]);
				
				Date date = StringDateConverter.StringToDateConvertor(data[3]);
				if (date == null) {
					continue;
				}
				flight.setValidTill(date);
				flight.setFlightTime(data[4]);
				flight.setFlightDuration(data[5]);
				flight.setFare(Integer.parseInt(data[6]));
				flight.setSeatAvail(data[7]);
				flight.setFlightClass(data[8]);

				String DepArrKey = data[1] + data[2];
				if (!(flightData.containsKey(DepArrKey))) {
					flightData.put(DepArrKey, new HashSet<Flight>());
				}
				flightData.get(DepArrKey).add(flight);
			}
		} catch (FileNotFoundException e) {
			throw new NewCustomException("Sorry the Files are not Found");
		} catch (IOException e) {
			throw new NewCustomException(
					"Unexpected Input Output Exceptions while Reading the File");
		}
		return flightData;
	}
}

package com.nagarro.training.assignment3.DAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

import com.nagarro.training.assignment3.Constants.Constants;
import com.nagarro.training.assignment3.customException.NewCustomException;
import com.nagarro.training.assignment3.flight.Flight;

/**
 * @author hiteshgarg This class is used as a Data Store to store all the data
 *         related to flights.
 */
public class FlightData {

	/*
	 * This method get the location string and a Set of flights related to that
	 * location and set them in the database
	 */
	public static void insertCsvFileData(String location, Set<Flight> flightData)
			throws NewCustomException {

		if (location != null && flightData.size() != 0) {

			Integer arrLoc = getLocation(location.substring(0, 3));
			Integer depLoc = getLocation(location.substring(3));
			if (arrLoc == null) {
				setLocation(location.substring(0, 3));
				arrLoc = getLocation(location.substring(0, 3));
			}
			if (depLoc == null) {
				setLocation(location.substring(3));
				depLoc = getLocation(location.substring(3));
			}

			System.out.println(arrLoc + " " + depLoc);

			Connection connection = null;
			PreparedStatement statement = null;
			for (Flight flight : flightData) {

				connection = DAO.getConnection();

				try {

					connection = DAO.getConnection();
					if (connection != null) {
						String sql = Constants.INSERT_FLIGHT;
						statement = connection.prepareStatement(sql);
						statement.setString(1, flight.getFlightNo());
						statement.setDate(2, new java.sql.Date(flight.getValidTill().getTime()));
						statement.setString(3, flight.getFlightTime());
						statement.setString(4, flight.getFlightDuration());
						statement.setInt(5, flight.getFare());
						statement.setString(6, flight.getSeatAvail());
						statement.setString(7, flight.getFlightClass());
						statement.setInt(8, depLoc);
						statement.setInt(9, arrLoc);
						statement.executeUpdate();
					}
				} catch (SQLException e) {
					// TODO: handle exception
					e.printStackTrace();
					throw new NewCustomException(
							"Error in Adding Flight Data to the Database");
				}
				catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
			if (statement != null){
				try {
					statement.close();
				} catch (SQLException ignore) {
				}
		}
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException ignore) {
				}
			}
		}
	}

	/*
	 * This method receives the Location(Departure + Arrival) String and sets
	 * the String to Location Table
	 */
	private static void setLocation(String location) throws NewCustomException {

		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		Integer LocId = null;

		try {
			connection = DAO.getConnection();
			if (connection != null) {
				String sql = Constants.ADD_LOCATION;
				statement = connection.prepareStatement(sql);
				statement.setString(1, location);
				statement.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new NewCustomException("Error in Finding Location");
		} finally {
			if (resultSet != null)
				try {
					resultSet.close();
				} catch (SQLException ignore) {
				}
			if (statement != null)
				try {
					statement.close();
				} catch (SQLException ignore) {
				}
			if (connection != null)
				try {
					connection.close();
				} catch (SQLException ignore) {
				}
		}
	}

	/*
	 * This method receives the location String and find the related object and
	 * return the object of Location Class
	 */
	private static Integer getLocation(String location)
			throws NewCustomException {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		Integer LocId = null;
		try {
			connection = DAO.getConnection();
			if (connection != null) {
				String sql = Constants.SEARCH_LOCATION;
				statement = connection.prepareStatement(sql);
				statement.setString(1, location);
				resultSet = statement.executeQuery();
				if (resultSet.next()) {
					System.out.println(resultSet.getInt(1));
					LocId = resultSet.getInt(1);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new NewCustomException("Error in Finding Location");
		} finally {
			if (resultSet != null)
				try {
					resultSet.close();
				} catch (SQLException ignore) {
				}
			if (statement != null)
				try {
					statement.close();
				} catch (SQLException ignore) {
				}
			if (connection != null)
				try {
					connection.close();
				} catch (SQLException ignore) {
				}
		}
		return LocId;
	}
}

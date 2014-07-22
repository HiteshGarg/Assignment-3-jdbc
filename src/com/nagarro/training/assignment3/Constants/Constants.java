/**
 * @author hiteshgarg
 * This class is used to delcare various constants that are used at various locations in the project.
 */

package com.nagarro.training.assignment3.Constants;

public class Constants {

	public static final String CSV_FILES_URL = "AirlinesCsvFiles";
	public static final String CSV_SPLIT_DELIMITTER = "\\|";
	public static final String PREPARED_STATEMENT = "\\|";
	public static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	public static final String DB_URL = "jdbc:mysql://localhost/flight_search_jdbc";
	public static final String USERNAME = "root";
	public static final String PASSWORD = "Passw0rd";
	public static final String INSERT_FLIGHT = "insert into flight_data(flight_no,valid_till,flight_time,flight_duration,fare,seat_avail,flight_class,dep_loc,arr_loc) values(?,?,?,?,?,?,?,?,?)";
	public static final String SEARCH_LOCATION= "select id from flight_location where location = ?";
	public static final String ADD_LOCATION = "INSERT into flight_location(location) values(?)";

}

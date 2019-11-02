package net.jfabricationgames.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import com.mysql.cj.jdbc.MysqlDataSource;

/**
 * Create a connection to a database and add or get values of one specific table for testing.
 * 
 * @author Tobias Faßbender
 */
public class DatabaseConnection {
	
	/**
	 * The connection url for the mysql database in the docker
	 * <ul>
	 * <li>"jdbc:mysql://" just tells the driver to use jdbc:mysql (needed)</li>
	 * <li>"mysql" is the name or alias of the docker container</li>
	 * <li>"useSSL=false" obviously tells to not use SSL (and don't warn because of no SSL every time), because SSL is not needed when communicating
	 * between docker containers</li>
	 * </ul>
	 */
	public static final String URL = "jdbc:mysql://mysql?useSSL=false";
	public static final String USER = "root";
	
	private static DatabaseConnection instance;
	
	private DatabaseConnection() {
		//create the database and the tables that are needed if they're not existing
		if (!createDatabaseIfNotExists()) {
			System.err.println("Error while creating the database");
		}
		if (!createTableIfNotExists()) {
			System.err.println("ERROR while creating the table");
		}
	}
	
	public static synchronized DatabaseConnection getInstance() {
		if (instance == null) {
			instance = new DatabaseConnection();
		}
		return instance;
	}
	
	private boolean createDatabaseIfNotExists() {
		String query = "CREATE DATABASE IF NOT EXISTS ws_db_test;";
		DataSource dataSource = getDataSourceWithoutDatabase();
		try (Connection con = dataSource.getConnection(); Statement statement = con.createStatement()) {
			statement.execute(query);
		}
		catch (SQLException sqle) {
			sqle.printStackTrace();
			return false;
		}
		return true;
	}
	
	private boolean createTableIfNotExists() {
		String query = "CREATE TABLE IF NOT EXISTS ws_db_test.entries (id INT NOT NULL PRIMARY KEY AUTO_INCREMENT, entry VARCHAR(100));";
		DataSource dataSource = getDataSource();
		try (Connection con = dataSource.getConnection(); Statement statement = con.createStatement()) {
			statement.execute(query);
		}
		catch (SQLException sqle) {
			sqle.printStackTrace();
			return false;
		}
		return true;
	}
	
	private MysqlDataSource getDataSource() {
		//https://www.journaldev.com/2509/java-datasource-jdbc-datasource-example
		MysqlDataSource dataSource = getDataSourceWithoutDatabase();
		dataSource.setDatabaseName("ws_db_test");
		return dataSource;
	}
	private MysqlDataSource getDataSourceWithoutDatabase() {
		//https://www.journaldev.com/2509/java-datasource-jdbc-datasource-example
		MysqlDataSource dataSource = new MysqlDataSource();
		dataSource.setURL(URL);
		dataSource.setUser(USER);
		dataSource.setPassword("4fgsdf32fewa321rfdserw32qf");//add the same password in the environment variables of the docker-compose.ylm
		dataSource.setPort(3306);//the default mysql port
		try {
			//enable public key retrieval because I want to get the keys of the inserted data 
			//(which seems to cause problems when using useSSL=false in the url)
			dataSource.setAllowPublicKeyRetrieval(true);
		}
		catch (SQLException sqle) {
			sqle.printStackTrace();
		}
		return dataSource;
	}
	
	/**
	 * Add an entry to the table.
	 * 
	 * @param entry
	 *        The entry that is to be added to the table
	 * 
	 * @return Returns the id of the entry that was added
	 */
	public int addEntry(String entry) {
		//https://javabeginners.de/Datenbanken/Prepared_Statement.php
		String query = "INSERT INTO ws_db_test.entries(`id`, `entry`) VALUES (\"0\", ?);";
		int id = -1;
		
		DataSource dataSource = getDataSource();
		try (Connection con = dataSource.getConnection();//
				PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
			ps.setString(1, entry);
			int affectedRows = ps.executeUpdate();
			
			if (affectedRows == 0) {
				throw new SQLException("Inserting data failed. No affected rows.");
			}
			
			try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					id = generatedKeys.getInt(1);
				}
				else {
					throw new SQLException("Inserting data failed. No ID obtained.");
				}
			}
		}
		catch (SQLException sqle) {
			sqle.printStackTrace();
		}
		return id;
	}
	
	/**
	 * Get an entry for an id in the table.
	 * 
	 * @param id
	 *        The id of the entry in the table
	 * 
	 * @return The entry at the given id in the table (or null if there is no such id in the table)
	 */
	public String getEntry(int id) {
		//https://javabeginners.de/Datenbanken/Prepared_Statement.php
		String query = "SELECT entry FROM ws_db_test.entries WHERE id = ?;";
		String entry = null;
		
		DataSource dataSource = getDataSource();
		try (Connection con = dataSource.getConnection();//
				PreparedStatement ps = con.prepareStatement(query)) {
			ps.setInt(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					entry = rs.getString(1);
				}
			}
		}
		catch (SQLException sqle) {
			sqle.printStackTrace();
		}
		return entry;
	}
}
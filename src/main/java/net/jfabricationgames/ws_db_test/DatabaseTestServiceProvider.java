package net.jfabricationgames.ws_db_test;

import java.sql.SQLException;
import java.util.List;

import net.jfabricationgames.db.DatabaseConnection;
import net.jfabricationgames.json_rpc.UnsupportedParameterException;

public class DatabaseTestServiceProvider {
	
	public Integer addEntry(Object parameters) throws SQLException, UnsupportedParameterException {
		if (parameters instanceof String) {
			String entry = (String) parameters;
			DatabaseConnection db = DatabaseConnection.getInstance();
			int id = db.addEntry(entry);
			return id;
		}
		else {
			throw new UnsupportedParameterException("A String is expected to be added to the database.");
		}
	}
	
	public String getEntry(Object parameters) throws SQLException, UnsupportedParameterException {
		if (parameters instanceof Integer) {
			Integer id = (Integer) parameters;
			DatabaseConnection db = DatabaseConnection.getInstance();
			String entry = db.getEntry(id);
			return entry;
		}
		else {
			throw new UnsupportedParameterException("An integer (the id) is expected to get an entry from the database.");
		}
	}
	
	public List<Integer> getAllIds(Object parameters) throws SQLException {
		//ignore the parameters
		DatabaseConnection db = DatabaseConnection.getInstance();
		List<Integer> ids = db.getIds();
		return ids;
	}
}
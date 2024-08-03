package app.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import app.model.Place;

public class PlacesDAO {
	
	private String jdbcURL = "jdbc:mysql://localhost:3306/demo?useSSL=false";
	private String jdbcUsername = "root";
	private String jdbcPassword = "root";
	
	private static final String INSERT_PLACES_SQL = "INSERT INTO places" + "  (name, price, image, rating) VALUES "
			+ " (?, ?, ?, ?);";

	private static final String SELECT_PLACES_BY_ID = "select id,name,price,image, rating from places where id =?";
	private static final String SELECT_ALL_PLACES = "select * from places";
	private static final String DELETE_PLACES_SQL = "delete from places where id = ?;";
	private static final String UPDATE_PLACES_SQL = "update places set name = ?,price= ?, image =?, rating=? where id = ?;";
	
	
	protected Connection getConnection() {
		Connection connection = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return connection;
	}
	
	
	
	public void insertPlaces(Place place) throws SQLException {
		System.out.println(INSERT_PLACES_SQL);
		// try-with-resource statement will auto close the connection.
		try (Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(INSERT_PLACES_SQL)) {
			preparedStatement.setString(1, place.getName());
			preparedStatement.setString(2, place.getImage());
			preparedStatement.setString(3, place.getPrice());
			preparedStatement.setString(4, place.getRating());
			System.out.println(preparedStatement);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
//			printSQLException(e);
		}
	}
	
	
	
	public Place selectPlace(int id) {
		Place place = null;
		// Step 1: Establishing a Connection
		try (Connection connection = getConnection();
				// Step 2:Create a statement using connection object
				PreparedStatement preparedStatement = connection.prepareStatement(SELECT_PLACES_BY_ID);) {
			preparedStatement.setInt(1, id);
			System.out.println(preparedStatement);
			// Step 3: Execute the query or update query
			ResultSet rs = preparedStatement.executeQuery();

			// Step 4: Process the ResultSet object.
			while (rs.next()) {
				String name = rs.getString("name");
				String price = rs.getString("price");
				String image = rs.getString("image");
				String rating = rs.getString("rating");
				place = new Place(id, name, price, image, rating);
			}
		} catch (SQLException e) {
//			printSQLException(e);
		}
		return place;
	}
	
	
	
	
	
	
	public List<Place> selectAllPlaces() {

		// using try-with-resources to avoid closing resources (boiler plate code)
		List<Place> places = new ArrayList<>();
		// Step 1: Establishing a Connection
		try (Connection connection = getConnection();

				// Step 2:Create a statement using connection object
			PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_PLACES);) {
			System.out.println(preparedStatement);
			// Step 3: Execute the query or update query
			ResultSet rs = preparedStatement.executeQuery();

			// Step 4: Process the ResultSet object.
			while (rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("name");
				String price = rs.getString("price");
				String image = rs.getString("image");
				String rating = rs.getString("rating");
				places.add(new Place(id, name, price, image, rating));
			}
		} catch (SQLException e) {
//			printSQLException(e);
		}
		return places;
	}
	
	
	
	
	
	public boolean deletePlace(int id) throws SQLException {
		boolean rowDeleted;
		try (Connection connection = getConnection();
				PreparedStatement statement = connection.prepareStatement(DELETE_PLACES_SQL);) {
			statement.setInt(1, id);
			rowDeleted = statement.executeUpdate() > 0;
		}
		return rowDeleted;
	}
	
	
	
	public boolean updatePlace(Place place) throws SQLException {
		boolean rowUpdated;
		try (Connection connection = getConnection();
				PreparedStatement statement = connection.prepareStatement(UPDATE_PLACES_SQL);) {
			statement.setString(1, place.getName());
			statement.setString(2, place.getPrice());
			statement.setString(3, place.getImage());
			statement.setString(4, place.getRating());
			statement.setInt(5, place.getId());

			rowUpdated = statement.executeUpdate() > 0;
		}
		return rowUpdated;
	}

}

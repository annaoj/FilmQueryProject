package com.skilldistillery.filmquery.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.skilldistillery.filmquery.entities.Actor;
import com.skilldistillery.filmquery.entities.Category;
import com.skilldistillery.filmquery.entities.Film;
import com.skilldistillery.filmquery.entities.Inventory;

public class DatabaseAccessorObject implements DatabaseAccessor {
	private static final String URL = "jdbc:mysql://localhost:3306/sdvid?useSSL=false";
	private String user = "student";
	private String pass = "student";
	static {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("Exception" + e.getMessage());
		}
	}

	@Override
	public Film findFilmById(int filmId) throws SQLException {

		String sql = "SELECT id, title, description," + " release_year, language_id, rental_duration,"
				+ " rental_rate,length, replacement_cost, rating, special_features FROM film WHERE id = ?";
		Film filmObj = null;
		try {
			Connection conn = DriverManager.getConnection(URL, user, pass);
			PreparedStatement ps = conn.prepareStatement(sql);

			// set parameter
			ps.setInt(1, filmId);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				filmObj = new Film(rs.getInt("id"), rs.getString("title"), rs.getString("description"),
						rs.getString("release_year"), rs.getInt("language_id"), rs.getInt("rental_duration"),
						rs.getDouble("rental_rate"), rs.getInt("length"), rs.getDouble("replacement_cost"),
						rs.getString("rating"), rs.getString("special_features"), findActorsByFilmId(filmId));
			}

			rs.close();
			ps.close();
			conn.close();
		} catch (SQLException e) {
			System.err.println(e);
		}

		return filmObj;
	}

	@Override
	public Actor findActorById(int actorId) throws SQLException {
		Actor actorObj = null;

		String sql = "SELECT  id, first_name, last_name FROM actor WHERE id = ?";

		try {
			Connection conn = DriverManager.getConnection(URL, user, pass);
			PreparedStatement ps = conn.prepareStatement(sql);

			// set parameter
			ps.setInt(1, actorId);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				actorObj = new Actor(rs.getInt("id"), rs.getString("first_name"), rs.getString("last_name"));
			}

			rs.close();
			ps.close();
			conn.close();
		} catch (SQLException e) {
			System.err.println(e);
		}

		return actorObj;
	}

//	Select a.first_name, a.last_name,film.title from actor a Join film_actor ON film_actor.actor_id=a.id JOin film ON film_actor.film_id=film.id where film.id=2;
	
	@Override
	public List<Actor> findActorsByFilmId(int filmId) {
		List<Actor> la = new ArrayList<Actor>();
		String sql = "Select a.id, a.first_name, a.last_name,film.title from actor a Join film_actor ON film_actor.actor_id=a.id JOin film ON film_actor.film_id=film.id where film.id=?";

		try {
			Connection conn = DriverManager.getConnection(URL, user, pass);
			PreparedStatement ps = conn.prepareStatement(sql);

			// set parameter
			ps.setInt(1, filmId);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				la.add(new Actor(rs.getInt("a.id"), rs.getString("a.first_name"), rs.getString("a.last_name")));
			}

			rs.close();
			ps.close();
			conn.close();
		} catch (SQLException e) {
			System.err.println(e);
		}

		return la;

	}
	
	
	@Override
	public List<Category> findCategorybyFilmId(int filmId) {
		List<Category> categories = new ArrayList<Category>();
		String sql = "Select category.name from category Join film_category ON film_category.category_id=category.id JOIN film ON film_category.film_id=film.id where film.id=?";

		try {
			Connection conn = DriverManager.getConnection(URL, user, pass);
			PreparedStatement ps = conn.prepareStatement(sql);

			// set parameter
			ps.setInt(1, filmId);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				categories.add(new Category( rs.getString("category.name")));
			}

			rs.close();
			ps.close();
			conn.close();
		} catch (SQLException e) {
			System.err.println(e);
		}

		return categories;

	}
	
	//Select title, description, l.name  from film JOIN language l ON film.language_id=l.id WHERE title LIKE "%test%" OR description LIKE "%test%";
	@Override
	public List<Film> findfilmByKeyword(String keyword) throws SQLException {
		List<Film> filmList = new ArrayList<Film>();

		String sql = "SELECT film.id, title, description," + " release_year, language_id, rental_duration,"
				+ " rental_rate,length, replacement_cost, rating, special_features, language.name  from film JOIN language ON language.id=film.language_id WHERE title LIKE ? OR description LIKE ? ORDER BY title ASC";

		try {
			Connection conn = DriverManager.getConnection(URL, user, pass);
			PreparedStatement ps = conn.prepareStatement(sql);

			// set parameter
			ps.setString(1, "%" + keyword + "%");
			ps.setString(2, "%" + keyword + "%");

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				filmList.add(new Film(rs.getInt("id"), rs.getString("title"), rs.getString("description"),
						rs.getString("release_year"), rs.getInt("language_id"), rs.getInt("rental_duration"),
						rs.getDouble("rental_rate"), rs.getInt("length"), rs.getDouble("replacement_cost"),
						rs.getString("rating"), rs.getString("special_features"), findActorsByFilmId(rs.getInt("id")),rs.getString("language.name"), findCategorybyFilmId(rs.getInt("id"))));
			}

			rs.close();
			ps.close();
			conn.close();
		} catch (SQLException e) {
			System.out.println("Error in findfilmByKeyword " + e.getMessage());
			System.err.println(e);
		}
		return filmList;
	}

	@Override
	public List<Inventory> getInventoryAndCondition(int filmId) {
		List<Inventory> inventoryList = new ArrayList<Inventory>();

		String sql = "SELECT film.title, media_condition, store_id FROM film JOIN inventory_item on film.id = film_id WHERE film.id = ? ORDER BY store_id ASC;";

		try {
			Connection conn = DriverManager.getConnection(URL, user, pass);
			PreparedStatement ps = conn.prepareStatement(sql);

			// set parameter
			ps.setInt(1,filmId);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				inventoryList.add(new Inventory(rs.getString("media_condition"), rs.getInt("store_id")));
			}

			rs.close();
			ps.close();
			conn.close();
		} catch (SQLException e) {
			System.out.println("Error in findfilmByKeyword " + e.getMessage());
			System.err.println(e);
		}
		return inventoryList;
	}

}

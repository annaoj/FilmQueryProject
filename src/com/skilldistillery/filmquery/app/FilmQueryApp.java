package com.skilldistillery.filmquery.app;

import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import com.skilldistillery.filmquery.database.DatabaseAccessor;
import com.skilldistillery.filmquery.database.DatabaseAccessorObject;
import com.skilldistillery.filmquery.entities.Actor;
import com.skilldistillery.filmquery.entities.Film;

public class FilmQueryApp {

	DatabaseAccessor db = new DatabaseAccessorObject();

	public static void main(String[] args) throws SQLException {
		FilmQueryApp app = new FilmQueryApp();
		// app.test();
		try {
			app.launch();
		} catch (Exception e) {
			System.out.println("Error " + e.getClass());
		}
	}

//	private void test() throws SQLException {
//		Film film = db.findFilmById(1);
//		System.out.println(film);
//	}

	private void launch() throws SQLException {
		Scanner input = new Scanner(System.in);

		startUserInterface(input);

		input.close();
	}

	private void startUserInterface(Scanner input) throws SQLException {
		int response = 0;

		try {
			do {
				System.out.println("What would you like to do?");
				System.out.println("1. Look up a film by its id.\n" + "2. Look up a film by a search keyword.\n"
						+ "3. Exit the application.");

				response = input.nextInt();

				switch (response) {
				case 1:
					lookupFilmById(input);
					break;
				case 2:
					lookupFilmByKeyword(input);
					break;
				case 3:
					System.out.println("Good Bye!");
					System.exit(0);
					break;
				default:
					System.out.println("Wrong Input");;

				}
			} while (response != 3);
		} catch (InputMismatchException e) {
			System.out.println("Error, You entered wrong input data. Result is " + e.getMessage());

		}

		
	}

	private void lookupFilmByKeyword(Scanner input) {
		System.out.print("Please enter keyword >>");
		String keyword = input.next();
		List<Film> filmList = null;
		try {
			filmList = db.findfilmByKeyword(keyword);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			if (filmList.isEmpty()) {
				System.out.println("There is no match found :( ");
			} else {
				System.out.println("Result  \n" + "\tTitle " + "\t\t Release Year " + "\t Rating" + "\t Language" + "\t\t Description: " + "\t\t Cast: ");
				
				for (Film film : filmList) {
					System.out.println("    " + film.getTitle() + " | " + film.getRelease_year() + " | \t" + film.getRating()
					+ " | \t" + film.getLanguage()+ " | \t" + film.getDescription()+" | \t" +displayActors(film.getLaCast()));
				}

			}
			System.out.println("");
		}

	}
	private StringBuilder displayActors(List<Actor> cast){
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < cast.size(); i++) {
			sb.append(cast.get(i).getFirst_name() + " " + cast.get(i).getLast_name());
			if(i != cast.size()-1) {
				sb.append( ", ");
			}
			
		}

		return sb;
	}
	private void lookupFilmById(Scanner input) throws SQLException {
		System.out.print("Please enter film id >>");
		int filmId = input.nextInt();
		Film film = null;
		try {
			film = db.findFilmById(filmId);
			System.out.println("Result  \n" + "\tTitle " + "\t\t Release Year " + "\t Rating" + "\t\t Description: ");
			System.out.println("    " + film.getTitle() + " | " + film.getRelease_year() + " | \t" + film.getRating()
					+ " | \t" + film.getDescription());

		} catch (Exception e) {
		} finally {
			if (film == null) {
				System.out.println("There is no film found with id " + filmId);
			}
		}
		System.out.println();

	}


}

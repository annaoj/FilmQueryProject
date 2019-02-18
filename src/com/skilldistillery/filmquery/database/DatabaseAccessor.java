package com.skilldistillery.filmquery.database;

import java.sql.SQLException;
import java.util.List;

import com.skilldistillery.filmquery.entities.Actor;
import com.skilldistillery.filmquery.entities.Category;
import com.skilldistillery.filmquery.entities.Film;
import com.skilldistillery.filmquery.entities.Inventory;

public interface DatabaseAccessor {
  public Film findFilmById(int filmId) throws SQLException;
  public Actor findActorById(int actorId) throws SQLException;
  public List<Actor> findActorsByFilmId(int filmId);
  public List<Film> findfilmByKeyword(String keyword) throws SQLException;
  public List<Category> findCategorybyFilmId(int filmId);
  public List<Inventory> getInventoryAndCondition(int filmId);
}

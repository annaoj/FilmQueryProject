package com.skilldistillery.filmquery.database;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.skilldistillery.filmquery.entities.Actor;
import com.skilldistillery.filmquery.entities.Film;

class DatabaseAccessTests {
  private DatabaseAccessor db;

  @BeforeEach
  void setUp() throws Exception {
    db = new DatabaseAccessorObject();
  }

  @AfterEach
  void tearDown() throws Exception {
    db = null;
  }

  @Test
  void test_getFilmById_with_invalid_id_returns_null() throws SQLException {
    Film f = db.findFilmById(-42);
    assertNull(f);
  }
  
  @Test 
  void test_getFilmById_with_valid_id() throws SQLException {
	  Film f = db.findFilmById(38);
	  assertEquals("ARK RIDGEMONT", f.getTitle());
  }
  
//  @Test 
//  void test_getFilmById_with_invalid_id() throws SQLException {
//	  Film f = db.findFilmById(0);
//	  assertThrows(SQLException.class,  ()->{
//		 
//         });
//  }
  
  @Test
  void get_actor_returns_valid_id() throws SQLException {
	  Actor a = db.findActorById(8);
	  String actorName = a.getFirst_name() + " " + a.getLast_name();
	  assertEquals("Matthew Johansson", actorName);
  }

}

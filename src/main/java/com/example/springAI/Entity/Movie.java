package com.example.springAI.Entity;

public class Movie {
	
	private String movieName;
	private String leadCast;
	private String director;
	private int year;
	
	public String getMovieName() {
		return movieName;
	}
	public void setMovieName(String movieName) {
		this.movieName = movieName;
	}
	public String getLeadCast() {
		return leadCast;
	}
	public void setLeadCast(String leadCast) {
		this.leadCast = leadCast;
	}
	public String getDirector() {
		return director;
	}
	public void setDirector(String director) {
		this.director = director;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
}

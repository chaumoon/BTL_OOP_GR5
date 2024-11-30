package com.coachweb.model;

import java.util.ArrayList;

public class BookingDTO {
	private String departure;
	private String destination;
	private String departDate;
	private String coachID;
	private String timeGo;
	
	public BookingDTO(String departure, String destination, String departDate, String coachID, String timeGo) {
		this.departure = departure;
		this.destination = destination;
		this.departDate = departDate;
		this.coachID = coachID;
		this.timeGo = timeGo;
	}

	public String getDeparture() {
		return departure;
	}

	public void setDeparture(String departure) {
		this.departure = departure;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getDepartDate() {
		return departDate;
	}

	public void setDepartDate(String departDate) {
		this.departDate = departDate;
	}

	public String getCoachID() {
		return coachID;
	}

	public void setCoachID(String coachID) {
		this.coachID = coachID;
	}

	public String getTimeGo() {
		return timeGo;
	}

	public void setTimeGo(String timeGo) {
		this.timeGo = timeGo;
	}
	
	
}

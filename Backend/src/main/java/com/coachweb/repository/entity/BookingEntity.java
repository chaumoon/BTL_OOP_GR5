package com.coachweb.repository.entity;

public class BookingEntity {
	private String timeGo;
	private String timeCome;
	private String coachID;
	private Integer seatsLeft;
	
	public String getTimeGo() {
		return timeGo;
	}
	public void setTimeGo(String timeGo) {
		this.timeGo = timeGo;
	}
	public String getTimeCome() {
		return timeCome;
	}
	public void setTimeCome(String timeCome) {
		this.timeCome = timeCome;
	}
	public String getCoachID() {
		return coachID;
	}
	public void setCoachID(String coachID) {
		this.coachID = coachID;
	}
	public Integer getSeatsLeft() {
		return seatsLeft;
	}
	public void setSeatsLeft(Integer seatsLeft) {
		this.seatsLeft = seatsLeft;
	}
	
}

package com.coachweb.repository.entity;

public class SeatsSoldEntity {
	private String coachID;
	private Integer seatID;
	private String timeGo;
	public String getCoachID() {
		return coachID;
	}
	public void setCoachID(String coachID) {
		this.coachID = coachID;
	}
	public Integer getSeatID() {
		return seatID;
	}
	public void setSeatID(Integer seatID) {
		this.seatID = seatID;
	}
	public String getTimeGo() {
		return timeGo;
	}
	public void setTimeGo(String timeGo) {
		this.timeGo = timeGo;
	}
	
}

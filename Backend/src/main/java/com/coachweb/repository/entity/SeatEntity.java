package com.coachweb.repository.entity;

public class SeatEntity {

	private Integer seatID;
	private Double seatPrice;
	private Integer seatStatus;
	
	public SeatEntity(Integer seatID, Double seatPrice, Integer seatStatus) {
		this.seatID = seatID;
		this.seatPrice = seatPrice;
		this.seatStatus = seatStatus;
	}
	
	public Integer getSeatID() {
		return seatID;
	}
	public Integer getSeatStatus() {
		return seatStatus;
	}
	public Double getSeatPrice() {
		return seatPrice;
	}
	public void setSeatStatus(Integer seatStatus) {
		this.seatStatus = seatStatus;
	}
	
}

package com.coachweb.repository.entity;

import java.util.ArrayList;

public class TicketEntity {
	private String ticketID;
	private String customerID;
	private String coachID;
	private String routeID;
	private String name;
	private String phone;
	private String email;
	private String departure;
	private String destination;
	private String departDate;
	private String timeGo;
	private Double totalAmount;
	private ArrayList<Integer> seatsID;
	
	public String getTicketID() {
		return ticketID;
	}
	public void setTicketID(String ticketID) {
		this.ticketID = ticketID;
	}
	public String getCustomerID() {
		return customerID;
	}
	public void setCustomerID(String customerID) {
		this.customerID = customerID;
	}
	public String getCoachID() {
		return coachID;
	}
	public void setCoachID(String coachID) {
		this.coachID = coachID;
	}
	public String getRouteID() {
		return routeID;
	}
	public void setRouteID(String routeID) {
		this.routeID = routeID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
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
	public String getTimeGo() {
		return timeGo;
	}
	public void setTimeGo(String timeGo) {
		this.timeGo = timeGo;
	}
	public Double getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}
	public ArrayList<Integer> getSeatsID() {
		return seatsID;
	}
	public void setSeatsID(ArrayList<Integer> seatsID) {
		this.seatsID = seatsID;
	}
}

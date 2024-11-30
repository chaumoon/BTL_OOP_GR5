package com.coachweb.model;

public class TicketDTO {
	private String ticketID;
	private String customerID;
	private String paymentChannel;
	public TicketDTO(String ticketID, String customerID, String paymentChannel) {
		this.ticketID = ticketID;
		this.customerID = customerID;
		this.paymentChannel = paymentChannel;
	}
	public String getTicketID() {
		return ticketID;
	}
	public String getCustomerID() {
		return customerID;
	}
	public String getPaymentChannel() {
		return paymentChannel;
	}
}

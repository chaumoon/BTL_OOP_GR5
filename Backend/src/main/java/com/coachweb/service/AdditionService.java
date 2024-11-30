package com.coachweb.service;

import com.coachweb.model.PaymentDTO;
import com.coachweb.model.TicketDTO;

public interface AdditionService {
	void addTempCustomer(PaymentDTO paymentDTO);
	void addTempSeats(PaymentDTO paymentDTO);
	void addNewCustomer(TicketDTO ticketDTO);
	void addNewTicket(TicketDTO ticketDTO);
}

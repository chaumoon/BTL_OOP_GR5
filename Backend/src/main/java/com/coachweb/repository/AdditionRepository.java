package com.coachweb.repository;

import com.coachweb.model.PaymentDTO;
import com.coachweb.model.TicketDTO;

public interface AdditionRepository {
	void addTempCustomer(PaymentDTO paymentDTO);
	void addTempSeats(PaymentDTO paymentDTO);
	void addNewTicket(TicketDTO ticket);
	void addNewCustomer(TicketDTO ticket);
}

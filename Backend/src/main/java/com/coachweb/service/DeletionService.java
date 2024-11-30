package com.coachweb.service;

import com.coachweb.model.TicketDTO;

public interface DeletionService {
	void removeCustomer(TicketDTO ticketDTO);
	void removeSeats(TicketDTO ticketDTO);
}

package com.coachweb.repository;

import com.coachweb.model.TicketDTO;

public interface DeletionRepository {
	void removeCustomer(TicketDTO ticketDTO, String table);
}

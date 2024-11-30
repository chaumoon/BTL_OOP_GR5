package com.coachweb.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coachweb.model.PaymentDTO;
import com.coachweb.model.TicketDTO;
import com.coachweb.repository.AdditionRepository;
import com.coachweb.service.AdditionService;

@Service
public class AdditionServiceImpl implements AdditionService{
	@Autowired
	private AdditionRepository additionRepository; 
	
	@Override
	public void addTempCustomer(PaymentDTO paymentDTO) {
		additionRepository.addTempCustomer(paymentDTO);
	}
	

	@Override
	public void addTempSeats(PaymentDTO paymentDTO) {
		additionRepository.addTempSeats(paymentDTO);
		
	}
	
	@Override
	public void addNewCustomer(TicketDTO ticketDTO) {
		additionRepository.addNewCustomer(ticketDTO);
		
	}

	@Override
	public void addNewTicket(TicketDTO ticketDTO) {
		additionRepository.addNewTicket(ticketDTO);
	}


}

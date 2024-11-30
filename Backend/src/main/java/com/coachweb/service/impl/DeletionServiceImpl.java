package com.coachweb.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coachweb.model.TicketDTO;
import com.coachweb.repository.DeletionRepository;
import com.coachweb.service.DeletionService;
@Service
public class DeletionServiceImpl implements DeletionService{
	@Autowired
	private DeletionRepository deletionRepository;
	@Override
	public void removeCustomer(TicketDTO ticketDTO) {
		deletionRepository.removeCustomer(ticketDTO, "Tam_ThongTinDatVe");
		
	}
	@Override
	public void removeSeats(TicketDTO ticketDTO) {
		deletionRepository.removeCustomer(ticketDTO, "Tam_KhachHangChoNgoi");
		
	}
}

package com.coachweb.repository;

import java.util.ArrayList;
import java.util.List;

import com.coachweb.model.BookingDTO;
import com.coachweb.model.PaymentDTO;
import com.coachweb.model.TicketDTO;
import com.coachweb.repository.entity.SeatsSoldEntity;

public interface SearchRepository {
	String findCustomer(PaymentDTO paymentDTO, String table);
	List<SeatsSoldEntity> findSeatsSold(BookingDTO bookingDTO, ArrayList<Integer> seatsID, String timeGo);
}

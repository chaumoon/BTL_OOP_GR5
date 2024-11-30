package com.coachweb.service;

import java.util.List;
import com.coachweb.model.BookingDTO;
import com.coachweb.repository.entity.BookingEntity;
import com.coachweb.repository.entity.SeatEntity;

public interface BookingService {
	List<BookingEntity> findAll(BookingDTO bookingDTO);
	List<SeatEntity> findSeats(BookingDTO bookingDTO);
}

package com.coachweb.repository;

import java.util.List;


import com.coachweb.model.BookingDTO;
import com.coachweb.repository.entity.BookingEntity;
import com.coachweb.repository.entity.SeatEntity;
import com.coachweb.repository.entity.SeatsSoldEntity;

public interface BookingRepository{
	List<SeatsSoldEntity> findSeatsSold(BookingDTO bookingDTO);
	List<SeatEntity> findSeats();
	List<BookingEntity> findAll(BookingDTO bookingDTO);
}

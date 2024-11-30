package com.coachweb.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coachweb.model.BookingDTO;
import com.coachweb.repository.BookingRepository;
import com.coachweb.repository.entity.BookingEntity;
import com.coachweb.repository.entity.SeatEntity;
import com.coachweb.repository.entity.SeatsSoldEntity;
import com.coachweb.service.BookingService;

@Service
public class BookingServiceImpl implements BookingService{

	@Autowired
	private BookingRepository bookingRepository;
	
	@Override
	public List<BookingEntity> findAll(BookingDTO bookingDTO) {
		List<BookingEntity> bookingEntities = bookingRepository.findAll(bookingDTO);
		List<SeatsSoldEntity> seatsSoldEntities = bookingRepository.findSeatsSold(bookingDTO);
		
		if(!seatsSoldEntities.isEmpty()) {
		for(BookingEntity be: bookingEntities) {
			int count = 0;
			for(SeatsSoldEntity sl: seatsSoldEntities) {
				if(be.getCoachID().equals(sl.getCoachID()) && be.getTimeGo().equals(sl.getTimeGo())) {
					count++;
				}
			}
			be.setSeatsLeft(28 - count);
		}
		}
		return bookingEntities;
	}

	@Override
	public List<SeatEntity> findSeats(BookingDTO bookingDTO) {
		List<SeatEntity> seatEntities = bookingRepository.findSeats();
		List<SeatsSoldEntity> seatsSoldEntities = bookingRepository.findSeatsSold(bookingDTO);
		
		if(seatEntities == null || seatEntities.isEmpty()){
			System.out.println("No seats available in the system.");
			return Collections.emptyList();
		}
		if(seatsSoldEntities == null || seatsSoldEntities.isEmpty()) {
			System.out.println("No seats sold for this trip.");
			return seatEntities;
		}
		
		//Map to look up sold seats faster
		Map<Integer, SeatsSoldEntity> soldSeatsMap = seatsSoldEntities.stream()
				.filter(sl -> sl.getCoachID().equals(bookingDTO.getCoachID()) 
						&& sl.getTimeGo().equals(bookingDTO.getTimeGo()))
				.collect(Collectors.toMap(SeatsSoldEntity::getSeatID, sl -> sl));
		
		for(SeatEntity se: seatEntities) {
			if(soldSeatsMap.containsKey(se.getSeatID())) {
				se.setSeatStatus(1);
			}
		}
		
		return seatEntities;
	}

}

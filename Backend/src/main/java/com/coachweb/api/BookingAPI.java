package com.coachweb.api;

import org.springframework.web.bind.annotation.RestController;

import com.coachweb.model.BookingDTO;
import com.coachweb.repository.entity.BookingEntity;
import com.coachweb.repository.entity.SeatEntity;
import com.coachweb.service.BookingService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
public class BookingAPI {
	
	@Autowired
	private BookingService bookingService;
	@GetMapping(value="/v1/booking")
		public List<BookingEntity> getBooking(@RequestParam(name="from") String departure,
											@RequestParam(name="to") String destination,
											@RequestParam(name="departDate") String departDate){
		BookingDTO bookingDTO = new BookingDTO(departure, destination, departDate, "", "");
		List<BookingEntity> result = bookingService.findAll(bookingDTO);
		return result;
	}
	
	@PostMapping(value="/v1/booking")
		public List<SeatEntity> getSeats(@RequestParam(name="from") String departure,
				@RequestParam(name="to") String destination,
				@RequestParam(name="departDate") String departDate,
				@RequestBody BookingDTO bookingDTO){
		bookingDTO.setDeparture(departure);
		bookingDTO.setDestination(destination);
		bookingDTO.setDepartDate(departDate);
		List<SeatEntity> result = bookingService.findSeats(bookingDTO);
			return result;
		
	}
}

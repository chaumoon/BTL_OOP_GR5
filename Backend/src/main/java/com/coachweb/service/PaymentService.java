package com.coachweb.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;

import com.coachweb.model.PaymentDTO;

public interface PaymentService {
	ResponseEntity<String> getOrderUrl(PaymentDTO paymentDTO);
	String checkSeatsStatus(PaymentDTO paymentDTO);
}

package com.coachweb.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.coachweb.model.PaymentDTO;
import com.coachweb.service.AdditionService;
import com.coachweb.service.PaymentService;

@RestController
public class PaymentAPI{
	
	@Autowired
	private PaymentService paymentService;
	@Autowired
	private AdditionService additionService;
	
	@PostMapping(value="/v1/payment/zalo-pay")
		public ResponseEntity<String> getPayment(@RequestBody PaymentDTO paymentDTO) {
		
		additionService.addTempCustomer(paymentDTO);
		additionService.addTempSeats(paymentDTO); // paymentDTO has been changed
		
		if(paymentService.checkSeatsStatus(paymentDTO).equals("reserved")) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Seats reserved");
		}
		return paymentService.getOrderUrl(paymentDTO);
	}
}

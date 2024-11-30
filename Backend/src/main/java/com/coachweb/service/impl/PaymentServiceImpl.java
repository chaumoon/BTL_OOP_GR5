package com.coachweb.service.impl;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.coachweb.config.payment.ZaloConfig;
import com.coachweb.model.BookingDTO;
import com.coachweb.model.PaymentDTO;
import com.coachweb.repository.SearchRepository;
import com.coachweb.repository.entity.SeatsSoldEntity;
import com.coachweb.service.PaymentService;
import com.coachweb.util.HMACUtil;

@Service
public class PaymentServiceImpl implements PaymentService{
	
	@Autowired
	private SearchRepository searchRepository;
	@Autowired
	private ZaloConfig zaloConfig;
	
	@Override
    public ResponseEntity<String> getOrderUrl(PaymentDTO paymentDTO) {
		try {
            final Map<String, Object> embeddata = new HashMap<String, Object>() {{
                put("merchantinfo", "embeddata123");
            }};

            final Map[] item = {
                    new HashMap() {{
//                        put("itemid", "knb");
//                        put("itemname", "kim nguyen bao");
//                        put("itemprice", 198400);
//                        put("itemquantity", 1);
                    }}
            };
            
            String transid = zaloConfig.getCurrentTimeString("yyMMdd") + "_" + UUID.randomUUID();
            Map<String, Object> order = new HashMap<String, Object>() {{
            	put("title", "Thông tin vé xe");
                put("appid", zaloConfig.getAppid());
                put("apptransid", transid); // mã giao dịch có định dạng yyMMdd_xxxx, maVe
                put("apptime", System.currentTimeMillis());
                put("appuser", paymentDTO.getCustomerID()); //maKH
                put("phone", paymentDTO.getPhone());
                put("email", paymentDTO.getEmail());
                put("amount", 1L * paymentDTO.getSeatsID().size() * 150000);
                put("description", paymentDTO.getName() + " - Thanh toán vé xe khách #" + transid);
                put("bankcode", "");
                put("item", new JSONObject(item).toString());
                put("embeddata", new JSONObject(embeddata).toString());
            }};

            String data = order.get("appid") + "|" + order.get("apptransid") + "|" + order.get("appuser") + "|"
                    + order.get("amount") + "|" + order.get("apptime") + "|" + order.get("embeddata") + "|"
                    + order.get("item");

            order.put("mac", HMACUtil.HMacHexStringEncode(HMACUtil.HMACSHA256, zaloConfig.getKey1(), data));

            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            
            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            for (Map.Entry<String, Object> entry : order.entrySet()) {
                map.add(entry.getKey(), entry.getValue().toString());
            }

            HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);

            // Send POST request
            ResponseEntity<String> response = restTemplate.postForEntity(zaloConfig.getEndpoint(), entity, String.class);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while processing the transaction.");
        }
    }
    
	
	@Override
    public String checkSeatsStatus(PaymentDTO paymentDTO) {
    	BookingDTO bookingDTO = new BookingDTO(paymentDTO.getDeparture(), paymentDTO.getDestination(),
    							paymentDTO.getDepartDate(), paymentDTO.getCoachID(), paymentDTO.getTimeGo());
    	List<SeatsSoldEntity> res = searchRepository.findSeatsSold(bookingDTO, paymentDTO.getSeatsID(), paymentDTO.getTimeGo());
    	if(!res.isEmpty()) {
    		return "reserved";
    	}
    	else {
    		return "available";
    	}
    }
	
	
}

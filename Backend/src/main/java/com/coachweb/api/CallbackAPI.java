package com.coachweb.api;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.coachweb.model.TicketDTO;
import com.coachweb.repository.AdditionRepository;
import com.coachweb.service.AdditionService;
import com.coachweb.service.DeletionService;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import java.util.HashMap;
import java.util.logging.Logger;

@RestController
public class CallbackAPI {

    private Logger logger = Logger.getLogger(this.getClass().getName());
    private String key2 = "eG4r0GcoNtRGbO8";
    private Mac HmacSHA256;
    
    

    public CallbackAPI() throws Exception  {
        HmacSHA256 = Mac.getInstance("HmacSHA256");
        HmacSHA256.init(new SecretKeySpec(key2.getBytes(), "HmacSHA256"));
    }

    @Autowired
    private AdditionService additionService;
    @Autowired
    private DeletionService deletionService;
    @PostMapping("/callback")
    public String callback(@RequestBody String jsonStr) {
        JSONObject result = new JSONObject();
        
        try {
        	HashMap<Integer, String> paymentChannel = new HashMap<Integer, String>() {{
        		put(36, "Visa/Master/JCB");
        		put(37, "Bank Account");
        		put(38, "ZaloPay Wallet");
        		put(39, "ATM");
        		put(41, "Visa/Master Debit");
        	}};
        	
          JSONObject cbdata = new JSONObject(jsonStr);
          String dataStr = cbdata.getString("data");
          String reqMac = cbdata.getString("mac");

          byte[] hashBytes = HmacSHA256.doFinal(dataStr.getBytes());
          String mac = DatatypeConverter.printHexBinary(hashBytes).toLowerCase();

          if (!reqMac.equals(mac)) {
              result.put("returncode", -1);
              result.put("returnmessage", "mac not equal");
          } else {

              JSONObject data = new JSONObject(dataStr);
              TicketDTO ticket = new TicketDTO(data.getString("apptransid"), 
            		  								data.getString("appuser"), 
            		  								paymentChannel.get(data.getInt("channel")));
              
              additionService.addNewCustomer(ticket); // add new customer to database
              additionService.addNewTicket(ticket); // add new ticket to database
              deletionService.removeCustomer(ticket); // remove temporary customer
              deletionService.removeSeats(ticket); // remove temporary seats
              
              logger.info("update order's status = success where apptransid = " + data.getString("apptransid"));

              result.put("returncode", 1);
              result.put("returnmessage", "success");
          }
        } catch (Exception ex) {
          result.put("returncode", 0); 
          result.put("returnmessage", ex.getMessage());
        }

        return result.toString();
    }
}

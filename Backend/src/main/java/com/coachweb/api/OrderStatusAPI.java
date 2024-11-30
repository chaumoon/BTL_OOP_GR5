package com.coachweb.api;

import com.coachweb.service.OrderStatusService;
import com.coachweb.model.OrderStatusDTO;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderStatusAPI {

    @Autowired
    private OrderStatusService orderStatusService;

    @GetMapping("/order-status")
    public OrderStatusDTO getOrderStatus(@RequestParam String apptransid) {
        try {
            JSONObject result = orderStatusService.getOrderStatus(apptransid);

            OrderStatusDTO orderStatus = new OrderStatusDTO();
            for (String key : result.keySet()) {
                orderStatus.setKey(key);
                orderStatus.setValue(result.get(key).toString());
            }
            return orderStatus;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

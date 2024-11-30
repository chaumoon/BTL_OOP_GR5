package com.coachweb.service;

import org.json.JSONObject;

public interface OrderStatusService {
	JSONObject getOrderStatus(String apptransid);
}

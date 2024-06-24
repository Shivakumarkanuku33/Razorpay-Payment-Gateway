package com.shiva.service;

import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.shiva.dto.StudentOrder;
import com.shiva.repository.StudentRepository;

@Service
public class StudentService {

	@Autowired
	private StudentRepository studentRepository;
	
	@Value("${razorpay.key.id}")
	private String razorPayKey;
	
	@Value("${razorpay.secret.key}")
	private String razorPaySecret;
	
	private RazorpayClient client;
	
	public StudentOrder createOrder(StudentOrder stuOrder) throws RazorpayException {
		
		JSONObject orderReq = new JSONObject();
		
		orderReq.put("amount", stuOrder.getAmount() * 100); // amount in paisa
		orderReq.put("currency", "INR");
		orderReq.put("receipt", stuOrder.getEmail());
		
		this.client = new RazorpayClient(razorPayKey, razorPaySecret);
		
		//create orders in razorpay
		Order razorPayOrder = client.orders.create(orderReq);
		
		stuOrder.setRazorpayOrderId(razorPayOrder.get("id"));
		stuOrder.setOrderStatus(razorPayOrder.get("status"));
		
		StudentOrder studentOrder = studentRepository.save(stuOrder);
		
		return studentOrder;
		
	}
	
	public StudentOrder updateOrder(Map<String, String> responsePayLoad) {
		
		String razorpayOrderId = responsePayLoad.get("razorpay_order_id");
		
		StudentOrder order = studentRepository.findByRazorpayOrderId(razorpayOrderId);
		
		order.setOrderStatus("PAYMENT_COMPLETED");
		
		StudentOrder updatedOrder = studentRepository.save(order);
		
		return updatedOrder;
	}
	
}

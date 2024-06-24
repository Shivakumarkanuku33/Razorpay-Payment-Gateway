package com.shiva.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shiva.dto.StudentOrder;

public interface StudentRepository extends JpaRepository<StudentOrder, Integer> {

	public StudentOrder findByRazorpayOrderId(String orderId);
}

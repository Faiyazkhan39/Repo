package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;

import com.example.demo.model.Employee;
import com.example.demo.repository.EmployeeRepository;

@Controller
public class EmployeeController {

	@Autowired
	private EmployeeRepository empRepo;

	private static final String Emp = "Employee";

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	/*
	 * @EventListener(ApplicationReadyEvent.class) public void ShowAll() {
	 * 
	 * List<Employee> listStudents = empRepo.findAll();
	 * 
	 * for (Employee emp : listStudents) { System.out.println(emp.getEmployeeId());
	 * System.out.println(emp.getEmployeeAddress());
	 * System.out.println(emp.getEmployeeName());
	 * System.out.println(emp.getEmployeeNumber());
	 * 
	 * }
	 * 
	 * }
	 */

	@EventListener(ApplicationReadyEvent.class)
	public void insertRedis() {

		List<Employee> listStudents = empRepo.findAll();
		
		for (Employee emp : listStudents) {
			redisTemplate.opsForHash().put(Emp, emp.getEmployeeId(), emp);
		}

		if (redisTemplate.hasKey("Employee")) {
			System.out.println("present");
		} else {
			System.out.println("doesn't exist");
		}

	}

}

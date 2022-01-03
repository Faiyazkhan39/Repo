package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.model.Employee;
import com.example.demo.repository.EmployeeRepository;

@Controller
public class EmployeeController {

	@Autowired
	private EmployeeRepository empRepo;

	private static final String Emp = "Employee";

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	@GetMapping("/employeelist")
	public void ShowAll() {

		try {

			List<Employee> listStudents = empRepo.findAll();

			for (Employee emp : listStudents) {
				System.out.println(emp.getEmployeeId());
				System.out.println(emp.getEmployeeAddress());
				System.out.println(emp.getEmployeeName());
				System.out.println(emp.getEmployeeNumber());

			}

		} catch (Exception e) {
			e.printStackTrace();

		}

	}

	@GetMapping("/insertRedis")
	public void insertRedis() {

		try {

			List<Employee> listStudents = empRepo.findAll();

			for (Employee emp : listStudents) {

				redisTemplate.opsForHash().put(Emp, emp.getEmployeeId(), emp);

			}

			// Verify whether inserted in memory database or not

			if (redisTemplate.hasKey("Employee")) {
				System.out.println("present");
			} else {
				System.out.println("doesn't exist");
			}

		} catch (Exception e) {
			e.printStackTrace();

		}

	}

	@EventListener(ApplicationReadyEvent.class)
	public void Streamdemo() {

		try {

			List<Object> objlistStudents;
			objlistStudents = redisTemplate.opsForHash().values(Emp);
			System.out.println(redisTemplate.opsForHash().values(Emp));
			List<Employee> listStudents = new ArrayList<Employee>();

			for (Object emp : objlistStudents) {

				listStudents.add(((Employee) emp));

			}

			List<Employee> result = listStudents.stream().filter(line -> !"Faiyaz".equals(line.getEmployeeName()))
					.collect(Collectors.toList());
			result.forEach(System.out::println);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}

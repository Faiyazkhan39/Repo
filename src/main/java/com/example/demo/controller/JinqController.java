package com.example.demo.controller;

import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.model.Employee;

@Controller
public class JinqController {

	@PersistenceContext
	private EntityManager em;

	@Autowired
	private JinqSource source;

	// @EventListener(ApplicationReadyEvent.class)
	public Collection<Employee> findByName(Long id) {
		System.out.println("Starting");
		return source.employees(em).where(c -> c.getEmployeeId().equals(id)).toList();
	}

	@EventListener(ApplicationReadyEvent.class)
	@GetMapping("/JinqDemo")
	public void JinqDemo() {

		List<Employee> result = source.streamAll(em, Employee.class).where(c -> c.getEmployeeName().equals("Faiyaz"))
				.toList();
		result.forEach(System.out::println);

	}

}

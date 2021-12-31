package com.example.demo.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.dao.EmployeeDao;
import com.example.demo.model.Employee;
import com.example.demo.repository.EmployeeRepository;

public class EmployeeService implements EmployeeDao{
	
	@Autowired
	private EmployeeRepository empRepo;

	@Override
	public Optional<Employee> findById(Long id) {
	
		return empRepo.findById(id);
	}

}

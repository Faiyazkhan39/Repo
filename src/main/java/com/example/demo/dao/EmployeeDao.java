package com.example.demo.dao;

import java.util.Optional;

import com.example.demo.model.Employee;

public interface EmployeeDao {

	Optional<Employee> findById(Long id);

}

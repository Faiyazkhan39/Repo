package com.example.demo.dao;

import java.util.List;

import com.example.demo.model.Employee;

public interface EmployeeServiceDao {

	// Save operation
	Employee saveEmployee(Employee employee);

	// Read operation
	List<Employee> fetchEmployeeList();

	// Update operation
	Employee updateEmployee(Employee employee, Long employeeId);

	// Delete operation
	void deleteEmployeeById(Long employeeId);

}

package com.example.demo.service;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dao.EmployeeServiceDao;
import com.example.demo.model.Employee;
import com.example.demo.repository.EmployeeRepository;

@Service
public class EmployeeService implements EmployeeServiceDao {

	@Autowired
	private EmployeeRepository empRepo;

	@Override
	public Employee saveEmployee(Employee employee) {
		// TODO Auto-generated method stub
		return empRepo.save(employee);
	}

	@Override
	public List<Employee> fetchEmployeeList() {
		// TODO Auto-generated method stub
		return (List<Employee>) empRepo.findAll();
	}

	@Override
	public Employee updateEmployee(Employee employee, Long employeeId) {
		// TODO Auto-generated method stub
		Employee emp = empRepo.findById(employeeId).get();
		if(Objects.nonNull(employee.getEmployeeName())) {
			
			
		}

		return null;
	}

	@Override
	public void deleteEmployeeById(Long employeeId) {
		// TODO Auto-generated method stub
		empRepo.deleteById(employeeId);

	}

}

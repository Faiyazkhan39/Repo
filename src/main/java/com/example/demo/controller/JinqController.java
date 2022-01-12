package com.example.demo.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.model.Employee;
import com.example.demo.repository.EmployeeRepository;

@Controller
public class JinqController {

	@PersistenceContext
	private EntityManager em;

	@Autowired
	private JinqSource source;

	@Autowired
	private EmployeeRepository empRepo;

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	@GetMapping("/JinqDemo")
	public void JinqDemo() {

		List<Employee> result = source.streamAll(em, Employee.class).where(c -> c.getEmployeeName().equals("Ahmed"))
				.toList();
		result.forEach(System.out::println);

		for (Employee emp : result) {

			System.out.println(emp.getEmployeeId());
			System.out.println(emp.getEmployeeAddress());
			System.out.println(emp.getEmployeeName());
			System.out.println(emp.getEmployeeNumber());
			System.out.println(emp.getEmployeeDob());
			System.out.println(emp.getEmployeeDoj());
			System.out.println(emp.getEmployeePan());
			System.out.println(emp.getEmployeeSalary());
			System.out.println(emp.getRole());

		}

	}

//	@EventListener(ApplicationReadyEvent.class)
	@GetMapping("/Filterdata")
	public void Filterdata() throws Exception {

		try {

			String date1 = "2018-05-12 00:00:00+05:30"; // Create a date object
			System.out.println(date1);

			List<Employee> objemp = empRepo.findAll();

			// List<Employee> result = objemp.stream()
			// .filter(line -> date1.compareTo(new
			// SimpleDateFormat("yyyy/MM/dd").parse(line.getEmployeeAddress())))
			// .collect(Collectors.toList());
			// result.forEach(System.out::println);

			// System.out.println("Sorting on Employee Name");
			// List<Employee> sortedList =
			// objemp.stream().sorted(Comparator.comparing(Employee::getEmployeeName))
			// .collect(Collectors.toList());

			// sortedList.forEach(System.out::println);

			System.out.println("Filtering on 2 field");

			SimpleDateFormat dateParser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSX");

			List<Employee> result1 = objemp.stream().filter(line -> {
				Date date2 = null;

				try {
					date2 = dateParser.parse(date1);
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				try {
					return dateParser.parse(line.getEmployeeDoj()).compareTo(date2) > 0;
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return false;
			}).collect(Collectors.toList());
			result1.forEach(System.out::println);

		} catch (Exception e) {
			e.printStackTrace();

		}

	}

//	@EventListener(ApplicationReadyEvent.class)
	@GetMapping("/Filterexdata")
	public void Filterexdata() throws Exception {

		List<Employee> objemp = empRepo.findAll();

		List<Employee> result = objemp.stream()
				.filter(line -> line.getEmployeeSalary() >= 31000 && line.getEmployeeSalary() <= 40000)
				.collect(Collectors.toList());
		result.forEach(System.out::println);

	}

//	@EventListener(ApplicationReadyEvent.class)
	@GetMapping("/Filterdatademo")
	public void Filteredata() throws Exception {

		List<Employee> objemp = empRepo.findAll();

		List<Employee> result = objemp.stream()
				.filter(line -> line.getEmployeeSalary() >= 31000 && line.getEmployeeSalary() <= 40000)
				.collect(Collectors.toList());
		result.forEach(System.out::println);

	}

//	@EventListener(ApplicationReadyEvent.class)
	@GetMapping("/Dateformatter")
	public void Dateformatter() throws Exception {

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-dd-MM");
		String dateStr1 = "2020-08-18";
		// Parsing the given String to Date object
		Date date1 = formatter.parse(dateStr1);

		List<Employee> objemp = empRepo.findAll();

		objemp.stream().forEach(line -> {
			Date date2 = null;
			try {
				date2 = formatter.parse(line.getEmployeeDoj());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			if (date1.before(date2)) {

				System.out.println(date2);

			}
		});

	}

//	@EventListener(ApplicationReadyEvent.class)
	@GetMapping("/filterwithdate")
	public void filterwithdate() throws Exception {

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-dd-MM");
		String dateStr1 = "2020-08-18";
		// Parsing the given String to Date object
		Date date1 = formatter.parse(dateStr1);

		List<Employee> objemp = empRepo.findAll();

		objemp.stream().forEach(line -> {
			Date date2 = null;
			try {
				date2 = formatter.parse(line.getEmployeeDoj());
			} catch (ParseException e) {
				e.printStackTrace();
			}

			if (date1.before(date2) && line.getEmployeeSalary() >= 25000 && line.getEmployeeSalary() <= 30000) {

				System.out.println(date2);

			}
		});

	}

	@EventListener(ApplicationReadyEvent.class)
	@GetMapping("/insertDataRedis")
	public void insertDataRedis() {

		try {

			List<Employee> listemp = empRepo.findAll();

			for (Employee emp : listemp) {

				// redisTemplate.opsForHash().put("", emp.getEmployeeId(), emp);
				// redisTemplate.opsForHash().put(emp.getEmployeeId().toString(),
				// emp.getEmployeeId(), emp);

				redisTemplate.opsForList().leftPush(emp.getEmployeeId().toString(), emp);

			}
		
			// Verify whether inserted in memory database or not
		//System.out.println(redisTemplate.boundListOps("1"));	
			
			
			redisTemplate.opsForList().rightPop("1");
			Object objlistEmp;
			objlistEmp = 	redisTemplate.opsForList().rightPop("1");
			
			System.out.println(objlistEmp.toString());
			

			if (redisTemplate.hasKey("4")) {
				System.out.println("present");
			} else {
				System.out.println("doesn't exist");
			}

		} catch (Exception e) {
			e.printStackTrace();

		}

	}
}

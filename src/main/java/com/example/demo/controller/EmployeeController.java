package com.example.demo.controller;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Employee;
import com.example.demo.repository.EmployeeRepository;
import com.example.demo.service.EmployeeService;
import com.example.demo.service.ExceptionService;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

@RestController
public class EmployeeController {

	@Autowired
	private EmployeeRepository empRepo;

	public static final String Emp = "1";

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	@Autowired
	private EmployeeService EmpService;

	@GetMapping("/employeelist")
	public void ShowAll() {

		List<Employee> objemp = empRepo.findAll();

		for (Employee emp : objemp) {

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

	@GetMapping("/insertRedis")
	public void insertRedis() {

		try {

			List<Employee> listemp = empRepo.findAll();

			for (Employee emp : listemp) {

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

	@GetMapping("/streamDemo")
	public void Streamdemo() {

		try {

			List<Object> objlistEmp;
			objlistEmp = redisTemplate.opsForHash().values(Emp);
			System.out.println(redisTemplate.opsForHash().values(Emp));
			List<Employee> listStudents = new ArrayList<Employee>();
			for (Object emp : objlistEmp) {

				listStudents.add(((Employee) emp));

			}

			List<Employee> result = listStudents.stream().filter(line -> !"mumbai".equals(line.getEmployeeAddress()))
					.collect(Collectors.toList());
			result.forEach(System.out::println);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@GetMapping("/StreamApiDemo")
	public void StreamApidemo() {

		try {

			List<Object> objlistStudents;
			objlistStudents = redisTemplate.opsForHash().values(Emp);
			System.out.println(redisTemplate.opsForHash().values(Emp));
			List<Employee> listStudents = new ArrayList<Employee>();

			for (Object emp : objlistStudents) {

				listStudents.add(((Employee) emp));

			}

			List<Employee> result = listStudents.stream().filter(
					line -> !"Mumbai".equals(line.getEmployeeAddress()) && !"Sameer".equals(line.getEmployeeName()))
					.collect(Collectors.toList());
			result.forEach(System.out::println);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@GetMapping("/StreamApiDemoSort")
	public void StreamApidemoSort() {

		try {

			List<Object> objlistStudents;
			objlistStudents = redisTemplate.opsForHash().values(Emp);
			System.out.println(redisTemplate.opsForHash().values(Emp));
			List<Employee> listStudents = new ArrayList<Employee>();

			for (Object emp : objlistStudents) {

				listStudents.add(((Employee) emp));

			}

			List<Employee> sortedList = listStudents.stream().sorted(Comparator.comparing(Employee::getEmployeeId))
					.collect(Collectors.toList());

			sortedList.forEach(System.out::println);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@GetMapping("/PDFgenerator")
	public void PDFgenerator() throws MalformedURLException, IOException {

		{
			Document document = new Document();
			try {

				List<Employee> objemppdf = empRepo.findAll();

				PdfWriter writer = PdfWriter.getInstance(document,
						new FileOutputStream("D:\\PDF\\EmployeeDetails.pdf"));
				document.open();
				PdfPTable table = new PdfPTable(4);
				table.setWidthPercentage(100);
				table.setSpacingBefore(10f);
				table.setSpacingAfter(10f);

				table.setHeaderRows(1);

				Font bold = new Font(FontFamily.HELVETICA, 12, Font.BOLD);

				table.addCell(new PdfPCell(new Paragraph("ID", bold)));
				table.addCell(new PdfPCell(new Paragraph("Address", bold)));
				table.addCell(new PdfPCell(new Paragraph("Name", bold)));
				table.addCell(new PdfPCell(new Paragraph("Phone", bold)));

				for (Employee emp : objemppdf) {

					PdfPCell cell1 = new PdfPCell(new Paragraph(emp.getEmployeeId().toString()));
					PdfPCell cell2 = new PdfPCell(new Paragraph(emp.getEmployeeAddress()));
					PdfPCell cell3 = new PdfPCell(new Paragraph(emp.getEmployeeName()));
					PdfPCell cell4 = new PdfPCell(new Paragraph(Integer.toString(emp.getEmployeeNumber())));

					table.addCell(cell1);
					table.addCell(cell2);
					table.addCell(cell3);
					table.addCell(cell4);

				}

				Image image1 = Image.getInstance("D:\\PDF\\logo.png");
				image1.setAlignment(Element.ALIGN_CENTER);
				image1.scaleAbsolute(120, 60);
				image1.setAlignment(Element.ALIGN_RIGHT);
				image1.setSpacingAfter(10f);

				document.add(image1);
				document.add(table);

				document.newPage();
				document.add(new Paragraph("This is Page 2"));

				document.newPage();
				document.add(new Paragraph("This is Page 3"));
				document.add(image1);

				document.close();
				writer.close();
			} catch (DocumentException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}

	}

//	@EventListener(ApplicationReadyEvent.class)
	@GetMapping("/Exceptiondemo")
	public void Exceptiondemo() {

		ExceptionService obj = new ExceptionService();
		obj.Expser();

	}

	// Save operation
	@PostMapping("/employees")
	public ResponseEntity<Employee> saveEmployees(@RequestBody Employee employee) {
		Employee enpsave = EmpService.saveEmployee(employee);
		return new ResponseEntity<Employee>(enpsave, HttpStatus.CREATED);
	}

	// Read operation
	@GetMapping("/employees")
	public ResponseEntity<List<Employee>> fetchDepartmentList() {

		List<Employee> emplist = EmpService.fetchEmployeeList();
		if (emplist != null) {
			System.out.println("In Error log");
		//	String s=null;  
		//	System.out.println(s.length());//NullPointerException  

			
		//	int data = 100 / 0;
			
			throw new GlobalExceptionConfig();
		}

		return new ResponseEntity<List<Employee>>(emplist, HttpStatus.OK);
	}

	// Update operation
	@PutMapping("/employees/{id}")
	public Employee updateEmployee(@RequestBody Employee employee, @PathVariable("id") Long employeeId) {
		return EmpService.updateEmployee(employee, employeeId);
	}

	// Delete operation
	@DeleteMapping("/employees/{id}")
	public ResponseEntity<Void> deleteEmployeeById(@PathVariable("id") Long employeeId) {
		EmpService.deleteEmployeeById(employeeId);
		return new ResponseEntity<Void>(HttpStatus.ACCEPTED);
	}

}

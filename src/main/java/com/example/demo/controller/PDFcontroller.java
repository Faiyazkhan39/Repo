package com.example.demo.controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;

@Controller
public class PDFcontroller {
	
	@EventListener(ApplicationReadyEvent.class)
	@GetMapping("/PDFhtml")
	public void PDFhtml() {
		try {
			OutputStream file = new FileOutputStream(new File("D:\\PDF\\HtmltoPDF.pdf"));
			Document document = new Document();
			PdfWriter writer = PdfWriter.getInstance(document, file);
			StringBuilder htmlString = new StringBuilder();
			htmlString.append(new String(
					"<html><body> Hii Welcome to a Merce technology <table border='2' align='center'> "));
			htmlString.append(new String(
					"<tr><td>Employee</td><td><a href='www.example.com'>Details</a> </td></tr>"));
			htmlString.append(new String(
					"<tr> <td> Department </td> <td><a href='www.demo.com'>Info</a> </td> </tr></table></body></html>"));

			document.open();
			InputStream is = new ByteArrayInputStream(htmlString.toString().getBytes());
			XMLWorkerHelper.getInstance().parseXHtml(writer, document, is);
			document.close();
			file.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}

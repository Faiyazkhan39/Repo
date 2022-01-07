package com.example.demo.controller;

import java.io.IOException;

import org.apache.lucene.queryParser.ParseException;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.lucene.LuceneService;

@Controller
public class LuceneTester {

	@GetMapping("/LuceneDemo")
	public void LuceneDemo() {

		LuceneService tester = new LuceneService();

		try {
			tester.createIndex();
			tester.search("Mohan");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
}

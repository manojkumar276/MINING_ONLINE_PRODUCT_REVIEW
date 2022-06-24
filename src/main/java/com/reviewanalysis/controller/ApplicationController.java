package com.reviewanalysis.controller;

import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.reviewanalysis.model.Result;
import com.reviewanalysis.service.ProductReviewAnalysisService;

@Controller
public class ApplicationController {

	@Autowired
	private ProductReviewAnalysisService productReviewAnalysisService;

	@CrossOrigin(origins = "http://localhost:3000")
	@GetMapping("/search/{product}")
	public ResponseEntity<?> getProductReviewAnalysis(@PathVariable("product") String product)
			throws InterruptedException, ExecutionException {
		System.out.println("Completed");
		Result result = productReviewAnalysisService.getProductReviewAnalysis(product);
		return ResponseEntity.ok(result);
	}
	

}

package com.reviewanalysis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class OnlineProductReviewAnalysisApplication {

	public static void main(String[] args) {
		System.out.print("**************** Application Started ***************");
		SpringApplication.run(OnlineProductReviewAnalysisApplication.class, args);
	}

}

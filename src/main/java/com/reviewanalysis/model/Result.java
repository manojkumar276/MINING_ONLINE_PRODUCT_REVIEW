package com.reviewanalysis.model;


import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Result {
	
	private List<Product> amazon = new ArrayList<>();
	private List<Product> flipkart = new ArrayList<>();

	public Result() {
		
	}

}

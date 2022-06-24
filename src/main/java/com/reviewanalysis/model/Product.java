package com.reviewanalysis.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Product {
	private String name;
	private String link;
	private String image;
	private float rating;
	private double price;
	private List<String> reviews = new ArrayList<>();
	private String type;
	private boolean isGood = false;
	private float positiveCount;
	private float negativeCount;
	private float neutralCount;
	private String suggestion = "Bad Product";

	public Product() {

	}

}

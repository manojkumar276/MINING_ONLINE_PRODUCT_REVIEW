package com.reviewanalysis.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import com.reviewanalysis.model.Product;

@Service
public class AmazonProductAnalysisService {

	@Autowired
	private ProductSentimentAnalyzer analyser;

    @Async
	public CompletableFuture<List<Product>> getAmazonProductDetails(String product) throws InterruptedException {
        Thread.sleep(3000); // sleep for 3 sec
		String URL = getAmazonURL(product);
		try {
			return new AsyncResult<List<Product>>(getAmazonReview(URL)).completable();

		} catch (Exception e) {
			System.out.println("Error while getting data from amazon");
		}
		return null;
	}

	private List<Product> getAmazonReview(String url) throws IOException {
		List<Product> amazonProducts = new ArrayList<>();
		Document doc = Jsoup.connect(url).userAgent(
				"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/40.0.2214.38 Safari/537.36")
				.get();
		// Document doc = Jsoup.connect(url).userAgent("Mozilla").get();
		Elements products = doc.select("div.s-asin");
		System.out.println(doc.title());
		for (Element product : products) {

			String name = product.select("h2.a-size-mini").select("span.a-size-medium").get(0).text();
			String reviewLink = product.select("div.a-spacing-top-micro").select("a[href]").get(1).attr("href");

			String productLink = product.select("h2.a-size-mini").select("a[href]").get(0).attr("href");
			String image = product.select("img.s-image").attr("src");
			try {
				Float rating = Float
						.parseFloat(product.select("i.a-icon-star-small").select("span").get(0).text().split(" ")[0]);
				double price = Double.parseDouble(product.select("span.a-price-whole").get(0).text());
				System.out.println("****************************");
				System.out.println(name);
				System.out.println(productLink);
				System.out.println(rating);
				System.out.println(price);
				List<String> reviews = getReviews(reviewLink);
				Product amz = new Product();
				amz.setName(name);
				amz.setPrice(price);
				amz.setLink(productLink);
				amz.setRating(rating);
				amz.setReviews(reviews);
				amz.setType("amazon");
				analyser.sentimentAnalyzeProduct(amz);
				amz.setImage(image);
				amazonProducts.add(amz);
				System.out.println(amz.toString());
				System.out.println("****************************");

			} catch (Exception e) {
				System.out.println("Error while getting data for item" + e.getMessage());

			}
		}
		return amazonProducts;
	}

	private List<String> getReviews(String link) {
		List<String> reviews = new ArrayList<>();
		try {
			String productUrl = "http://www.amazon.in" + link;
			Document doc = Jsoup.connect(productUrl).userAgent(
					"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/40.0.2214.38 Safari/537.36")
					.timeout(30000).get();
			Elements reviewsList = doc.select("a.review-title-content");
			for (Element review : reviewsList) {
				System.out.println(review.select("span").get(0).text());
				reviews.add(review.select("span").get(0).text());
			}

		} catch (Exception e) {
			System.out.println("Error while getting reviews in Amazon" + e.getMessage());

		}
		return reviews;
	}

	private String getAmazonURL(String product) {
		String key = product.replaceAll("\\s+", "+");
		return "http://www.amazon.in/s?url=search-alias%3Daps&field-keywords=" + key;
	}

}

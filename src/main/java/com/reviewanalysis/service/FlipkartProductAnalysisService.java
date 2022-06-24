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
public class FlipkartProductAnalysisService {

	@Autowired
	private ProductSentimentAnalyzer analyser;

    @Async
	public CompletableFuture<List<Product>> getFlipkartProductDetails(String product) throws InterruptedException {
        Thread.sleep(2000); // sleep for 2 sec
		String URL = getFlipkartURL(product);
		try {
			return new AsyncResult<List<Product>>(getFlipkartReview(URL)).completable();

		} catch (Exception e) {
			System.out.println("Error while getting data from flipkart");
		}
		return null;
	}

	private List<Product> getFlipkartReview(String url) throws IOException {
		List<Product> flipkartProducts = new ArrayList<>();
		Document doc = Jsoup.connect(url).userAgent(
				"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/40.0.2214.38 Safari/537.36")
				.get();
		Elements products = doc.select("div[data-id]");
		System.out.println(doc.title());
		for (Element product : products) {
			String name = product.select("a.s1Q9rs").get(0).text();
			String reviewLink = product.select("a.s1Q9rs").attr("href");
			String productLink = product.select("a.s1Q9rs").attr("href");
			String image = product.select("div.CXW8mj").select("img").attr("src");
			try {
				Float rating = Float.parseFloat(product.select("div.gUuXy-").select("div._3LWZlK").text());
				double price = Double.parseDouble(product.select("div._30jeq3").text().strip().split("₹")[1]);
				System.out.println("****************************");
				System.out.println(name);
				System.out.println(productLink);
				System.out.println(rating);
				System.out.println(price);
				List<String> reviews = getReviews(reviewLink);
				Product flip = new Product();
				flip.setName(name);
				flip.setPrice(price);
				flip.setLink(productLink);
				flip.setRating(rating);
				flip.setReviews(reviews);
				flip.setType("flipkart");
				analyser.sentimentAnalyzeProduct(flip);
				flip.setImage(image);
				flipkartProducts.add(flip);
				System.out.println(flip.toString());
				System.out.println("****************************");

			} catch (Exception e) {
				System.out.println("Error while getting data for item" + e.getMessage());

			}

		}
		return flipkartProducts;
	}

	private List<String> getReviews(String link) {
		List<String> reviews = new ArrayList<>();
		try {
			String productUrl = "https://www.flipkart.com" + link;
			Document doc = Jsoup.connect(productUrl).userAgent(
					"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/40.0.2214.38 Safari/537.36")
					.timeout(30000).get();
			Elements reviewsList = doc.select("div._16PBlm");
			for (Element review : reviewsList) {
				System.out.println(review.select("div._2-N8zT").text());
				reviews.add(review.select("p._2-N8zT").text());
			}

		} catch (Exception e) {
			System.out.println("Error while getting reviews in flipkart" + e.getMessage());

		}
		return reviews;
	}

	private String getFlipkartURL(String product) {
		String key = product.replaceAll("\\s+", "+");
		return "https://www.flipkart.com/search?q=" + key;
	}

}

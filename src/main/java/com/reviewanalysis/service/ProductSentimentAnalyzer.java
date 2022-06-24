package com.reviewanalysis.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.reviewanalysis.model.Product;
import com.vader.sentiment.analyzer.SentimentAnalyzer;

@Service
public class ProductSentimentAnalyzer {

	public Product sentimentAnalyzeProduct(Product product) {
		try {
			List<String> reviews = product.getReviews();
			double positive = 0;
			double negative = 0;
			double neutral = 0;
			for (String sentence : reviews) {
				System.out.println(sentence);
				SentimentAnalyzer sentimentAnalyzer = new SentimentAnalyzer(sentence);
				sentimentAnalyzer.analyze();
				System.out.println(sentimentAnalyzer.getPolarity());
				positive += sentimentAnalyzer.getPolarity().get("positive") * 100;
				negative += sentimentAnalyzer.getPolarity().get("negative") * 100;
				neutral += sentimentAnalyzer.getPolarity().get("neutral") * 100;

			}
			if (!reviews.isEmpty()) {
				product.setPositiveCount(round(positive / reviews.size(), 3));
				product.setNegativeCount(round(negative / reviews.size(), 3));
				product.setNeutralCount(round(neutral / reviews.size(), 3));
				if (product.getPositiveCount() > 10 & product.getNeutralCount() > 10 & product.getRating() > 4) {
					product.setGood(true);
					product.setSuggestion("Good");
				} else if (product.getPositiveCount() > 6 & product.getNeutralCount() > 10
						& product.getNegativeCount() > 8) {
					product.setGood(true);
					product.setSuggestion("Neutral");
				}
			}

		} catch (Exception e) {
			System.out.println("Error while sentiment analysis");
		}

		return product;
	}

	public static float round(double value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();

		long factor = (long) Math.pow(10, places);
		value = value * factor;
		long tmp = Math.round(value);
		return (float) tmp / factor;
	}
}

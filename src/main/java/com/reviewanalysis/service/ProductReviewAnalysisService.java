package com.reviewanalysis.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reviewanalysis.model.Product;
import com.reviewanalysis.model.Result;

@Service
public class ProductReviewAnalysisService {

	@Autowired
	private AmazonProductAnalysisService amazon;

	@Autowired
	private FlipkartProductAnalysisService flipkart;

	public Result getProductReviewAnalysis(String product) throws InterruptedException, ExecutionException {
	    long start = System.currentTimeMillis();

		Result result = new Result();
		CompletableFuture<List<Product>> amz = amazon.getAmazonProductDetails(product);
        CompletableFuture<List<Product>> flip = flipkart.getFlipkartProductDetails(product);
        
        result.setAmazon(amz.get());
        result.setFlipkart(flip.get());
	    System.out.println("Elapsed time: " + (System.currentTimeMillis() - start));

		return result;
	}

}

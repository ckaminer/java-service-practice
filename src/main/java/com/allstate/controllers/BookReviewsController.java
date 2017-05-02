package com.allstate.controllers;

import com.allstate.models.NYTBookReviewResults;
import com.allstate.services.NYTBookReviewsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by localadmin on 5/2/17.
 */
@RestController
public class BookReviewsController {
    private NYTBookReviewsService nytBookReviewsService;

    BookReviewsController(NYTBookReviewsService nytBookReviewsService) {
        this.nytBookReviewsService = nytBookReviewsService;
    }

    @GetMapping
    public NYTBookReviewResults getReviews(@RequestParam String search) {
        return nytBookReviewsService.forTitle(search);
    }
}

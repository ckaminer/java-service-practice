package com.allstate.services;

import com.allstate.models.NYTBookReviewResults;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;

@Service
public class NYTBookReviewsService {

    private final RestTemplate template = new RestTemplate();
    private final String NYT_API_KEY = "<YOUR API KEY HERE>";
    private final String API_URL = "https://api.nytimes.com/svc/books/v3/reviews.json";

    public NYTBookReviewResults forTitle(String search) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(API_URL)
                .queryParam("api-key", NYT_API_KEY)
                .queryParam("title", search);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<NYTBookReviewResults> exchange = template.exchange(
                builder.build().encode().toUri(),
                HttpMethod.GET,
                entity,
                NYTBookReviewResults.class);

        return exchange.getBody();
    }

    public RestTemplate getRestTemplate() {
        return template;
    }
}
package com.allstate;

import com.google.gson.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.util.UriUtils;

import java.io.UnsupportedEncodingException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookReviewsIntegrationTest {
    @Autowired
    TestRestTemplate template;

    @Test
    public void returnsReviews() throws UnsupportedEncodingException {
        JsonObject requestBody = new JsonObject();
        String path = "/book-reviews?search=In the Heart of the Sea: The Tragedy of the Whaleship Essex";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Gson builder = new GsonBuilder().create();
        String jsonString = builder.toJson(requestBody);
        HttpEntity<String> request = new HttpEntity<>(jsonString, headers);

        ResponseEntity<String> response = template.exchange(path, HttpMethod.GET, request, String.class);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        JsonArray resultObjects = new JsonParser().parse(response.getBody()).getAsJsonObject().getAsJsonArray("results");
        assertEquals("NYT Book Review API returns 1 result for a known search", 1, resultObjects.size());
        JsonObject result = resultObjects.get(0).getAsJsonObject();
        assertThat(result.get("url").getAsString(), equalTo("http://www.nytimes.com/2000/05/24/books/books-of-the-times-no-ahab-but-one-destructive-whale.html"));
        assertThat(result.get("byline").getAsString(), equalTo("RICHARD BERNSTEIN"));
        assertThat(result.get("book_title").getAsString(), equalTo("In the Heart of the Sea: The Tragedy of the Whaleship Essex"));
        assertThat(result.get("book_author").getAsString(), equalTo("Nathaniel Philbrick"));

        Gson gson = new Gson();
        String[] isbns = gson.fromJson(result.get("isbn13"), String[].class);
        String[] isbnArray = new String[]{"9780141001821",
                "9780143126812",
                "9780606217811",
                "9780606365741",
                "9780670891573",
                "9781101221570",
                "9781101997765",
                "9781439566718",
                "9781504655798",
                "9781568951782",
                "9781568959443",
                "9781611763577"};
        assertArrayEquals(isbnArray, isbns);
    }
}
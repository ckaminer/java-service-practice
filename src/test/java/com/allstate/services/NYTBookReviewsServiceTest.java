package com.allstate.services;

import com.allstate.models.NYTBookReviewResults;
import com.allstate.services.NYTBookReviewsService;
import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.stringContainsInOrder;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

public class NYTBookReviewsServiceTest {
    private final NYTBookReviewsService service = new NYTBookReviewsService();

    @Test
    public void callsMockRestTemplateWithTitleAndReturnsAList() throws Exception {
        MockRestServiceServer mockServer = MockRestServiceServer.createServer(service.getRestTemplate());

        mockServer.expect(requestTo(stringContainsInOrder(Arrays.asList("https://api.nytimes.com/svc/books/v3/reviews.json?", "api-key=", "title=In%20the%20Heart%20of%20the%20Sea:%20The%20Tragedy%20of%20the%20Whaleship%20Essex"))))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(getJSON("/heartOfTheSea.json"), MediaType.APPLICATION_JSON));

        NYTBookReviewResults nytBookReviewResults = service.forTitle("In the Heart of the Sea: The Tragedy of the Whaleship Essex");
        NYTBookReviewResults.NYTBookReviewResult result = nytBookReviewResults.getResults().get(0);

        assertEquals(result.getBookTitle(), "In the Heart of the Sea: The Tragedy of the Whaleship Essex");
        assertEquals(result.getBookAuthor(), "Nathaniel Philbrick");
        assertEquals(result.getByline(), "RICHARD BERNSTEIN");
        assertEquals(result.getUrl(), "http://www.nytimes.com/2000/05/24/books/books-of-the-times-no-ahab-but-one-destructive-whale.html");
        List<String> isbns = Arrays.asList("9780141001821",
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
                "9781611763577");
        assertEquals(result.getIsbn13(), isbns);

        mockServer.verify();
    }

    private String getJSON(String path) throws Exception {
        URL url = this.getClass().getResource(path);
        return new String(Files.readAllBytes(Paths.get(url.getFile())));
    }
}

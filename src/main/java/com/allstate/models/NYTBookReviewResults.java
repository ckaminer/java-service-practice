package com.allstate.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class NYTBookReviewResults {
    private List<NYTBookReviewResult> results;

    public List<NYTBookReviewResult> getResults() {
        return results;
    }

    public static class NYTBookReviewResult {

        private String bookTitle;
        private String bookAuthor;
        private String byline;
        private String url;
        private List<String> isbn13;

        @JsonProperty("book_title")
        public String getBookTitle() {
            return bookTitle;
        }

        @JsonProperty("book_author")
        public String getBookAuthor() {
            return bookAuthor;
        }

        public String getByline() {
            return byline;
        }

        public String getUrl() {
            return url;
        }

        public List<String> getIsbn13() { return isbn13; }
    }
}

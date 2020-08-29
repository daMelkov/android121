package com.astra.melkovhw121;

public class Article {
    private String header;
    private String article;

    public String getHeader() {
        return header;
    }

    public String getArticle() {
        return article;
    }

    public Article(String header, String article) {
        this.header = header;
        this.article = article;
    }
}

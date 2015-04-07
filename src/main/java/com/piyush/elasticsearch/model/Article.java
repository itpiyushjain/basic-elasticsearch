package com.piyush.elasticsearch.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;


/**
 * Created by jain06 on 3/4/15.
 */

public class Article {

    private String id;
    private String title;
    private String content;
    private String source;
    private List<String> authors;

    public Article(String id, String title, List<String> authors){
        this.id = id;
        this.title = title;
        this.authors = new ArrayList<String>();
        this.authors = authors;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public void addAuthors(List<String> authors) {
        this.authors.addAll(authors);
    }

    public void removeAuthors(List<String> authors) {
        this.authors.removeAll(authors);
    }

    public String toJson() throws IOException {
        return jsonBuilder().startObject()
                .field("id",id)
                .field("title", title)
                .field("content", content)
                .field("source", source)
                .field("authors", authors)
                .endObject()
                .string();
    }
}
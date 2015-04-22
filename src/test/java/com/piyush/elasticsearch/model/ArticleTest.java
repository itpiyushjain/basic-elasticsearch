package com.piyush.elasticsearch.model;


import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jain06 on 3/4/15.
 */


public class ArticleTest {

    @Test
    public void shouldGenerateJson() throws IOException {

        String expectedJson = "{\"id\":\"art1428080044517\",\"title\":\"Sample Title\",\"content\":\"Dummy content for the article\",\"source\":\"Biomed\",\"authors\":[\"Author 1\",\"Author 2\"]}";

        List<String> authors = new ArrayList<String>();
        authors.add("Author 1");
        authors.add("Author 2");

        Article article = new Article("art1428080044517",
                                      "Sample Title",
                                       authors);
        article.setContent("Dummy content for the article");
        article.setSource("Biomed");
        assertEquals(expectedJson, article.toJson());
        System.out.println(article.toJson());
    }
}

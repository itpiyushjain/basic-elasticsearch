package com.piyush.rssfeedreader;

import com.piyush.elasticsearch.model.Article;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by jain06 on 4/4/15.
 */

public class LatestArticlesFeedReader extends FeedReader {

    private String url;

    private List<Article> articles;

    public LatestArticlesFeedReader(String url){
        this.url = url;
        articles = new ArrayList<Article>();
    }

    @Override
    protected URL getFeedURL() throws MalformedURLException {
        return new URL(url);
    }

    @Override
    protected void populateModel(Map<String, String> itemContent) {
        List<String> authors = new ArrayList<String>();
        authors.add(itemContent.get(AUTHOR));
        String id = itemContent.get(IDENTIFIER);
        if(id.contains("/")){
            id = id.substring(id.indexOf("/")+1);
        }
        Article article = new Article(id,
                                      itemContent.get(TITLE),
                                      authors);
        article.setContent(itemContent.get(DESCRIPTION));
        String source = itemContent.get(SOURCE);
        if(source.contains(",")) {
            source = source.substring(0,source.indexOf(","));
        }
        article.setSource(source);
        articles.add(article);
    }

    public List<Article> getArticles() {
        return articles;
    }

}
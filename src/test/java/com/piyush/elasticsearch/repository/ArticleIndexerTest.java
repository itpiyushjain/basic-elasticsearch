package com.piyush.elasticsearch.repository;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@ContextConfiguration(locations = {"classpath:application-context.xml"})
public class ArticleIndexerTest {

    @Autowired
    private ArticleIndexer indexer;

    @Before
    public void initIndexer(){
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("/application-context.xml");
        indexer = (ArticleIndexer)applicationContext.getBean("indexer");
    }

    @After
    public void emptyData(){
        indexer.deleteAll();
        System.out.println("empty Done");
    }

    @Test
    public void indexAndFindArticlesTest() throws IOException, Throwable {

        List<ArticleType> articles = getArticles();

        indexer.indexArticles(articles);

        ArticleType article = indexer.searchById("art1");
        System.out.println(article==null?"null":article.toJson());
        List<ArticleType> articlesFound = indexer.searchByContent("Title 2");
        System.out.println(articles==null?"null":articlesFound.size() + " found");
    }

    private List<ArticleType> getArticles() {

        List<ArticleType> articles = new ArrayList<ArticleType>();
        ArticleType type = null;
        List<String> authors = null;

        for(int i=0;i<5;i++){
            type = new ArticleType();
            type.setId("art"+ i);
            type.setTitle("Article Title " + i);
            type.setContent("Article Content " + i);
            type.setSource("BiomedCentral");

            authors = new ArrayList<String>();
            authors.add("Test Author " + i);
            type.setAuthors(authors);

            articles.add(type);
        }


        return articles;
    }


}

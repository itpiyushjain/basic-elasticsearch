package com.piyush.elasticsearch.client;

import com.piyush.elasticsearch.model.Article;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by jain06 on 3/4/15.
 */
public class ElasticSearchClientTest {

    String host = "localhost";
    int port = 9300;
    String clusterName = "jpiy_es_cluster";
    String indexName = "articles";

    @Test
    public void testIndexArticle() throws Exception {

        try{
            ElasticSearchClient articleSearchClient = new ElasticSearchClient(host, port, clusterName, indexName);
            articleSearchClient.indexArticle(newArticle());
        }catch (Exception e){
            System.out.println("Exception in indexing article" + e);
            e.printStackTrace();
        }
    }

    @Test
    public void testGetArticle(){

        try{
            ElasticSearchClient articleSearchClient = new ElasticSearchClient(host, port, clusterName, indexName);
            Article article = articleSearchClient.getArticle("s40781-014-0035-z");
            System.out.println(article.toJson());
        }catch (Exception e){
            System.out.println("Exception in getting article" + e);
            e.printStackTrace();
        }

    }

    @Test
    public void testSearchArticle() {
        try{
            ElasticSearchClient articleSearchClient = new ElasticSearchClient(host, port, clusterName, indexName);
            List<Article> articles = articleSearchClient.searchArticle("Sample");
            System.out.println(articles.size() + " articles found");

        }catch (Exception e){
            System.out.println("Exception in searching article" + e);
            e.printStackTrace();
        }
    }


        private Article newArticle() {

        List<String> authors = new ArrayList<String>();
        authors.add("Author 1");
        authors.add("Author 2");
        Article article = new Article("art123456"  ,
                                      "Sample Title on " + new Date(),
                                       authors);
        article.setContent("Dummy content for the article generated on "
                            + new Date()
                            + " some random "
                            + new Random().nextDouble());
        article.setSource("Biomed");
        return article;
    }
}

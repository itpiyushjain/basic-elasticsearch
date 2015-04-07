package com.piyush.rssfeedreader;

import com.piyush.elasticsearch.client.ElasticSearchClient;
import com.piyush.elasticsearch.model.Article;
import org.junit.Test;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.util.List;

/**
 * Created by jain06 on 4/4/15.
 */
public class LatestArticlesFeedReaderTest {

    @Test
    public void testReadFeed(){

        LatestArticlesFeedReader feedReader = new LatestArticlesFeedReader("http://www.springeropen.com/latest/rss");

        String host = "localhost";
        int port = 9300;
        String clusterName = "jpiy_es_cluster";
        String indexName = "articles";

        try {
            feedReader.readFeed();
            List<Article> articles = feedReader.getArticles();
            System.out.print(articles);
            ElasticSearchClient articleSearchClient = new ElasticSearchClient(host, port, clusterName, indexName);
            articleSearchClient.indexArticles(articles);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}

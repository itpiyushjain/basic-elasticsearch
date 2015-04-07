package com.piyush.elasticsearch.client;

import com.piyush.elasticsearch.model.Article;
import org.apache.lucene.util.QueryBuilder;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

import java.io.IOException;
import java.util.*;

/**
 * Created by jain06 on 3/4/15.
 */
public class ElasticSearchClient {

    private static TransportClient client;

    private String indexName;
    private String clusterName;
    private String host;
    private int port;

    private static final String ARTICLE_TYPE_NAME = "Article";

    private TransportClient initClient() {
        Settings settings = ImmutableSettings.settingsBuilder().put("cluster.name", clusterName).build();
        TransportClient transportClient = new TransportClient(settings);
        return transportClient.addTransportAddress(new InetSocketTransportAddress(host, port));
    }

    public ElasticSearchClient(String host, int port, String clusterName, String indexName){
        this.host = host;
        this.port = port;
        this.indexName = indexName;
        this.clusterName = clusterName;
        if(client == null){
            client = initClient();
        }
    }

    public void indexArticle(Article article) throws IOException {
        //prepareIndexIfNotDone();

        IndexResponse response = client.prepareIndex(indexName, ARTICLE_TYPE_NAME, article.getId())
                   .setSource(article.toJson())
                   .execute()
                   .actionGet();

        String id = response.getId();
        System.out.println(" INDEXED : Article "  + article.getId() +
                                       " as:" + response.getId() +
                                       " version:" + response.getVersion());
    }

    public void indexArticles(List<Article> articles) throws Exception {
        //prepareIndexIfNotDone();

        BulkRequestBuilder bulkReq = client.prepareBulk();

        for(Article article: articles){
            bulkReq.add(client.prepareIndex(indexName, ARTICLE_TYPE_NAME, article.getId())
                              .setSource(article.toJson())
                       );

        }

        BulkResponse bulkRes = bulkReq.execute().actionGet();

        if(bulkRes.hasFailures()){
            throw new Exception(bulkRes.buildFailureMessage());
        }
        System.out.println(" INDEXED : Articles "  + articles.size());
    }

    public Article getArticle(String articleId) throws IOException {
        //prepareIndexIfNotDone();

        GetResponse response = client.prepareGet(indexName, ARTICLE_TYPE_NAME, articleId)
                .execute()
                .actionGet();

        String id = response.getId();
        Map<String, Object> articleMap = response.getSource();
        Article art = new Article((String)articleMap.get("id"), (String)articleMap.get("title"), (List<String>)articleMap.get("authors"));
        art.setContent((String)articleMap.get("content"));
        art.setSource((String)articleMap.get("source"));

        System.out.println(" GET : ArticleID: " + articleId + " content: " + articleMap);
        return art;
    }

    public List<Article> searchArticle(String contentKeyWord) throws IOException {
        //prepareIndexIfNotDone();

        MultiSearchResponse response = client.prepareMultiSearch()
                                  .add(client.prepareSearch(indexName).setQuery(QueryBuilders.termQuery("content", contentKeyWord)))
                                  .add(client.prepareSearch(indexName).setQuery(QueryBuilders.termQuery("title", contentKeyWord)))
                                  .execute()
                                  .actionGet();

        MultiSearchResponse.Item[] items = response.getResponses();
        Set<String> articleIds = new HashSet<String>();
        List<Article> articles = new ArrayList<Article>();
        if(items!=null && items.length > 0) {
           for(MultiSearchResponse.Item item: items){
              for(SearchHit hit: item.getResponse().getHits()){
                    articleIds.add(hit.getId());
              }
           }
        }
        else{
            System.out.println("SEARCH is empty");
        }
        for(String id:articleIds){
            articles.add(getArticle(id));
        }

        return articles;
    }

    /**
     * preparing index through program
     * @param indexName
     */
    private static void prepareIndexIfNotDone(String indexName) {
        CreateIndexRequestBuilder createIndexRequestBuilder = client.admin().indices().prepareCreate(indexName);
        createIndexRequestBuilder.execute().actionGet();
    }
}

package com.piyush.elasticsearch.repository;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.FacetedPage;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

import static org.elasticsearch.index.query.FilterBuilders.*;
import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.queryString;
import static org.elasticsearch.index.query.QueryStringQueryBuilder.Operator.OR;

/**
 * Created by jain06 on 12/4/15.
 */
/*@Component("indexer")*/
public class ArticleIndexer {

    @Resource
    private ArticleRepository repository;

    public void indexArticles(List<ArticleType> articles) throws IOException {
        for(ArticleType article: articles) {
            repository.save(article);
        }
    }

    public ArticleType searchById(String articleId){
        return repository.findOne(articleId);
    }

    public List<ArticleType> searchByContent(String keyword){

        BoolQueryBuilder queryBuilder = boolQuery().should(queryString(keyword)
                .field("title")
                .field("content")
                .defaultOperator(OR));

                SearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(queryBuilder)
                .withFilter(matchAllFilter())
                .build();

        FacetedPage<ArticleType> values = repository.search(query);
        List<ArticleType> articles = values.getContent();
        if(articles.size() > 0) {
            return articles;
        }
        else{
            return null;
        }
    }

    public void deleteAll(){
        repository.deleteAll();
    }

}

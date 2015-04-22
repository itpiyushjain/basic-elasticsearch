package com.piyush.elasticsearch.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Created by jain06 on 12/4/15.
 */

public interface ArticleRepository extends ElasticsearchRepository<ArticleType, String> {
}

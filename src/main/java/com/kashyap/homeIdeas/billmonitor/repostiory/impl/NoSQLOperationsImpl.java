package com.kashyap.homeIdeas.billmonitor.repostiory.impl;

import com.kashyap.homeIdeas.billmonitor.repostiory.NoSQLOperations;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This is the nosql db repository for nosql related common operations.
 * @author Kashyap Kadiya
 * @since 2021-06
 */
@Repository
public class NoSQLOperationsImpl implements NoSQLOperations {

    private static final Logger log = LoggerFactory.getLogger(NoSQLOperationsImpl.class);

    @Autowired
    private RestHighLevelClient client;

    @Override
    public void partialUpdate(String index, String id, String field, String value) throws IOException {
        final UpdateRequest updateRequest = new UpdateRequest(index, id);
        updateRequest.doc(field, value);
        client.update(updateRequest, RequestOptions.DEFAULT);
    }

    @Override
    public boolean removeById(final String id) throws IOException {
        final DeleteRequest request = new DeleteRequest("user", id);
        final DeleteResponse response = client.delete(request, RequestOptions.DEFAULT);

        return response.status().getStatus() > 0;
    }

/*
    @Override
    public boolean removeByUsername(final String username) throws IOException {
        final DeleteByQueryRequest request = new DeleteByQueryRequest("user");
        request.setQuery(new TermQueryBuilder("username", username));

            final BulkByScrollResponse response = client.deleteByQuery(request, RequestOptions.DEFAULT);
        return response.getDeleted() > 0;
    }
*/

    @Override
    public boolean bulkInsert(String indexName, List<String> jsonValueList) throws IOException {

        final BulkRequest bulkRequest = new BulkRequest();

        jsonValueList.forEach(valueInJson -> {
            final IndexRequest request = new IndexRequest(indexName);
            request.source(valueInJson, XContentType.JSON);
            bulkRequest.add(request);
        });

        final BulkResponse bulkResponse = client.bulk(bulkRequest, RequestOptions.DEFAULT);

        return StringUtils.isBlank(bulkResponse.buildFailureMessage());
    }

    @Override
    public List<String> getOnlyIds(String indexName, String field, String value) throws IOException {
        final List<String> idList = new ArrayList<>();

        final SearchRequest searchRequest = new SearchRequest();
        searchRequest.source(new SearchSourceBuilder().query(QueryBuilders.termQuery(field, value)));

        final SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        if (searchResponse != null) {
            final SearchHit[] searchHits = searchResponse.getHits().getHits();
            if (searchHits != null) {
                for (SearchHit searchHit : searchHits) {
                    idList.add(searchHit.getId());
                }
            }
        }
        return idList;
    }

    @Override
    public void bulkUpdate(String indexName, List<String> esIdList, String field, String value) throws IOException {
        final BulkRequest bulkRequest = new BulkRequest();

        esIdList.forEach(esId -> {
            final UpdateRequest updateRequest = new UpdateRequest(indexName, esId);
            updateRequest.doc(field, value);
        });

        client.bulk(bulkRequest, RequestOptions.DEFAULT);
    }

    @Override
    public SearchResponse getSearchResponse(SearchRequest searchRequest) throws IOException {
        return client.search(searchRequest, RequestOptions.DEFAULT);
    }
}

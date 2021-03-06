package com.kashyap.homeIdeas.billmonitor.repostiory.impl;

import com.kashyap.homeIdeas.billmonitor.repostiory.NoSQLOperations;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;

@Repository
public class NoSQLOperationsImpl implements NoSQLOperations {

    private static final Logger log = LoggerFactory.getLogger(NoSQLOperationsImpl.class);

    @Autowired
    private RestHighLevelClient client;

    @Override
    public boolean partialUpdate(String index, String id, String field, String value) throws IOException {
        final UpdateRequest updateRequest = new UpdateRequest(index, id);
        updateRequest.doc(field, value);
        final UpdateResponse updateResponse = client.update(updateRequest, RequestOptions.DEFAULT);

        return updateResponse.getGetResult().isExists();

    }

    @Override
    public boolean removeById(final String id) throws IOException {
        final DeleteRequest request = new DeleteRequest("user", id);
        final DeleteResponse response = client.delete(request, RequestOptions.DEFAULT);

        return response.status().getStatus() > 0;
    }

    @Override
    public boolean removeByUsername(final String username) throws IOException {
        final DeleteByQueryRequest request = new DeleteByQueryRequest("user");
        request.setQuery(new TermQueryBuilder("username", username));

        final BulkByScrollResponse response = client.deleteByQuery(request, RequestOptions.DEFAULT);
        return response.getDeleted() > 0;
    }

    @Override
    public boolean bulkInsert(String indexName, List<String> valueInJsonList) throws IOException {

        final BulkRequest bulkRequest = new BulkRequest();

        valueInJsonList.forEach(valueInJson -> {
            final IndexRequest request = new IndexRequest(indexName);
            request.source(valueInJson, XContentType.JSON);
            bulkRequest.add(request);
        });

        final BulkResponse bulkResponse = client.bulk(bulkRequest, RequestOptions.DEFAULT);

        return StringUtils.isBlank(bulkResponse.buildFailureMessage());
    }
}

package com.kashyap.homeIdeas.billmonitor.repostiory.impl;

import com.kashyap.homeIdeas.billmonitor.repostiory.ElasticSearchOperation;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;

@Repository
public class ElasticSearchOperationImpl implements ElasticSearchOperation {

    @Autowired
    private RestHighLevelClient client;

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
}

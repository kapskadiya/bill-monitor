package com.kashyap.homeIdeas.billmonitor.repostiory.impl;

import com.kashyap.homeIdeas.billmonitor.repostiory.AttachmentESRepository;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.query.TermsQueryBuilder;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;

@Repository
public class AttachmentESRepositoryImpl implements AttachmentESRepository {

    @Autowired
    private RestHighLevelClient client;

    @Override
    public void removeAttachments(String billId, List<String> attachmentIds) throws IOException {
        final DeleteByQueryRequest request = new DeleteByQueryRequest("attachment");
        final BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        boolQueryBuilder.should(new TermQueryBuilder("billId", billId));
        boolQueryBuilder.should(new TermsQueryBuilder("attachments.id", attachmentIds));

        request.setQuery(boolQueryBuilder);

        final BulkByScrollResponse response = client.deleteByQuery(request, RequestOptions.DEFAULT);

    }
}

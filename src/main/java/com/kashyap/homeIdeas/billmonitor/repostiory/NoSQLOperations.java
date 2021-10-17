package com.kashyap.homeIdeas.billmonitor.repostiory;

import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;

import java.io.IOException;
import java.util.List;

public interface NoSQLOperations {

    void partialUpdate(String index, String id, String field, String value) throws IOException;

    boolean removeById(final String id) throws IOException;

    boolean bulkInsert(String indexName, List<String> valueInJsonList) throws IOException;

    List<String> getOnlyIds(String indexName, String field, String value) throws IOException;

    void bulkUpdate(String indexName, List<String> esIdList, String field, String value) throws IOException;
}

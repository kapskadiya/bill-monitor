package com.kashyap.homeIdeas.billmonitor.repostiory;

import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;

import java.io.IOException;
import java.util.List;

public interface NoSQLOperations {

    boolean partialUpdate(String index, String id, String field, String value) throws IOException;

    boolean removeById(final String id) throws IOException;

    boolean removeByUsername(final String username) throws IOException;

    boolean bulkInsert(String indexName, List<String> valueInJsonList) throws IOException;
}

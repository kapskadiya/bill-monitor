package com.kashyap.homeIdeas.billmonitor.repostiory.impl;

import com.kashyap.homeIdeas.billmonitor.model.User;
import com.kashyap.homeIdeas.billmonitor.repostiory.UserESRepository;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;

@Repository
public class UserESRepositoryImpl extends ElasticSearchOperationImpl implements UserESRepository {

    @Autowired
    private RestHighLevelClient client;

    @Override
    public boolean disableUser(final String username) throws IOException {

        final UpdateRequest updateRequest = new UpdateRequest("user", username);
        updateRequest.doc("enabled", false);

        final UpdateResponse updateResponse = client.update(updateRequest, RequestOptions.DEFAULT);

        return updateResponse.getGetResult().isExists();
    }

    @Override
    public boolean enableUser(final String username) throws IOException {
        final UpdateRequest updateRequest = new UpdateRequest("user", username);
        updateRequest.doc("enabled", true);

        final UpdateResponse updateResponse = client.update(updateRequest, RequestOptions.DEFAULT);

        return updateResponse.getGetResult().isExists();
    }

    @Override
    public void upsert(User user) throws IOException {
//        final UpdateRequest updateRequest = new UpdateRequest()
//                .index("user")
//                .docAsUpsert(true);
//
//        updateRequest.upsert(user, RequestOptions.DEFAULT);
    }
}

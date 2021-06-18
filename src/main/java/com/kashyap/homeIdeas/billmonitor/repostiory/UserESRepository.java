package com.kashyap.homeIdeas.billmonitor.repostiory;

import com.kashyap.homeIdeas.billmonitor.model.User;

import java.io.IOException;

public interface UserESRepository extends ElasticSearchOperation{

    boolean disableUser(final String username) throws IOException;

    boolean enableUser(final String username) throws IOException;

    void upsert(final User user) throws IOException;
}

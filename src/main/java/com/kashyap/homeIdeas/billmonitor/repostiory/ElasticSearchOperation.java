package com.kashyap.homeIdeas.billmonitor.repostiory;

import java.io.IOException;

public interface ElasticSearchOperation {

    boolean removeById(final String id) throws IOException;

    boolean removeByUsername(final String username) throws IOException;
}

package com.kashyap.homeIdeas.billmonitor.config;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;

/**
 * This is the elasticsearch client configuration which can help to connect application with elasticsearch.
 * @author Kashyap Kadiya
 * @since 2021-06
 */
@Configuration
public class ESClientConfig extends AbstractElasticsearchConfiguration {

    @Value("${elasticsearch.hostAndPort}")
    private String hostAndPort;

    @Value("${elasticsearch.username}")
    private String username;

    @Value("${elasticsearch.password}")
    private String password;

    @Override
    @Bean
    public RestHighLevelClient elasticsearchClient() {

        final ClientConfiguration config = ClientConfiguration
                .builder()
                .connectedTo(hostAndPort)
                .withBasicAuth(username, password)
                .build();

        return RestClients.create(config).rest();
    }
}

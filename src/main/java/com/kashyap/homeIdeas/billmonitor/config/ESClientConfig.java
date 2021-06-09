package com.kashyap.homeIdeas.billmonitor.config;

import org.elasticsearch.client.RestHighLevelClient;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;

@Configuration
public class ESClientConfig extends AbstractElasticsearchConfiguration {
    @NotNull
    @Override
    @Bean
    public RestHighLevelClient elasticsearchClient() {

        final ClientConfiguration config = ClientConfiguration
                .builder()
                .connectedTo("localhost:9200")
                .build();

        return RestClients.create(config).rest();
    }
}

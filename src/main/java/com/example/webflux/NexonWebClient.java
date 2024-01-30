package com.example.webflux;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class NexonWebClient {
    private final String apiServer;
    private final String apiKey;
    private final String keyHeader;

    public NexonWebClient(@Value("${nexon.api.server}") String server,
                     @Value("${nexon.api.key}") String key,
                     @Value("${nexon.api.key_header}") String header) {
        this.apiServer = server;
        this.apiKey = key;
        this.keyHeader = header;
    }

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl(apiServer)
                .defaultHeader(keyHeader, apiKey)
                .build();
    }
}

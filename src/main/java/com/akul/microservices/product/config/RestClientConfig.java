package com.akul.microservices.product.config;

import com.akul.microservices.product.client.FileServiceClient;
import io.micrometer.observation.ObservationRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.time.Duration;

/**
 * RestClientConfig.java.
 *
 * @author Andrii Kulynych
 * @since 1/25/2026
 */
@Configuration
@RequiredArgsConstructor
public class RestClientConfig {

    private final ObservationRegistry observationRegistry;

    @Value("${file-service.url}")
    private String fileServiceUrl;

//    @Bean
//    @LoadBalanced
//    public RestClient.Builder loadBalancedRestClientBuilder() {
//        return RestClient.builder();
//    }

    @Bean
    public RestClient.Builder restClientBuilder() {
        return RestClient.builder();
    }

    @Bean
    public FileServiceClient fileServiceClient(RestClient.Builder builder) {
        RestClient restClient = builder
                .baseUrl(fileServiceUrl)
                .observationRegistry(observationRegistry)
                .requestFactory(getRequestFactory())
                .build();

        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                .builderFor(RestClientAdapter.create(restClient))
                .build();

        return factory.createClient(FileServiceClient.class);
    }

    private SimpleClientHttpRequestFactory getRequestFactory() {
        SimpleClientHttpRequestFactory factory =
                new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout((int) Duration.ofSeconds(3).toMillis());
        factory.setReadTimeout((int) Duration.ofSeconds(3).toMillis());
        return factory;
    }
}

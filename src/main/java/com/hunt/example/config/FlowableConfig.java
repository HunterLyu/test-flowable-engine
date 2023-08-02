package com.hunt.example.config;

import org.flowable.engine.cfg.HttpClientConfig;
import org.flowable.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlowableConfig {


    @Bean
    public HttpClientConfig httpClientConfig() {

        HttpClientConfig httpClientConfig = new HttpClientConfig();
        httpClientConfig.setConnectTimeout(5000);
        httpClientConfig.setSocketTimeout(5000);
        httpClientConfig.setConnectionRequestTimeout(5000);
        httpClientConfig.setRequestRetryLimit(0);

        return httpClientConfig;
    }

    @Bean
    public StandaloneProcessEngineConfiguration processEngineConfiguration(HttpClientConfig config) {
        StandaloneProcessEngineConfiguration pec = new StandaloneProcessEngineConfiguration();
        pec.setHttpClientConfig(config);
        return pec;
    }
}
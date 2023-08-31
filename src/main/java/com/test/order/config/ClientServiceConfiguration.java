package com.test.order.config;

import feign.Logger;
import org.springframework.context.annotation.Bean;

public class ClientServiceConfiguration {

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }
}

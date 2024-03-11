package com.longsan;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnMissingBean(HelloService.class)
@EnableConfigurationProperties(MyProperties.class)
public class ServiceAutoConfiguration {

    @Bean
    public HelloService helloService() {
        return new HelloService();
    }
}

package com.vencent.transcloudgatewayzuul;

import com.vencent.transcloudgatewayzuul.filter.PreRequestAuthFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;


@EnableZuulProxy
@EnableDiscoveryClient
@EnableRedisHttpSession
@SpringBootApplication
public class TranscloudGatewayZuulApplication {

    public static void main(String[] args) {
        SpringApplication.run(TranscloudGatewayZuulApplication.class, args);
    }

    @Bean
    public PreRequestAuthFilter preRequestAuthFilter(){
        return new PreRequestAuthFilter();
    }
}

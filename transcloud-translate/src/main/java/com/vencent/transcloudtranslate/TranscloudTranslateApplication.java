package com.vencent.transcloudtranslate;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients
@EnableTransactionManagement
@EnableRedisHttpSession
@MapperScan("com.vencent.transcloudtranslate.mapper")
public class TranscloudTranslateApplication {

    public static void main(String[] args) {
        SpringApplication.run(TranscloudTranslateApplication.class, args);
    }

}

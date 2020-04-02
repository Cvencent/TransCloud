package com.vencent.transclouddata;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableFeignClients
@SpringBootApplication
@EnableDiscoveryClient
@EnableTransactionManagement
@EnableRedisHttpSession
@MapperScan("com.vencent.transclouddata.mapper")
public class TranscloudDataApplication {

    public static void main(String[] args) {
        SpringApplication.run(TranscloudDataApplication.class, args);
    }

}

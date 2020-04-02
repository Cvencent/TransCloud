package com.vencent.transclouduser;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
@EnableTransactionManagement
@EnableRedisHttpSession
@MapperScan("com.vencent.transclouduser.mapper")
public class TranscloudUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(TranscloudUserApplication.class, args);
    }

}

package com.vencent.transcloudeureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;


@EnableEurekaServer
@SpringBootApplication
public class    TranscloudEurekaApplication {

    public static void main(String[] args) {
        SpringApplication.run(TranscloudEurekaApplication.class, args);
    }

}

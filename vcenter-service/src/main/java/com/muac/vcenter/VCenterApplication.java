package com.muac.vcenter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class VCenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(VCenterApplication.class, args);
    }
}

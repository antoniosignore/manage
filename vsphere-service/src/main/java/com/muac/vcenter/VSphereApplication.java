package com.muac.vcenter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class VSphereApplication {

	public static void main(String[] args) {
		SpringApplication.run(VSphereApplication.class, args);
	}
}

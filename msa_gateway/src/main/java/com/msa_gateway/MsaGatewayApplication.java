package com.msa_gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableEurekaServer
public class MsaGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsaGatewayApplication.class, args);
    }

}

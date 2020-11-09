package com.lens.epay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan("com.lens.epay.configuration")
public class EpayApplication {

    public static void main(String[] args) {
        SpringApplication.run(EpayApplication.class, args);
    }

}

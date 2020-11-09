package com.lens.epay.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Created by Emir Gökdemir
 * on 10 Kas 2020
 */

@Configuration
public class ProfileConfig {

    @Profile("dev")
    @Bean
    public void devDB() {
        System.out.println("Dev Profile is used");
    }

    @Profile("test")
    @Bean
    public void testDB() {
        System.out.println("Test Profile is used");
    }

    @Profile("prod")
    @Bean
    public void prodDB() {
        System.out.println("Prod Profile is used");
    }
}

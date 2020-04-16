package com.lens.epay.configuration;

/**
 * Created by Emir Gökdemir
 * on 17 Nis 2020
 */

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors().and()
                .csrf().disable().authorizeRequests()
                .antMatchers("/users").hasRole("manager")
                .anyRequest().permitAll()
                .and()
                .formLogin();
    }
}

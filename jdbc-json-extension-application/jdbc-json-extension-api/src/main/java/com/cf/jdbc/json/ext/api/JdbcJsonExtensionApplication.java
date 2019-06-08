package com.cf.jdbc.json.ext.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.cf.jdbc.json.ext"})
public class JdbcJsonExtensionApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(JdbcJsonExtensionApplication.class, args);
    }
    
}

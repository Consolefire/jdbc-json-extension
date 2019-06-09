package com.cf.jdbc.json.ext.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@SpringBootApplication(scanBasePackages = {"com.cf.jdbc.json.ext"})
public class JdbcJsonExtensionWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(JdbcJsonExtensionWebApplication.class, args);
    }

}

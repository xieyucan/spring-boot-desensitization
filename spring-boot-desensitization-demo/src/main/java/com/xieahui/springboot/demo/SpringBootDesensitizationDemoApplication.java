package com.xieahui.springboot.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class SpringBootDesensitizationDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootDesensitizationDemoApplication.class, args);
        log.info("Hello, World! phone=18511111111");
    }
}

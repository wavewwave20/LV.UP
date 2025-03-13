package com.alltogether.lvupbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class LvUpBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(LvUpBackendApplication.class, args);
    }

}

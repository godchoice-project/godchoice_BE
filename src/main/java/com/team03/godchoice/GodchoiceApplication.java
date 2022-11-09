package com.team03.godchoice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class GodchoiceApplication {

    /*public static void main(String[] args) {
        new SpringApplicationBuilder(StompApplication.class)
                .properties(APPLICATION_LOCATIONS)
                .run(args);
    }*/

    public static void main(String[] args) {
        SpringApplication.run(GodchoiceApplication.class, args);
    }

}

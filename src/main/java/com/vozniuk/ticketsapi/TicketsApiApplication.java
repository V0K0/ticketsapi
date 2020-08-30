package com.vozniuk.ticketsapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("classpath:application.properties")
public class TicketsApiApplication {

    public static void main(String[] args) {
       SpringApplication.run(TicketsApiApplication.class, args);
    }

}

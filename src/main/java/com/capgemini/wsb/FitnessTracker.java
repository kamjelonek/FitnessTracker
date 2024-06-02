package com.capgemini.wsb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
public class FitnessTracker {

    public static void main(String[] args) {
        //System.out.println("FitnessTracker main...");
        SpringApplication.run(FitnessTracker.class, args);
    }

}

package com.capgemini.wsb.fitnesstracker.config;

import com.capgemini.wsb.fitnesstracker.aop.logging.LoggingAspect;
import org.springframework.context.annotation.*;

@Configuration
@EnableAspectJAutoProxy
public class LoggingAspectConfiguration {

    @Bean
    public LoggingAspect loggingAspect() {

        //System.out.println("LoggingAspect()...");

        return new LoggingAspect();
    }
}

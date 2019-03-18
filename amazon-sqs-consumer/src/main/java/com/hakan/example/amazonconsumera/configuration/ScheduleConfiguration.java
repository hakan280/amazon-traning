package com.hakan.example.amazonconsumera.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@EnableRetry
public class ScheduleConfiguration {
}

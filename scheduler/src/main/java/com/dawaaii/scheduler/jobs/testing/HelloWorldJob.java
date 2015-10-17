package com.dawaaii.scheduler.jobs.testing;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@EnableScheduling
public class HelloWorldJob {

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

	@Scheduled(fixedRate = 5000)
    public void reportHelloWorldCurrentTime() {
        System.out.println("Hello World!, The time is now " + dateFormat.format(new Date()));
    }
}

package com.gpr.apps.pojo;

import java.util.List;
import java.util.Map;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.jdbc.core.JdbcTemplate;

import io.micrometer.observation.transport.Propagator.Getter;

@SpringBootApplication
public class Education {
	static int count;
	static String edu;

    public static void main(String[] args) {
    
	
        SpringApplication.run(Education.class, args);

    }
}






package com.neuedu;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.neuedu.dao")
@EnableScheduling
public class ForProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(ForProjectApplication.class, args);
	}

}

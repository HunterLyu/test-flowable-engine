package com.hunt.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;
//import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(proxyBeanMethods = false)
@EnableTransactionManagement
//@EnableJpaAuditing
public class MsgFlowableApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsgFlowableApplication.class, args);
	}
}

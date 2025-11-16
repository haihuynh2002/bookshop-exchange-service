package com.bookshop.exchange_service;

import org.springframework.boot.SpringApplication;

public class TestExchangeServiceApplication {

	public static void main(String[] args) {
		SpringApplication.from(ExchangeServiceApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}

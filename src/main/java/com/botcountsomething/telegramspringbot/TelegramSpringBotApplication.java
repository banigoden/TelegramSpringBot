package com.botcountsomething.telegramspringbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class TelegramSpringBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(TelegramSpringBotApplication.class, args);
    }

}

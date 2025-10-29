package io.synergy.minichat;

import io.synergy.minichat.service.ContactServiceImpl;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MiniChatApplication {

    public static void main(String[] args) {
        SpringApplication.run(MiniChatApplication.class, args);
    }

    @Bean
    CommandLineRunner initData(ContactServiceImpl contactService) {
        return args -> contactService.initFromFile();
    }
}

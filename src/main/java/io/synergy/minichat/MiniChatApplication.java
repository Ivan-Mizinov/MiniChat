package io.synergy.minichat;

import io.synergy.minichat.service.ContactServiceImpl;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
@EnableCaching
public class MiniChatApplication {

    public static void main(String[] args) {
        SpringApplication.run(MiniChatApplication.class, args);
    }

    @Bean
    @Profile("!test")
    CommandLineRunner initData(ContactServiceImpl contactService) {
        return args -> contactService.initFromFile();
    }
}

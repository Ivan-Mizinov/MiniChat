package io.synergy.minichat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableCaching
@EnableJpaRepositories
@EnableTransactionManagement
public class MiniChatApplication {

    public static void main(String[] args) {
        SpringApplication.run(MiniChatApplication.class, args);
    }
}

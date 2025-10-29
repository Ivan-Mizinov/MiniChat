package io.synergy.minichat;

import io.synergy.minichat.model.Contact;
import io.synergy.minichat.service.ContactService;
import io.synergy.minichat.service.ContactServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.List;

@SpringBootApplication
public class MiniChatApplication {

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(MiniChatApplication.class, args);
        ContactService contactService = ctx.getBean(ContactServiceImpl.class);
        List<Contact> contacts = contactService.findAll();
        for (Contact contact : contacts) {
            System.out.println(contact);
        }
    }
}

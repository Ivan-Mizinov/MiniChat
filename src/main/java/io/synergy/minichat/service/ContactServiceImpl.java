package io.synergy.minichat.service;

import io.synergy.minichat.model.Contact;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class ContactServiceImpl implements ContactService {

    @Value("${contacts.file.path}")
    private String filePath;

    @Override
    public List<Contact> findAll() {
        List<Contact> contacts = new ArrayList<>();

        try {
            ClassPathResource resource = new ClassPathResource(filePath);
            try (BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
                String line;
                boolean isFirst = true;

                while ((line = br.readLine()) != null) {
                    if (isFirst) {
                        isFirst = false;
                        continue;
                    }

                    String[] parts = line.split(",");
                    if (parts.length != 4) {
                        System.err.println("Ошибка формата строки: " + line);
                        continue;
                    }

                    Contact contact = new Contact();
                    contact.setFirstName(parts[0]);
                    contact.setLastName(parts[1]);
                    contact.setMiddleName(parts[2]);
                    contact.setPhone(parts[3]);
                    contacts.add(contact);
                }
            }
        } catch (IOException e) {
            System.out.println("Ошибка чтения файла: " + e.getMessage());
        }

        return contacts;
    }

}

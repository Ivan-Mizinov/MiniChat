package io.synergy.minichat.service;

import io.synergy.minichat.dto.Contact;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ContactServiceImpl implements ContactService {

    @Value("${contacts.file.path}")
    private String filePath;

    private final Map<Long, Contact> contactMap = new ConcurrentHashMap<>();
    private Long currentId = 1L;


    @Override
    @Cacheable(value = "allContacts")
    public List<Contact> findAll() {
        return new ArrayList<>(contactMap.values());
    }

    @Override
    public Contact save(Contact contact) {
        if (contact.getId() == null) {
            contact.setId(currentId++);
        }
        contactMap.put(contact.getId(), contact);
        return contact;
    }

    @Override
    @CachePut(value = "contacts", key = "#contact.id")
    public Contact update(Contact contact) {
        Long id = contact.getId();
        if (!contactMap.containsKey(id)) {
            throw new NoSuchElementException("Контакт с ID " + id + " не найден");
        }
        contact.setId(id);
        contactMap.put(id, contact);
        return contact;
    }

    @Override
    @Cacheable(value = "contacts", key = "#id")
    public Contact findById(Long id) throws Exception {
        if (Objects.isNull(id)) throw new Exception("id can not be null");
        return contactMap.get(id);
    }

    @Override
    @CacheEvict(value = "contacts", key = "#id")
    public void deleteById(Long id) {
        contactMap.remove(id);
    }

    public void initFromFile() {
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
                    save(contact);
                }
            }
        } catch (IOException e) {
            System.out.println("Ошибка чтения файла: " + e.getMessage());
        }
    }

}

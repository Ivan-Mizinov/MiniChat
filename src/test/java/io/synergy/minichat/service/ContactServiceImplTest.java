package io.synergy.minichat.service;

import io.synergy.minichat.dto.Contact;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class ContactServiceImplTest {


    @Autowired
    private ContactService contactService;

    @Test
    void testFindAll() {
        List<Contact> contacts = contactService.findAll();

        assertNotNull(contacts);
        assertFalse(contacts.isEmpty());

        for (Contact contact : contacts) {
            assertNotNull(contact.getFirstName());
            assertNotNull(contact.getLastName());
            assertNotNull(contact.getMiddleName());
            assertNotNull(contact.getPhone());
        }
    }

}
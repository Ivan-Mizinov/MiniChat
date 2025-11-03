package io.synergy.minichat.service;

import io.synergy.minichat.dto.Contact;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class ContactServiceImplTest {

    @Autowired
    private ContactService contactService;

    private Contact testContact;

    @BeforeEach
    void setUp() {
        testContact = new Contact();
        testContact.setFirstName("Иван");
        testContact.setLastName("Иванов");
        testContact.setMiddleName("Иванович");
        testContact.setPhone("+79991234567");
    }

    @Test
    void testFindAll_ShouldReturnNonEmptyList() {
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

    @Test
    void testSave_ShouldPersistNewContact() throws Exception {
        Contact savedContact = contactService.save(testContact);

        assertNotNull(savedContact.getId());
        assertEquals(testContact.getFirstName(), savedContact.getFirstName());
        assertEquals(testContact.getLastName(), savedContact.getLastName());
        assertEquals(testContact.getMiddleName(), savedContact.getMiddleName());
        assertEquals(testContact.getPhone(), savedContact.getPhone());

        Contact foundContact = contactService.findById(savedContact.getId());
        assertNotNull(foundContact);
        assertEquals(savedContact.getId(), foundContact.getId());
    }

    @Test
    void testUpdate_ShouldModifyExistingContact() throws Exception {
        Contact savedContact = contactService.save(testContact);

        savedContact.setFirstName("Пётр");
        savedContact.setLastName("Петров");

        Contact updatedContact = contactService.update(savedContact);

        assertEquals("Пётр", updatedContact.getFirstName());
        assertEquals("Петров", updatedContact.getLastName());

        Contact foundContact = contactService.findById(updatedContact.getId());
        assertEquals("Пётр", foundContact.getFirstName());
        assertEquals("Петров", foundContact.getLastName());
    }

    @Test
    void testUpdate_ShouldThrowException_WhenContactNotFound() {
        Contact contact = new Contact();
        contact.setId(999L);

        assertThrows(NoSuchElementException.class, () -> contactService.update(contact));
    }

    @Test
    void testFindById_ShouldReturnContact_WhenExists() throws Exception {
        Contact savedContact = contactService.save(testContact);

        Contact foundContact = contactService.findById(savedContact.getId());

        assertNotNull(foundContact);
        assertEquals(savedContact.getId(), foundContact.getId());
        assertEquals(savedContact.getFirstName(), foundContact.getFirstName());
    }

    @Test
    void testFindById_ShouldThrowException_WhenIdIsNull() {
        assertThrows(Exception.class, () -> contactService.findById(null));
    }

    @Test
    void testFindById_ShouldReturnNull_WhenContactNotExists() throws Exception {
        Contact foundContact = contactService.findById(999L);

        assertNull(foundContact);
    }

    @Test
    void testDeleteById_ShouldRemoveContact() throws Exception {
        Contact savedContact = contactService.save(testContact);

        contactService.deleteById(savedContact.getId());

        Contact foundContact = contactService.findById(savedContact.getId());
        assertNull(foundContact);
    }

    @Test
    void testDeleteById_ShouldNotThrow_WhenIdNotExists() {
        assertDoesNotThrow(() -> contactService.deleteById(999L), "Удаление несуществующего ID не должно выбрасывать исключений");
    }

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
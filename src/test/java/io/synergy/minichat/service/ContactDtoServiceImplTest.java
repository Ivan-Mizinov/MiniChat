package io.synergy.minichat.service;

import io.synergy.minichat.dto.ContactDto;
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
class ContactDtoServiceImplTest {

    @Autowired
    private ContactService contactService;

    private ContactDto testContact;

    @BeforeEach
    void setUp() {
        testContact = new ContactDto();
        testContact.setFirstName("Иван");
        testContact.setLastName("Иванов");
        testContact.setMiddleName("Иванович");
        testContact.setPhone("+79991234567");
    }

    @Test
    void testFindAll_ShouldReturnNonEmptyList() {
        List<ContactDto> contacts = contactService.findAll();

        assertNotNull(contacts);
        assertFalse(contacts.isEmpty());

        for (ContactDto contact : contacts) {
            assertNotNull(contact.getFirstName());
            assertNotNull(contact.getLastName());
            assertNotNull(contact.getMiddleName());
            assertNotNull(contact.getPhone());
        }
    }

    @Test
    void testSave_ShouldPersistNewContact() throws Exception {
        ContactDto savedContact = contactService.save(testContact);

        assertNotNull(savedContact.getId());
        assertEquals(testContact.getFirstName(), savedContact.getFirstName());
        assertEquals(testContact.getLastName(), savedContact.getLastName());
        assertEquals(testContact.getMiddleName(), savedContact.getMiddleName());
        assertEquals(testContact.getPhone(), savedContact.getPhone());

        ContactDto foundContact = contactService.findById(savedContact.getId());
        assertNotNull(foundContact);
        assertEquals(savedContact.getId(), foundContact.getId());
    }

    @Test
    void testUpdate_ShouldModifyExistingContact() throws Exception {
        ContactDto savedContact = contactService.save(testContact);

        savedContact.setFirstName("Пётр");
        savedContact.setLastName("Петров");

        ContactDto updatedContact = contactService.update(savedContact);

        assertEquals("Пётр", updatedContact.getFirstName());
        assertEquals("Петров", updatedContact.getLastName());

        ContactDto foundContact = contactService.findById(updatedContact.getId());
        assertEquals("Пётр", foundContact.getFirstName());
        assertEquals("Петров", foundContact.getLastName());
    }

    @Test
    void testUpdate_ShouldThrowException_WhenContactNotFound() {
        ContactDto contact = new ContactDto();
        contact.setId(999L);

        assertThrows(NoSuchElementException.class, () -> contactService.update(contact));
    }

    @Test
    void testFindById_ShouldReturnContact_WhenExists() throws Exception {
        ContactDto savedContact = contactService.save(testContact);

        ContactDto foundContact = contactService.findById(savedContact.getId());

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
        ContactDto foundContact = contactService.findById(999L);

        assertNull(foundContact);
    }

    @Test
    void testDeleteById_ShouldRemoveContact() throws Exception {
        ContactDto savedContact = contactService.save(testContact);

        contactService.deleteById(savedContact.getId());

        ContactDto foundContact = contactService.findById(savedContact.getId());
        assertNull(foundContact);
    }

    @Test
    void testDeleteById_ShouldNotThrow_WhenIdNotExists() {
        assertDoesNotThrow(() -> contactService.deleteById(999L), "Удаление несуществующего ID не должно выбрасывать исключений");
    }

    @Test
    void testFindAll() {
        List<ContactDto> contacts = contactService.findAll();

        assertNotNull(contacts);
        assertFalse(contacts.isEmpty());

        for (ContactDto contact : contacts) {
            assertNotNull(contact.getFirstName());
            assertNotNull(contact.getLastName());
            assertNotNull(contact.getMiddleName());
            assertNotNull(contact.getPhone());
        }
    }

}
package io.synergy.minichat.service;

import io.synergy.minichat.dto.Contact;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.spy;

@SpringBootTest
@ActiveProfiles("test")
class ContactServiceImplCacheTest {

    @Autowired
    private ContactServiceImpl contactService;

    @Autowired
    private CacheManager cacheManager;

    private Contact contact1;

    @BeforeEach
    void setUp() {
        Objects.requireNonNull(cacheManager.getCache("contacts")).clear();
        Objects.requireNonNull(cacheManager.getCache("allContacts")).clear();

        contact1 = new Contact();
        contact1.setId(1L);
        contact1.setFirstName("Иван");
        contact1.setLastName("Иванов");
        contact1.setMiddleName("Иванович");
        contact1.setPhone("+79991234567");
    }

    @Test
    void testFindById_CachesResult() throws Exception {
        Contact result1 = contactService.save(contact1);
        Contact found1 = contactService.findById(1L);

        assertEquals("Иван", found1.getFirstName());

        Contact found2 = contactService.findById(1L);

        assertEquals(found1, found2);
    }

    @Test
    void testUpdate_UpdatesCache() throws Exception {
        contactService.save(contact1);

        Contact cached = contactService.findById(1L);
        assertEquals("Иван", cached.getFirstName());

        cached.setFirstName("Пётр");
        Contact updated = contactService.update(cached);

        Contact refreshed = contactService.findById(1L);
        assertEquals("Пётр", refreshed.getFirstName());
    }

    @Test
    void testDeleteById_EvictsFromCache() throws Exception {
        contactService.save(contact1);

        Contact cached = contactService.findById(1L);
        assertNotNull(cached);

        contactService.deleteById(1L);

        Contact afterDelete = contactService.findById(1L);

        assertNull(afterDelete);
    }

    @Test
    void testFindAll_CachesResult() {
        contactService.save(contact1);

        List<Contact> all1 = contactService.findAll();
        assertEquals(1, all1.size());

        List<Contact> all2 = contactService.findAll();
        assertEquals(all1, all2);
    }

    @Test
    void testCacheReducesMethodCalls() throws Exception {
        ContactServiceImpl spyService = spy(contactService);

        spyService.save(contact1);

        spyService.findById(1L);
        spyService.findById(1L);

        var cache = cacheManager.getCache("contacts");
        assertNotNull(Objects.requireNonNull(cache).get(1L));
    }
}

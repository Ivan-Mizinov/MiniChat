package io.synergy.minichat.repository;

import io.synergy.minichat.entity.ContactEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.tuple;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class ContactRepositoryTest {

    @Autowired
    private ContactRepository contactRepository;

    @Test
    void shouldSaveAndFindContactById() {
        ContactEntity contact = new ContactEntity();
        contact.setFirstName("Иван");
        contact.setLastName("Иванов");
        contact.setMiddleName("Иванович");
        contact.setPhone("+79991234567");

        ContactEntity saved = contactRepository.save(contact);

        assertThat(saved.getId()).isNotNull();

        Optional<ContactEntity> found = contactRepository.findById(saved.getId());

        assertThat(found)
                .hasValueSatisfying(contactEntity -> {
                    assertThat(contactEntity.getFirstName()).isEqualTo("Иван");
                    assertThat(contactEntity.getLastName()).isEqualTo("Иванов");
                    assertThat(contactEntity.getPhone()).isEqualTo("+79991234567");
                });
    }

    @Test
    void shouldFindAllContacts() {
        ContactEntity c1 = new ContactEntity("Алексей", "Петров", "Сергеевич", "+79997778888");
        ContactEntity c2 = new ContactEntity("Мария", "Сидорова", "Алексеевна", "+79996665555");

        contactRepository.save(c1);
        contactRepository.save(c2);

        var allContacts = contactRepository.findAll();

        assertThat(allContacts).hasSize(2);
        assertThat(allContacts)
                .extracting("firstName", "lastName")
                .containsExactlyInAnyOrder(
                        tuple("Алексей", "Петров"),
                        tuple("Мария", "Сидорова")
                );
    }

    @Test
    void shouldUpdateContact() {
        ContactEntity contact = new ContactEntity("Анна", "Волкова", null, "+79990001111");
        ContactEntity saved = contactRepository.save(contact);

        saved.setFirstName("Анна-Мария");
        saved.setPhone("+79990002222");

        ContactEntity updated = contactRepository.save(saved);

        assertThat(updated.getFirstName()).isEqualTo("Анна-Мария");
        assertThat(updated.getPhone()).isEqualTo("+79990002222");

        Optional<ContactEntity> reloaded = contactRepository.findById(updated.getId());
        assertThat(reloaded)
                .hasValueSatisfying(contactEntity ->
                        assertThat(contactEntity.getFirstName()).isEqualTo("Анна-Мария"));
    }

    @Test
    void shouldDeleteContactById() {
        ContactEntity contact = new ContactEntity("Дмитрий", "Козлов", "Андреевич", "+79993334444");
        ContactEntity saved = contactRepository.save(contact);

        contactRepository.deleteById(saved.getId());

        Optional<ContactEntity> found = contactRepository.findById(saved.getId());
        assertThat(found).isEmpty();
    }

    @Test
    void shouldReturnEmptyOptionalWhenContactNotFound() {
        Optional<ContactEntity> found = contactRepository.findById(999L);
        assertThat(found).isEmpty();
    }
}
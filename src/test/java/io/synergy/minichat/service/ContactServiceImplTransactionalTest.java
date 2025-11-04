package io.synergy.minichat.service;

import io.synergy.minichat.dto.ContactDto;
import io.synergy.minichat.entity.ContactEntity;
import io.synergy.minichat.exception.NotFoundException;
import io.synergy.minichat.repository.ContactRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ContactServiceImplTransactionalTest {

    @InjectMocks
    private ContactServiceImpl contactService;
    @Mock
    private ContactRepository contactRepository;
    private ContactDto dto;
    private ContactEntity entity;

    @BeforeEach
    void setUp() {
        entity = new ContactEntity(
                1L,
                "Doe",
                "John",
                "Middle",
                "+71234567890"
        );
        dto = new ContactDto(
                1L,
                "Doe",
                "John",
                "Middle",
                "+71234567890"
        );
    }

    @Test
    @DisplayName("findAll должен вызывать репозиторий и возвращать список DTO")
    void findAll() {
        when(contactRepository.findAll()).thenReturn(List.of(entity));

        List<ContactDto> result = contactService.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0))
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("lastName", "Doe")
                .hasFieldOrPropertyWithValue("firstName", "John")
                .hasFieldOrPropertyWithValue("middleName", "Middle")
                .hasFieldOrPropertyWithValue("phone", "+71234567890");

        verify(contactRepository).findAll();
    }

    @Test
    @DisplayName("save должен сохранять сущность и возвращать DTO с ID")
    void save() {
        when(contactRepository.save(any(ContactEntity.class))).thenReturn(entity);

        ContactDto result = contactService.save(dto);

        assertThat(result)
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("lastName", "Doe")
                .hasFieldOrPropertyWithValue("firstName", "John")
                .hasFieldOrPropertyWithValue("middleName", "Middle")
                .hasFieldOrPropertyWithValue("phone", "+71234567890");

        verify(contactRepository).save(any(ContactEntity.class));
    }

    @Test
    @DisplayName("update должен обновить существующую сущность")
    void update() throws Exception {
        when(contactRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(contactRepository.save(any(ContactEntity.class))).thenReturn(entity);

        ContactDto result = contactService.update(dto);

        assertThat(result)
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("lastName", "Doe")
                .hasFieldOrPropertyWithValue("firstName", "John")
                .hasFieldOrPropertyWithValue("middleName", "Middle")
                .hasFieldOrPropertyWithValue("phone", "+71234567890");

        verify(contactRepository).findById(1L);
        verify(contactRepository).save(any(ContactEntity.class));
    }

    @Test
    @DisplayName("findById должен вернуть DTO по ID или выбросить исключение")
    void findById() throws Exception {
        when(contactRepository.findById(1L)).thenReturn(Optional.of(entity));

        ContactDto result = contactService.findById(1L);

        assertThat(result)
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("lastName", "Doe")
                .hasFieldOrPropertyWithValue("firstName", "John");

        verify(contactRepository).findById(1L);

        when(contactRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> contactService.findById(999L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("entity not found");
    }

    @Test
    @DisplayName("deleteById должен удалить сущность по ID")
    void deleteById() {
        when(contactRepository.existsById(1L)).thenReturn(true);

        contactService.deleteById(1L);

        verify(contactRepository).existsById(1L);
        verify(contactRepository).deleteById(1L);

        when(contactRepository.existsById(999L)).thenReturn(false);

        assertThatThrownBy(() -> contactService.deleteById(999L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("entity not found");
    }
}
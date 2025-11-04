package io.synergy.minichat.service;

import io.synergy.minichat.dto.ContactDto;
import io.synergy.minichat.entity.ContactEntity;
import io.synergy.minichat.exception.NotFoundException;
import io.synergy.minichat.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ContactServiceImpl implements ContactService {

    @Autowired
    private final ContactRepository contactRepository;

    public ContactServiceImpl(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    @Transactional(
            isolation = Isolation.READ_COMMITTED,
            propagation = Propagation.SUPPORTS,
            readOnly = true
    )
    @Override
    public List<ContactDto> findAll() {
        List<ContactEntity> list = contactRepository.findAll();
        return list.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(
            isolation = Isolation.REPEATABLE_READ,
            propagation = Propagation.REQUIRED
    )
    @Override
    public ContactDto save(ContactDto contact) {
        ContactEntity entity = mapToEntity(contact);
        entity = contactRepository.save(entity);
        return this.mapToDto(entity);
    }

    @Transactional(
            isolation = Isolation.REPEATABLE_READ,
            propagation = Propagation.REQUIRED
    )
    @Override
    @CachePut(value = "contacts", key = "#contact.id")
    public ContactDto update(ContactDto contact) {
        Optional<ContactEntity> originalEntity = contactRepository.findById(contact.getId());
        if (originalEntity.isPresent()) {
            ContactEntity entity = this.mapToEntity(contact);
            entity = contactRepository.save(entity);
            return this.mapToDto(entity);
        } else {
            throw new NotFoundException("entity not found");
        }
    }

    @Transactional(
            isolation = Isolation.READ_COMMITTED,
            propagation = Propagation.REQUIRED,
            timeout = 10000,
            readOnly = true
    )
    @Override
    @Cacheable(value = "contacts", key = "#id")
    public ContactDto findById(Long id) throws Exception {
        if (Objects.isNull(id)) throw new Exception("id can not be null");
        return contactRepository.findById(id)
                .map(this::mapToDto)
                .orElseThrow(() -> new NotFoundException("entity not found"));
    }

    @Transactional(
            isolation = Isolation.SERIALIZABLE,
            propagation = Propagation.REQUIRED
    )
    @Override
    @CacheEvict(value = "contacts", key = "#id")
    public void deleteById(Long id) {
        if (!contactRepository.existsById(id)) throw new NotFoundException("entity not found");
        contactRepository.deleteById(id);
    }

    public ContactDto mapToDto(ContactEntity entity) {
        return new ContactDto(entity.getId(),
                entity.getLastName(),
                entity.getFirstName(),
                entity.getMiddleName(),
                entity.getPhone());
    }

    public ContactEntity mapToEntity(ContactDto dto) {
        return new ContactEntity(dto.getId(),
                dto.getFirstName(),
                dto.getMiddleName(),
                dto.getLastName(),
                dto.getPhone());
    }

}

package io.synergy.minichat.service;

import io.synergy.minichat.dto.ContactDto;

import java.util.List;

public interface ContactService {
    List<ContactDto> findAll();

    ContactDto save(ContactDto contact);

    ContactDto update(ContactDto contact);

    ContactDto findById(Long id) throws Exception;

    void deleteById(Long id);
}

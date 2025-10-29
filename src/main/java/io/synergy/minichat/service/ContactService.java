package io.synergy.minichat.service;

import io.synergy.minichat.model.Contact;

import java.util.List;

public interface ContactService {
    List<Contact> findAll();

    Contact save(Contact contact);

    Contact update(Contact contact);

    Contact findById(Long id);

    void deleteById(Long id);
}

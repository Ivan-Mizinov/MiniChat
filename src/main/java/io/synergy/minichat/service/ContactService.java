package io.synergy.minichat.service;

import io.synergy.minichat.model.Contact;

import java.util.List;

public interface ContactService {
    List<Contact> findAll();
}

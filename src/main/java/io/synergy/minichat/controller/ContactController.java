package io.synergy.minichat.controller;

import io.synergy.minichat.model.Contact;
import io.synergy.minichat.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ContactController {

    private final ContactService contactService;

    @Autowired
    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @GetMapping("/api/service/Contacts")
    public List<Contact> getAllContacts() {
        return contactService.findAll();
    }

    @GetMapping("/api/service/Contact/{id}")
    public Contact getContactById(@PathVariable Long id) throws Exception {
        return contactService.findById(id);
    }

    @PostMapping("/api/service/Contact")
    public Contact addContact(@RequestBody Contact contact) {
        return contactService.save(contact);
    }

    @PutMapping("/api/service/Contact")
    public Contact updateContact(@RequestBody Contact contact) {
        return contactService.update(contact);
    }

    @DeleteMapping("/api/service/Contact/{id}")
    public void deleteContact(@PathVariable Long id) {
        contactService.deleteById(id);
    }

}

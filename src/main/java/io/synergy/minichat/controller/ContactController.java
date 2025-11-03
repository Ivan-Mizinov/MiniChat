package io.synergy.minichat.controller;

import io.synergy.minichat.dto.ContactDto;
import io.synergy.minichat.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public List<ContactDto> getAllContacts() {
        return contactService.findAll();
    }

    @GetMapping("/api/service/Contact/{id}")
    public ResponseEntity<ContactDto> getContactById(@PathVariable Long id) throws Exception {
        ContactDto contact = contactService.findById(id);
        if (contact == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(contact);
    }

    @PostMapping("/api/service/Contact")
    public ContactDto addContact(@RequestBody ContactDto contact) {
        return contactService.save(contact);
    }

    @PutMapping("/api/service/Contact")
    public ContactDto updateContact(@RequestBody ContactDto contact) {
        return contactService.update(contact);
    }

    @DeleteMapping("/api/service/Contact/{id}")
    public void deleteContact(@PathVariable Long id) {
        contactService.deleteById(id);
    }

}

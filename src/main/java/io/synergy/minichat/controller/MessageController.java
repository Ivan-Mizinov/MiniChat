package io.synergy.minichat.controller;

import io.synergy.minichat.dto.MessageDto;
import io.synergy.minichat.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MessageController {

    @Autowired
    private MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/api/service/Messages")
    public List<MessageDto> findAll() {
        return messageService.findAll();
    }

    @GetMapping("/api/service/Message/{id}")
    public MessageDto findById(@PathVariable Long id) throws Exception {
        return messageService.findById(id);
    }

    @PostMapping("/api/service/Message")
    public MessageDto create(@RequestBody MessageDto messageDto) {
        return messageService.create(messageDto);
    }

    @PutMapping("/api/service/Message")
    public MessageDto update(@RequestBody MessageDto messageDto) throws Exception {
        return messageService.update(messageDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/api/service/Message/{id}")
    public void delete(@PathVariable Long id) {
        messageService.delete(id);
    }

}

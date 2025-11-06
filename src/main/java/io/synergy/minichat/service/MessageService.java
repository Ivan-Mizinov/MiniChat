package io.synergy.minichat.service;

import io.synergy.minichat.dto.MessageDto;

import java.util.List;

public interface MessageService {
    MessageDto findById(Long id) throws Exception;

    List<MessageDto> findAll();

    MessageDto create(MessageDto messageDto);

    MessageDto update(MessageDto messageDto) throws Exception;

    void delete(Long id);
}

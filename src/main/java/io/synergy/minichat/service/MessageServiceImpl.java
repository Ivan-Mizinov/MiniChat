package io.synergy.minichat.service;

import io.synergy.minichat.dto.MessageDto;
import io.synergy.minichat.entity.MessageEntity;
import io.synergy.minichat.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Primary
public class MessageServiceImpl implements MessageService {
    @Autowired
    private MessageRepository messageRepository;

    @Transactional(
            isolation = Isolation.READ_COMMITTED,
            propagation = Propagation.REQUIRED,
            timeout = 10000,
            readOnly = true
    )
    @Override
    public MessageDto findById(Long id) throws Exception {
        if (Objects.isNull(id)) throw new Exception("id can not be null");
        return messageRepository.findById(id)
                .map(this::mapToDto)
                .orElse(null);
    }

    @Override
    public List<MessageDto> findAll() {
        List<MessageEntity> entities = messageRepository.findAll();
        return entities.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public MessageDto create(MessageDto dto) {
        MessageEntity entity = this.mapToEntity(dto);
        entity = this.messageRepository.save(entity);
        return this.mapToDto(entity);
    }

    @Transactional(
            isolation = Isolation.REPEATABLE_READ,
            propagation = Propagation.REQUIRED
    )
    @Override
    public MessageDto update(MessageDto dto) throws Exception {
        Optional<MessageEntity> originalEntity = messageRepository.findById(dto.getId());
        if (originalEntity.isPresent()) {
            MessageEntity entity = this.mapToEntity(dto);
            entity = this.messageRepository.save(entity);
            return this.mapToDto(entity);
        } else {
            throw new Exception("Entity not found");
        }
    }

    @Transactional(
            isolation = Isolation.SERIALIZABLE,
            propagation = Propagation.REQUIRED
    )
    @Override
    public void delete(Long id) {
        MessageEntity entity = messageRepository.findById(id)
                .orElseThrow();
        messageRepository.delete(entity);
    }

    public MessageDto mapToDto(MessageEntity entity) {
        return new MessageDto(entity.getId(), entity.getText(), entity.getAuthor());
    }

    public MessageEntity mapToEntity(MessageDto dto) {
        return new MessageEntity(dto.getId(), dto.getText(), dto.getAuthor());
    }
}

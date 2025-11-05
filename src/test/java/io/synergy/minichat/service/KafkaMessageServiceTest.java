package io.synergy.minichat.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KafkaMessageServiceTest {

    @Mock
    private ChatService chatService;

    @Mock
    private KafkaStreamBridge kafkaStreamBridge;

    @InjectMocks
    private KafkaMessageService kafkaMessageService;

    @BeforeEach
    void setUp() {
        kafkaMessageService.init();
    }

    @Test
    void testProducer_ReturnsMessage_WhenDequeNotEmpty() {
        Message<String> message = kafkaMessageService.producer().get();

        assert message != null;
        assertEquals("Message 1", message.getPayload());

        Message<String> nextMessage = kafkaMessageService.producer().get();
        assert nextMessage != null;
        assertEquals("Message 2", nextMessage.getPayload());
    }

    @Test
    void testProducer_ReturnsNull_WhenDequeEmpty() {
        while (!kafkaMessageService.deque.isEmpty()) {
            kafkaMessageService.producer().get();
        }

        Message<String> message = kafkaMessageService.producer().get();
        assert message == null;
    }

    @Test
    void testConsumer_ProcessesMessage_AndSendsReply() {
        Message<String> inputMessage = MessageBuilder
                .withPayload("Test question")
                .build();

        when(chatService.processMessage("Test question"))
                .thenReturn("Test answer");

        kafkaMessageService.consumer().accept(inputMessage);

        verify(chatService).processMessage("Test question");

        verify(kafkaStreamBridge).sendMessage(argThat(msg ->
                msg.getPayload().equals("Test answer") &&
                        msg.getHeaders().containsKey("id") &&
                        msg.getHeaders().containsKey("timestamp")
        ));
    }

    @Test
    void testConsumer_HandlesNullPayload() {
        Message<String> mockedMessage = Mockito.mock(Message.class);
        when(mockedMessage.getPayload()).thenReturn(null);

        kafkaMessageService.consumer().accept(mockedMessage);

        verify(chatService, never()).processMessage(any());
        verify(kafkaStreamBridge, never()).sendMessage(any());
    }

}
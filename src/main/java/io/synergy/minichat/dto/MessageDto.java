package io.synergy.minichat.dto;

import lombok.Data;

@Data
public class MessageDto {
    private Long id;
    private String text;
    private String author;

    public MessageDto() {
    }

    public MessageDto(Long id, String text, String author) {
        this.id = id;
        this.text = text;
        this.author = author;
    }
}

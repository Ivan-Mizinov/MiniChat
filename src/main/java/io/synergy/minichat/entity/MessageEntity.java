package io.synergy.minichat.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "message")
public class MessageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "text")
    private String text;
    @Column(name = "author")
    private String author;

    public MessageEntity() {
    }

    public MessageEntity(Long id, String text, String author) {
        this.id = id;
        this.text = text;
        this.author = author;
    }
}

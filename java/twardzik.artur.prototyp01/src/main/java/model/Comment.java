package model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Comment {
    private UUID id;
    private User author;
    private String text;
    private LocalDateTime createdAt;

    public Comment(User author, String text) {
        this.id = UUID.randomUUID();
        this.author = author;
        this.text = text;
        this.createdAt = LocalDateTime.now();
    }

    public String getCommentText() {
        return text;
    }
}

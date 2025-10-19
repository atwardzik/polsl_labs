package model;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Class that holds comments that can be assigned to issues
 *
 * @author Artur Twardzik
 * @version 0.1
 */
public class Comment {
    /**
     * Comment ID
     */
    private UUID id;

    /**
     * Author of a comment
     */
    private User author;

    /**
     * Comment contents
     */
    private String text;

    /**
     * Date at which the comment was created
     */
    private LocalDateTime createdAt;

    /**
     * Comment constructor
     *
     * @param author of a comment
     * @param text   contents
     */
    public Comment(User author, String text) {
        this.id = UUID.randomUUID();
        this.author = author;
        this.text = text;
        this.createdAt = LocalDateTime.now();
    }

    /**
     * Get contents of a comment
     *
     * @return contents of a comment
     */
    public String getText() {
        return text;
    }

    /**
     * Get author of a comment
     *
     * @return author of a comment
     */
    public User getAuthor() {
        return author;
    }

    /**
     * Get date at which the comment was created of a comment
     *
     * @return date of comment creation
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}

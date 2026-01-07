package model;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Class that holds comments that can be assigned to issues
 *
 * @author Artur Twardzik
 * @version 0.3
 */
public class Comment {
    /**
     * Comment ID
     */
    private UUID id;

    /**
     * Author of a comment
     */
    @Getter
    private User author;

    /**
     * Comment contents
     */
    @Getter
    private String text;

    /**
     * Date at which the comment was created
     */
    @Getter
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

}

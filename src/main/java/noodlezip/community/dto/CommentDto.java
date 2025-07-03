package noodlezip.community.dto;

import lombok.*;
import noodlezip.community.entity.Comment;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentDto {
    private long id;
    private String author;
    private String authorProfileImageUrl;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public CommentDto(Comment comment) {
        this.id = comment.getId();
        if (comment.getUser() != null) {
            this.author = comment.getUser().getUserName();
            this.authorProfileImageUrl = comment.getUser().getProfileImageUrl();
        } else {
            this.author = null;
            this.authorProfileImageUrl = null;
        }
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt();
        this.updatedAt = comment.getUpdatedAt();
    }

    public static CommentDto from(Comment comment) {
        return new CommentDto(comment);
    }
}

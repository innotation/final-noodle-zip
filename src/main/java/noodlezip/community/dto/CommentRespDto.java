package noodlezip.community.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;
import noodlezip.community.entity.Comment;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class CommentRespDto {
    private Long id;
    private String author;
    private String authorProfileImageUrl;
    private Long userId;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isWriter;

    @QueryProjection
    public CommentRespDto(Long id, String author, String authorProfileImageUrl, Long userId, String content, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.author = author;
        this.authorProfileImageUrl = authorProfileImageUrl;
        this.userId = userId;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}

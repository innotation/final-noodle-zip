package noodlezip.board.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import noodlezip.common.entity.BaseTimeEntity;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "tbl_comment")
public class Comment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "community_id", nullable = false)
    private Long communityId;

    @NotNull
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Size(max = 300)
    @NotNull
    @Column(name = "content", nullable = false, length = 300)
    private String content;

    @Size(max = 30)
    @NotNull
    @Column(name = "comment_status", nullable = false, length = 30)
    private String commentStatus;

    @Column(name = "parent_comment_id")
    private Long parentCommentId;

}
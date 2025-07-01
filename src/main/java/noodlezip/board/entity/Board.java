package noodlezip.board.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import noodlezip.common.entity.BaseTimeEntity;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "tbl_community")
public class Board extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "community_id", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Size(max = 30)
    @NotNull
    @Column(name = "title", nullable = false, length = 30)
    private String title;

    @Lob
    @Column(name = "content")
    private String content;

    @Size(max = 30)
    @NotNull
    @Column(name = "community_type", nullable = false, length = 30)
    private String communityType;

    @Size(max = 30)
    @NotNull
    @Column(name = "post_status", nullable = false, length = 30)
    private String postStatus;

    @Column(name = "review_store_id")
    private Long reviewStoreId;

    @Column(name = "review_visit_date")
    private Instant reviewVisitDate;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "likes_count", nullable = false)
    private Integer likesCount;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "views_count", nullable = false)
    private Integer viewsCount;

}
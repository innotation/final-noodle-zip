package noodlezip.community.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import noodlezip.common.entity.BaseTimeEntity;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "tbl_community")
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
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

    @Enumerated(EnumType.STRING)
    @Column(name = "post_status", nullable = false, length = 30)
    private CommunityActiveStatus postStatus;

    @Column(name = "review_store_id")
    private Long reviewStoreId;

    @Column(name = "review_visit_date")
    private Instant reviewVisitDate;

    @ColumnDefault("0")
    @Column(name = "likes_count", nullable = false)
    private Integer likesCount;

    @ColumnDefault("0")
    @Column(name = "views_count", nullable = false)
    private Integer viewsCount;

    @Column(name = "image_url")
    private String imageUrl;

}
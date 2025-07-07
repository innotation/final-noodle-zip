package noodlezip.community.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import noodlezip.community.entity.CommunityActiveStatus;

import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class BoardRespDto {

    private Long boardId;
    private Long userId;
    private String userName;
    private String userProfileImageUrl;
    private String title;
    private String content;
    private String communityType;
    private CommunityActiveStatus postStatus;
    private int likesCount;
    private int viewsCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String imageUrl;
    private Boolean isWriter;

}

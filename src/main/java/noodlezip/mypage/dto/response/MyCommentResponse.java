package noodlezip.mypage.dto.response;

import lombok.*;

import java.time.LocalDateTime;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class MyCommentResponse {

    private Long id;
    private Long communityBoardId;
    private String boardTitle;
    private String author;
    private String authorProfileImageUrl;
    private Long userId;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
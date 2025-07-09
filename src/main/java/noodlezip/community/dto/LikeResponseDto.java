package noodlezip.community.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LikeResponseDto {
    private boolean isLiked;
    private long totalLikes;
}
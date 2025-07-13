package noodlezip.community.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import noodlezip.community.status.CommunityType;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryCountDto {
    private String communityType;
    private Long count;
    private String displayName;
    
    public CategoryCountDto(String communityType, Long count) {
        this.communityType = communityType;
        this.count = count;
        this.displayName = CommunityType.getDisplayName(communityType);
    }
} 
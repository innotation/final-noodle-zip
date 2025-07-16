package noodlezip.community.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PopularTagDto {
    private String tagName;
    private String tagType; // "category" 또는 "soup"
    private Long count;
} 
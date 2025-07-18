package noodlezip.store.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExtraToppingRequestDto {
    private Long toppingId;
    private Integer price;
}
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
    private String name;

    public static ExtraToppingRequestDto fromEntity(noodlezip.store.entity.StoreExtraTopping extraTopping) {
        return ExtraToppingRequestDto.builder()
                .toppingId(extraTopping.getTopping().getId())
                .name(extraTopping.getTopping().getToppingName())
                .price(extraTopping.getPrice())
                .build();
    }
}
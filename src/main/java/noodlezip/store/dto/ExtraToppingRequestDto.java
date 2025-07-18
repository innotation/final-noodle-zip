package noodlezip.store.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ExtraToppingRequestDto {
    private String name;
    private Integer price;

    public static ExtraToppingRequestDto fromEntity(noodlezip.store.entity.StoreExtraTopping extraTopping) {
        return ExtraToppingRequestDto.builder()
                .name(extraTopping.getTopping().getToppingName())
                .price(extraTopping.getPrice())
                .build();
    }
}
package noodlezip.store.dto;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StoreIdNameDto {
    private Long id;
    private String name;
}

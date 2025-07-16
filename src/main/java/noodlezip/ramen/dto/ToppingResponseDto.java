package noodlezip.ramen.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class ToppingResponseDto {
    private Long id;
    private String name;
    private Integer price;

    public ToppingResponseDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
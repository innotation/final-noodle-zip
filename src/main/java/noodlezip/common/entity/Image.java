package noodlezip.common.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "tbl_image")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id", nullable = false)
    private Long id;

    @Size(max = 30)
    @NotNull
    @Column(name = "image_type", nullable = false, length = 30)
    private String imageType;

    @NotNull
    @Column(name = "target_id", nullable = false)
    private Long targetId;

    @NotNull
    @Column(name = "image_order", nullable = false)
    private Integer imageOrder;

    @NotNull
    @Column(name = "image_url", nullable = false)
    private String imageUrl;

}
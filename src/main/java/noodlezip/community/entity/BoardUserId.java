package noodlezip.community.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class BoardUserId implements Serializable {

  @Column(name = "user_id")
  private Long userId;

  @Column(name = "community_id")
  private Long communityId;
}
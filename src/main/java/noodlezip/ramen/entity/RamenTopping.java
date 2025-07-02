package noodlezip.ramen.entity;

import jakarta.persistence.*;
import lombok.*;
import noodlezip.store.entity.Menu;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Table(name = "tbl_ramen_topping")
public class RamenTopping {

    @EmbeddedId
    private RamenToppingId id; // 필드명은 'id'로 하는 것이 일반적입니다.

    // menuId 부분은 Menu 엔티티의 PK와 매핑됩니다.
    @MapsId("menuId") // RamenToppingId의 menuId 필드를 매핑
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", insertable = false, updatable = false) // 실제 DB 컬럼명, ID이므로 insertable/updatable false
    private Menu menu;

    // toppingId 부분은 Topping 엔티티의 PK와 매핑됩니다.
    @MapsId("toppingId") // RamenToppingId의 toppingId 필드를 매핑
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topping_id", insertable = false, updatable = false) // 실제 DB 컬럼명, ID이므로 insertable/updatable false
    private Topping topping;

    // 편의 메서드: 엔티티 객체를 받아 ID를 자동으로 설정
    public void setMenu(Menu menu) {
        this.menu = menu;
        if (this.id == null) {
            this.id = new RamenToppingId();
        }
        this.id.setMenuId(menu.getId());
    }

    public void setTopping(Topping topping) {
        this.topping = topping;
        if (this.id == null) {
            this.id = new RamenToppingId();
        }
        this.id.setToppingId(topping.getId());
    }

}
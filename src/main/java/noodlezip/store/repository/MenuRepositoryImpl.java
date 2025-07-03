package noodlezip.store.repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import noodlezip.ramen.entity.QCategory;
import noodlezip.ramen.entity.QRamenSoup;
import noodlezip.ramen.entity.QRamenTopping;
import noodlezip.ramen.entity.QTopping;
import noodlezip.store.dto.MenuDetailDto;
import noodlezip.store.entity.QMenu;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Repository
public class MenuRepositoryImpl implements MenuRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<MenuDetailDto> findMenuDetailByStoreId(Long storeId) {
        QMenu menu = QMenu.menu;
        QCategory category = QCategory.category;
        QRamenSoup soup = QRamenSoup.ramenSoup;
        QRamenTopping ramenTopping = QRamenTopping.ramenTopping;
        QTopping topping = QTopping.topping;

        // 1. 메뉴, 카테고리, 국물 정보 조회
        List<MenuDetailDto> menuList = queryFactory
                .select(Projections.constructor(MenuDetailDto.class,
                        menu.id,
                        menu.menuName,
                        menu.price,
                        menu.menuDescription,
                        menu.menuImageUrl,
                        category.categoryName,
                        soup.soupName,
                        Expressions.nullExpression() // 나중에 toppingNames 세팅
                ))
                .from(menu)
                .join(menu.category, category)
                .join(menu.ramenSoup, soup)
                .where(menu.storeId.id.eq(storeId))
                .fetch();

        List<Long> menuIds = menuList.stream()
                .map(MenuDetailDto::getMenuId)
                .toList();

        // 2. 토핑 조회
        List<Tuple> toppingTuples = queryFactory
                .select(ramenTopping.menu.id, topping.toppingName)
                .from(ramenTopping)
                .join(ramenTopping.topping, topping)
                .where(ramenTopping.menu.id.in(menuIds))
                .fetch();

        // 3. menuId → toppingName 리스트로 그룹핑
        Map<Long, List<String>> menuToppingMap = toppingTuples.stream()
                .collect(Collectors.groupingBy(
                        tuple -> tuple.get(ramenTopping.menu.id),
                        Collectors.mapping(tuple -> tuple.get(topping.toppingName), Collectors.toList())
                ));

        // 4. 각 DTO에 토핑 리스트 세팅
        for (MenuDetailDto dto : menuList) {
            dto.setToppingNames(menuToppingMap.getOrDefault(dto.getMenuId(), List.of()));
        }

        return menuList;
    }
}

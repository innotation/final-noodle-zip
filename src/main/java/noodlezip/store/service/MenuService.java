package noodlezip.store.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import noodlezip.store.entity.Menu;
import noodlezip.store.repository.MenuRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class MenuService {

    private final MenuRepository menuRepository;

    public List<Integer> getRamenCategoryIdListByMenuIds(List<Long> menuIds) {
        return menuRepository.findAllCategoryIdByMenuIds(menuIds);
    }

}

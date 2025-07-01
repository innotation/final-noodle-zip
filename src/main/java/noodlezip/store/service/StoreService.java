package noodlezip.store.service;

import lombok.RequiredArgsConstructor;
import noodlezip.ramen.dto.CategoryResponseDto;
import noodlezip.ramen.dto.ToppingResponseDto;
import noodlezip.ramen.entity.Category;
import noodlezip.ramen.entity.RamenSoup;
import noodlezip.ramen.entity.RamenTopping;
import noodlezip.ramen.entity.RamenToppingId;
import noodlezip.ramen.entity.Topping;
import noodlezip.ramen.repository.RamenToppingRepository;
import noodlezip.ramen.service.RamenService;
import noodlezip.store.dto.MenuRequestDto;
import noodlezip.store.dto.StoreRequestDto;
import noodlezip.store.entity.Menu;
import noodlezip.store.entity.Store;
import noodlezip.store.entity.StoreWeekSchedule;
import noodlezip.store.entity.StoreWeekScheduleId;
import noodlezip.store.repository.MenuRepository;
import noodlezip.store.repository.StoreRepository;
import noodlezip.store.repository.StoreWeekScheduleRepository;
import noodlezip.users.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class StoreService {

    private final StoreRepository storeRepository;
    private final StoreWeekScheduleRepository scheduleRepository;
    private final MenuRepository menuRepository;
    private final RamenToppingRepository ramenToppingRepository;
    private final RamenService ramenService;

    @Value("${upload.path}")
    private String uploadDir;

    @Value("${upload.url.prefix}")
    private String urlPrefix;

    @Transactional(rollbackFor = Exception.class)
    public Long registerStore(StoreRequestDto dto, Long userId, MultipartFile storeMainImage) throws IOException {
        User user = new User();
        user.setId(userId);

        String imageUrl = null;
        if (storeMainImage != null && !storeMainImage.isEmpty()) {
            imageUrl = saveFile(storeMainImage);
        }

        Store store = Store.builder()
                .storeName(dto.getStoreName())
                .address(dto.getAddress())
                .phone(dto.getPhone())
                .isLocalCard(dto.getIsLocalCard())
                .isChildAllowed(dto.getIsChildAllowed())
                .hasParking(dto.getHasParking())
                .ownerComment(dto.getOwnerComment())
                .storeMainImageUrl(imageUrl)
                .storeLat(dto.getStoreLat())
                .storeLng(dto.getStoreLng())
                .approvalStatus(dto.getApprovalStatus())
                .operationStatus(dto.getOperationStatus())
                .userId(userId)
                .build();

        store.setStoreLegalCode(dto.getStoreLegalCode());

        Store savedStore = storeRepository.save(store);

        if (dto.getWeekSchedule() != null) {
            List<StoreWeekSchedule> schedules = dto.getWeekSchedule().stream()
                    .map(s -> {
                        StoreWeekScheduleId id = new StoreWeekScheduleId();
                        id.setStoreId(savedStore.getId());
                        id.setDayOfWeek(s.getDayOfWeek());

                        StoreWeekSchedule schedule = new StoreWeekSchedule();
                        schedule.setId(id);
                        schedule.setOpeningAt(s.getOpeningAt());
                        schedule.setClosingAt(s.getClosingAt());
                        schedule.setIsClosedDay(s.getIsClosedDay());
                        return schedule;
                    }).collect(Collectors.toList());

            scheduleRepository.saveAll(schedules);
        }

        // 메뉴 및 기본 토핑 저장
        if (dto.getMenus() != null) {
            for (MenuRequestDto m : dto.getMenus()) {
                RamenSoup soup = new RamenSoup();
                soup.setId(m.getRamenSoupId());

                Category category = new Category();
                category.setId(m.getRamenCategoryId());

                Menu menu = Menu.builder()
                        .storeId(savedStore)
                        .menuName(m.getMenuName())
                        .price(m.getPrice())
                        .menuDescription(m.getMenuDescription())
                        .menuImageUrl(m.getMenuImageUrl())
                        .category(category)
                        .ramenSoupId(soup)
                        .build();

                Menu savedMenu = menuRepository.save(menu);

                if (m.getDefaultToppingIds() != null) {
                    for (Integer toppingId : m.getDefaultToppingIds()) {
                        RamenToppingId toppingIdObj = new RamenToppingId();
                        toppingIdObj.setMenuId(savedMenu.getId());
                        toppingIdObj.setToppingId(toppingId);

                        RamenTopping ramenTopping = new RamenTopping();
                        ramenTopping.setToppingId(toppingIdObj);
                        ramenTopping.setMenu(savedMenu);

                        // Topping 객체도 연관관계 맞춰 설정
                        Topping topping = new Topping();
                        topping.setId(toppingId);
                        ramenTopping.setTopping(topping);

                        ramenToppingRepository.save(ramenTopping);
                    }
                }
            }
        }

        return savedStore.getId();
    }

    // 이미지 저장 메서드 (이미지 MIME 타입 체크 포함)
    private String saveFile(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String ext = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            ext = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("이미지 파일만 업로드 가능합니다.");
        }

        String savedFilename = UUID.randomUUID().toString() + ext;
        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path filePath = uploadPath.resolve(savedFilename);
        file.transferTo(filePath.toFile());

        return urlPrefix + savedFilename;
    }

    // 라멘 카테고리 목록 조회
    public List<CategoryResponseDto> getRamenCategories() {
        return ramenService.getAllCategories();
    }

    // 라멘 토핑 목록 조회
    public List<ToppingResponseDto> getRamenToppings() {
        return ramenService.getAllToppings();
    }
}

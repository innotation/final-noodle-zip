package noodlezip.store.service;

import lombok.RequiredArgsConstructor;
import noodlezip.ramen.dto.CategoryResponseDto;
import noodlezip.ramen.dto.ToppingResponseDto;
import noodlezip.ramen.entity.RamenCategory;
import noodlezip.ramen.entity.RamenSoup;
import noodlezip.ramen.entity.RamenTopping;
import noodlezip.ramen.entity.RamenToppingId;
import noodlezip.ramen.service.RamenService;
import noodlezip.store.dto.MenuRequestDto;
import noodlezip.store.dto.StoreRequestDto;
import noodlezip.store.entity.Menu;
import noodlezip.store.entity.Store;
import noodlezip.store.entity.StoreWeekSchedule;
import noodlezip.store.entity.StoreWeekScheduleId;
import noodlezip.store.repository.StoreMenuRepository;
import noodlezip.store.repository.StoreRepository;
import noodlezip.store.repository.StoreScheduleRepository;
import noodlezip.users.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final StoreScheduleRepository scheduleRepository;
    private final StoreMenuRepository menuRepository;
    private final noodlezip.ramen.repository.RamenToppingRepository ramenToppingRepository;
    private final RamenService ramenService;

    @Value("${upload.path}")
    private String uploadDir;

    @Value("${upload.url.prefix}")
    private String urlPrefix;

    @Transactional
    public Long registerStore(StoreRequestDto dto, Long userId, MultipartFile storeMainImage) throws IOException {
        // User 엔티티 세팅 (ID만)
        User user = new User();
        user.setId(userId);

        // 이미지 업로드 처리
        String imageUrl = null;
        if (storeMainImage != null && !storeMainImage.isEmpty()) {
            imageUrl = saveFile(storeMainImage);
        }

        // Store 엔티티 생성 및 저장
        Store store = Store.builder()
                .storeName(dto.getStoreName())
                .address(dto.getAddress())
                .phone(dto.getPhone())
                .isLocalCard(dto.getIsLocalCard())
                .isChildAllowed(dto.getIsChildAllowed())
                .hasParking(dto.getHasParking())
                .ownerComment(dto.getOwnerComment())
                .storeMainImageUrl(imageUrl)
                .xAxis(dto.getXAxis())
                .yAxis(dto.getYAxis())
                .approvalStatus(dto.getApprovalStatus())
                .operationStatus(dto.getOperationStatus())
                .user(user)
                .build();

        Store savedStore = storeRepository.save(store);

        // 영업시간 저장
        if (dto.getWeekSchedule() != null) {
            List<StoreWeekSchedule> schedules = dto.getWeekSchedule().stream()
                    .map(s -> {
                        StoreWeekScheduleId id = new StoreWeekScheduleId();
                        id.setStoreId(savedStore.getId());
                        id.setDayOfWeek(s.getDayOfWeek());

                        StoreWeekSchedule schedule = new StoreWeekSchedule();
                        schedule.setId(id);
                        schedule.setOpeningAt(LocalDateTime.of(LocalDate.now(), s.getOpeningAt()));
                        schedule.setClosingAt(LocalDateTime.of(LocalDate.now(), s.getClosingAt()));
                        schedule.setIsClosedDay(s.getIsClosedDay());
                        return schedule;
                    })
                    .collect(Collectors.toList());
            scheduleRepository.saveAll(schedules);
        }

        // 메뉴 및 기본 토핑 저장
        if (dto.getMenus() != null) {
            for (MenuRequestDto m : dto.getMenus()) {
                RamenCategory category = new RamenCategory();
                category.setId(m.getRamenCategoryId());

                RamenSoup soup = new RamenSoup();
                soup.setId(m.getRamenSoupId());

                Menu menu = Menu.builder()
                        .store(savedStore)
                        .menuName(m.getMenuName())
                        .price(m.getPrice())
                        .menuDescription(m.getMenuDescription())
                        .menuImageUrl(m.getMenuImageUrl())
                        .ramenCategory(category)
                        .ramenSoup(soup)
                        .build();

                Menu savedMenu = menuRepository.save(menu);

                if (m.getDefaultToppingIds() != null) {
                    m.getDefaultToppingIds().forEach(toppingId -> {
                        RamenToppingId toppingIdObj = new RamenToppingId();
                        toppingIdObj.setMenuId(savedMenu.getId());
                        toppingIdObj.setToppingId(toppingId);

                        RamenTopping ramenTopping = new RamenTopping();
                        ramenTopping.setId(toppingIdObj);

                        ramenToppingRepository.save(ramenTopping);
                    });
                }
            }
        }

        return savedStore.getId();
    }

    // 이미지 저장
    private String saveFile(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String ext = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            ext = originalFilename.substring(originalFilename.lastIndexOf("."));
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

    /* 라멘 카테고리 목록 조회 */
    public List<CategoryResponseDto> getRamenCategories() {
        return ramenService.getAllCategories();
    }

    /* 라멘 토핑 목록 조회 */
    public List<ToppingResponseDto> getRamenToppings() {
        return ramenService.getAllToppings();
    }
}
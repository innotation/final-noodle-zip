package noodlezip.store.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import noodlezip.store.dto.StoreRequestDto;
import noodlezip.store.service.StoreService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stores")
@Tag(name = "Store API", description = "매장 관련 API")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    @Operation(summary = "매장 등록", description = "신규 매장을 등록합니다.")
    @PostMapping
    public ResponseEntity<Long> registerStore(@ModelAttribute StoreRequestDto dto) {
        Long storeId = storeService.registerStore(dto);
        return ResponseEntity.ok(storeId);
    }

}

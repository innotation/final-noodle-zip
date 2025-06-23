package noodlezip.store.controller;

import lombok.RequiredArgsConstructor;
import noodlezip.store.dto.StoreRequestDto;
import noodlezip.store.entity.Store;
import noodlezip.store.service.StoreService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/stores")
@RequiredArgsConstructor
public class StoreController {
    private final StoreService storeService;

    @PostMapping
    public ResponseEntity<?> createStore(@RequestBody StoreRequestDto dto) {
        Store store = storeService.saveStore(dto);
        return ResponseEntity.ok(store.getStoreId());
    }
}
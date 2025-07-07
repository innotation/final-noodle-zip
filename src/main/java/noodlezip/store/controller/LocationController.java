package noodlezip.store.controller;

import lombok.RequiredArgsConstructor;
import noodlezip.store.dto.AddressRequestDto;
import noodlezip.store.dto.LocationInfoDto;
import noodlezip.store.service.LocationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    @PostMapping("/location-info")
    public ResponseEntity<LocationInfoDto> getLocationInfo(@RequestBody AddressRequestDto dto) {
        return ResponseEntity.ok(locationService.getLocationInfo(dto.getAddress()));
    }
}

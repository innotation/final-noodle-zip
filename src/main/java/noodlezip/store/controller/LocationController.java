package noodlezip.store.controller;

import lombok.RequiredArgsConstructor;
import noodlezip.store.dto.AddressRequestDto;
import noodlezip.store.dto.LocationInfoDto;
import noodlezip.store.service.LocationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/location")
@RequiredArgsConstructor
public class LocationController {
    private final LocationService locationService;

    @PostMapping("/info")
    public LocationInfoDto getLocationInfo(@RequestBody AddressRequestDto dto) {
        return locationService.getLocationInfo(dto.getRoadAddress());
    }
}
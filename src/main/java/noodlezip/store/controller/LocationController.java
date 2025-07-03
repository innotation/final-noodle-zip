package noodlezip.store.controller;

import jdk.jfr.ContentType;
import lombok.RequiredArgsConstructor;
import noodlezip.store.dto.AddressRequestDto;
import noodlezip.store.dto.LocationInfoDto;
import noodlezip.store.service.LocationService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/location")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    @PostMapping("/info")
    @ResponseBody
    public LocationInfoDto getLocationInfo(@RequestBody AddressRequestDto dto) {
        System.out.println(dto.getRoadAddress());
        return locationService.getLocationInfo(dto.getRoadAddress());
    }
}
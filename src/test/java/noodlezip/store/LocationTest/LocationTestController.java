package noodlezip.store.LocationTest;

import noodlezip.store.dto.LocationInfoDto;
import noodlezip.store.service.LocationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LocationTestController {

    private final LocationService locationService;

    public LocationTestController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping("/test/location")
    public LocationInfoDto getLocation(@RequestParam String address) {
        return locationService.getLocationInfo(address);
    }
}
package noodlezip.store.LocationTest;

import noodlezip.store.dto.LocationInfoDto;
import noodlezip.store.service.LocationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class LocationServiceTest {

    @Autowired
    private LocationService locationService;

    @Test
    public void testGetLocationInfo() {
        // 테스트할 주소
        String testAddress = "서울 강남구 테헤란로 123";

        // 서비스 호출
        LocationInfoDto locationInfo = locationService.getLocationInfo(testAddress);

        // 결과 출력 (콘솔 확인용)
        System.out.println("위도: " + locationInfo.getStoreLat());
        System.out.println("경도: " + locationInfo.getStoreLng());
        System.out.println("법정동 코드: " + locationInfo.getStoreLegalCode());

        // 간단한 검증 - null 값이 아니어야 함
        assertNotNull(locationInfo);
        assertNotNull(locationInfo.getStoreLat());
        assertNotNull(locationInfo.getStoreLng());
        assertNotNull(locationInfo.getStoreLegalCode());
    }
}

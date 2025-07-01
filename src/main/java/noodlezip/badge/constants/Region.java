package noodlezip.badge.constants;

import lombok.Getter;

@Getter
public enum Region {

    SEOUL(11, "서울"),
    BUSAN(26, "부산"),
    DAEGU(27, "대구"),
    INCHEON(28, "인천"),
    GWANGJU(29, "광주"),
    DAEJEON(30, "대전"),
    ULSAN(31, "울산"),
    SEJONG(36, "세종"),
    GYEONGGI(41, "경기"),
    GANGWON(42, "강원"),
    CHUNGBUK(43, "충북"),
    CHUNGNAM(44, "충남"),
    JEONBUK(45, "전북"),
    JEONNAM(46, "전남"),
    GYEONGBUK(47, "경북"),
    GYEONGNAM(48, "경남"),
    JEJU(50, "제주");

    private final int code;
    private final String name;

    Region(int code, String name) {
        this.code = code;
        this.name = name;
    }


    public static Region getRegionByCode(int legalCode) {
        int sido = Integer.parseInt(String.valueOf(legalCode).substring(0, 2));

        for (Region region : Region.values()) {
            if (region.code == sido) {
                return region;
            }
        }
        return null;
    }

}

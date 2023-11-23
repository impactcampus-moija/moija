package impact.moija.domain.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Location {
    SEOUL("서울", "003002001"),
    BUSAN("부산", "003002002"),
    DAEGU("대구", "003002003"),
    INCHEON("인천", "003002004"),
    GWANGJU("광주", "003002005"),
    DAEJEON("대전", "003002006"),
    ULSAN("울산", "003002007"),
    GYEONGGI("경기", "003002008"),
    GANGWON("강원", "003002009"),
    CHUNGBUK("충북", "003002010"),
    CHUNGNAM("충남", "003002011"),
    JEONBUK("전북", "003002012"),
    JEONNAM("전남", "003002013"),
    GYEONGBUK("경북", "003002014"),
    GYEONGNAM("경남", "003002015"),
    JEJU("제주", "003002016"),
    SEJONG("세종", "003002017"),
    ;

    private final String name;
    private final String code;
}

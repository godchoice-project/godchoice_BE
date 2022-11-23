package com.team03.godchoice.enumclass;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RegionTag {
    Seoul("서울"),
    Gyeonggi("경기"),
    Gangwon("강원"),
    Gyeongsang("경상"),
    Jeolla("전라"),
    Chungcheong("충청"),
    Jeju("제주"),
    Incheon("인천"),
    Sejong("세종"),
    Daegu("대구"),
    Busan("부산"),
    Ulsan("울산"),
    Gwangju("광주"),
    Daejeon("대전");

    private final String region;

}

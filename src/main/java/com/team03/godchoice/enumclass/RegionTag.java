package com.team03.godchoice.enumclass;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RegionTag {
    Seoul("서울"),
    Gyeonggi("경기도"),
    Gangwon("강원도"),
    Gyeongsang("경상도"),
    Jeolla("전라도"),
    Chungcheong("충청도"),
    Jeju("제주도"),
    Incheon("인천"),
    Sejong("세종"),
    Daegu("대구"),
    Busan("부산"),
    Ulsan("울산"),
    Gwangju("광주"),
    Daejeon("대전");

    private final String region;

}

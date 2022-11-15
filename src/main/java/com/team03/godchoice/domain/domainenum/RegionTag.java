package com.team03.godchoice.domain.domainenum;

import com.querydsl.core.types.dsl.BooleanExpression;
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
    Jeju("제주도");

    private final String region;

}

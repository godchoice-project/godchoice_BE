package com.team03.godchoice.abstrctPackage;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.team03.godchoice.enumclass.RegionTag;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class MakeRegionTagClass {

    private RegionTag getRegionTag(String key) {
        Map<String, RegionTag> regionTagMap = new HashMap<>() {{
            put("서울", RegionTag.Seoul);
            put("경기", RegionTag.Gyeonggi);
            put("강원", RegionTag.Gangwon);
            put("경상", RegionTag.Gyeongsang);
            put("경북", RegionTag.Gyeongsang);
            put("경남", RegionTag.Gyeongsang);
            put("전북", RegionTag.Jeolla);
            put("전남", RegionTag.Jeolla);
            put("전라", RegionTag.Jeolla);
            put("충북", RegionTag.Chungcheong);
            put("충남", RegionTag.Chungcheong);
            put("충청", RegionTag.Chungcheong);
            put("제주", RegionTag.Jeju);
            put("인천", RegionTag.Incheon);
            put("세종", RegionTag.Sejong);
            put("대구", RegionTag.Daegu);
            put("부산", RegionTag.Busan);
            put("울산", RegionTag.Ulsan);
            put("광주", RegionTag.Gwangju);
            put("대전", RegionTag.Daejeon);
        }};

        return regionTagMap.get(key);
    }

    //지역태그만드는 메서드
    public RegionTag toRegionTag(String region) {
        return getRegionTag(region.substring(0, 2));
    }


    public BooleanExpression listTag(List<String> tags) {
        if (tags == null) {
            return toBoolRegionTag("전국");
        } else if (tags.size() >= 1 && tags.size() < 14) {
            for (int i = tags.size(); i < 14; i++) {
                tags.add(i, "없음");
            }

            return toBoolRegionTag(tags.get(0))
                    .or(toBoolRegionTag(tags.get(1)))
                    .or(toBoolRegionTag(tags.get(2)))
                    .or(toBoolRegionTag(tags.get(3)))
                    .or(toBoolRegionTag(tags.get(4)))
                    .or(toBoolRegionTag(tags.get(5)))
                    .or(toBoolRegionTag(tags.get(6)))
                    .or(toBoolRegionTag(tags.get(7)))
                    .or(toBoolRegionTag(tags.get(8)))
                    .or(toBoolRegionTag(tags.get(9)))
                    .or(toBoolRegionTag(tags.get(10)))
                    .or(toBoolRegionTag(tags.get(11)))
                    .or(toBoolRegionTag(tags.get(12)));
        } else {
            return toBoolRegionTag("전국");
        }
    }

    public BooleanExpression toBoolRegionTag(String tag) {
        return null;
    }

}

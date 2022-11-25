package com.team03.godchoice.interfacepackage;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.team03.godchoice.enumclass.RegionTag;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.team03.godchoice.domain.eventpost.QEventPost.eventPost;

public interface MakeRegionTag {

    private RegionTag getRegionTag(String key){
        Map<String ,RegionTag> regionTagMap = new HashMap<>(){{
            put("서울",RegionTag.Seoul);
            put("경기",RegionTag.Gyeonggi);
            put("강원",RegionTag.Gangwon);
            put("경북",RegionTag.Gyeongsang);
            put("경남",RegionTag.Gyeongsang);
            put("전북",RegionTag.Jeolla);
            put("전남",RegionTag.Jeolla);
            put("충북",RegionTag.Chungcheong);
            put("충남",RegionTag.Chungcheong);
            put("제주",RegionTag.Jeju);
            put("인천",RegionTag.Incheon);
            put("세종",RegionTag.Sejong);
            put("대구",RegionTag.Daegu);
            put("부산",RegionTag.Busan);
            put("울산",RegionTag.Ulsan);
            put("광주",RegionTag.Gwangju);
            put("대전",RegionTag.Daejeon);
        }};

        return regionTagMap.get(key);
    }

    //지역태그만드는 메서드
    default RegionTag toRegionTag(String region) {
        return getRegionTag(region.substring(0,2));
    }


     default BooleanExpression listTag(List<String> tags) {
        if (tags == null) {
            return toboolRegionTag("전국");
        } else if (tags.size() >= 1 && tags.size() < 14) {
            for (int i = tags.size(); i < 14; i++) {
                tags.add(i, "없음");
            }

            return toboolRegionTag(tags.get(0))
                    .or(toboolRegionTag(tags.get(1)))
                    .or(toboolRegionTag(tags.get(2)))
                    .or(toboolRegionTag(tags.get(3)))
                    .or(toboolRegionTag(tags.get(4)))
                    .or(toboolRegionTag(tags.get(5)))
                    .or(toboolRegionTag(tags.get(6)))
                    .or(toboolRegionTag(tags.get(7)))
                    .or(toboolRegionTag(tags.get(8)))
                    .or(toboolRegionTag(tags.get(9)))
                    .or(toboolRegionTag(tags.get(10)))
                    .or(toboolRegionTag(tags.get(11)))
                    .or(toboolRegionTag(tags.get(12)));
        } else {
            return toboolRegionTag("전국");
        }
    }

     default BooleanExpression toboolRegionTag(String tag){
         Map<String, BooleanExpression> regionTagMap = new HashMap<>() {{
             put("서울", eventPost.regionTag.eq(RegionTag.Seoul));
             put("경기도", eventPost.regionTag.eq(RegionTag.Gyeonggi));
             put("강원도", eventPost.regionTag.eq(RegionTag.Gangwon));
             put("경상도", eventPost.regionTag.eq(RegionTag.Gyeongsang));
             put("전라도", eventPost.regionTag.eq(RegionTag.Jeolla));
             put("충청도", eventPost.regionTag.eq(RegionTag.Chungcheong));
             put("제주도", eventPost.regionTag.eq(RegionTag.Jeju));
             put("인천", eventPost.regionTag.eq(RegionTag.Incheon));
             put("세종", eventPost.regionTag.eq(RegionTag.Sejong));
             put("대구", eventPost.regionTag.eq(RegionTag.Daegu));
             put("부산", eventPost.regionTag.eq(RegionTag.Busan));
             put("울산", eventPost.regionTag.eq(RegionTag.Ulsan));
             put("광주", eventPost.regionTag.eq(RegionTag.Gwangju));
             put("대전", eventPost.regionTag.eq(RegionTag.Daejeon));
         }};

         return regionTagMap.get(tag);
    }
}

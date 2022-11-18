package com.team03.godchoice.repository.eventpost;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team03.godchoice.domain.Member;
import com.team03.godchoice.domain.domainenum.RegionTag;
import com.team03.godchoice.domain.eventpost.EventPost;
import com.team03.godchoice.dto.responseDto.eventpost.EventPostAllResDto;
import com.team03.godchoice.interfacepackage.MakeRegionTag;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import static com.team03.godchoice.domain.eventpost.QEventPost.eventPost;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class EventPostRepositoryImpl extends QuerydslRepositorySupport implements MakeRegionTag {
    private final JPAQueryFactory queryFactory;
    private final EventPostLikeRepository eventPostLikeRepository;

    public EventPostRepositoryImpl(JPAQueryFactory queryFactory, EventPostLikeRepository eventPostLikeRepository) {
        super(EventPost.class);
        this.queryFactory = queryFactory;
        this.eventPostLikeRepository = eventPostLikeRepository;
    }

    public PageImpl<?> searchEventPost(List<String> tag, String progress, String sort, String search, Pageable pageable, Member member) {
        List<EventPost> eventPostList = queryFactory
                .selectFrom(eventPost)
                .where(listTag(tag),
                        eventPost.eventStatus.eq(progress)
                                .and(searchKeyword(search)
                                ))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(listSort(sort), eventPost.eventPostId.desc())
                .fetch();

        return toPage(eventPostList, pageable, member);
    }

//    public BooleanExpression listTag(List<String> tags) {
//        if (tags == null) {
//            return toRegionTag("전국");
//        } else if (tags.size() >= 1 && tags.size() < 14) {
//            for (int i = tags.size(); i < 14; i++) {
//                tags.add(i, "없음");
//            }
//
//            return toRegionTag(tags.get(0))
//                    .or(toRegionTag(tags.get(1)))
//                    .or(toRegionTag(tags.get(2)))
//                    .or(toRegionTag(tags.get(3)))
//                    .or(toRegionTag(tags.get(4)))
//                    .or(toRegionTag(tags.get(5)))
//                    .or(toRegionTag(tags.get(6)))
//                    .or(toRegionTag(tags.get(7)))
//                    .or(toRegionTag(tags.get(8)))
//                    .or(toRegionTag(tags.get(9)))
//                    .or(toRegionTag(tags.get(10)))
//                    .or(toRegionTag(tags.get(11)))
//                    .or(toRegionTag(tags.get(12)));
//        } else {
//            return toRegionTag("전국");
//        }
//    }
//
//    public BooleanExpression toRegionTag(String tag) {
//        Map<String, BooleanExpression> regionTagMap = new HashMap<>() {{
//            put("서울", eventPost.regionTag.eq(RegionTag.Seoul));
//            put("경기도", eventPost.regionTag.eq(RegionTag.Gyeonggi));
//            put("강원도", eventPost.regionTag.eq(RegionTag.Gangwon));
//            put("경상도", eventPost.regionTag.eq(RegionTag.Gyeongsang));
//            put("전라도", eventPost.regionTag.eq(RegionTag.Jeolla));
//            put("충청도", eventPost.regionTag.eq(RegionTag.Chungcheong));
//            put("제주도", eventPost.regionTag.eq(RegionTag.Jeju));
//            put("인천", eventPost.regionTag.eq(RegionTag.Incheon));
//            put("세종", eventPost.regionTag.eq(RegionTag.Sejong));
//            put("대구", eventPost.regionTag.eq(RegionTag.Daegu));
//            put("부산", eventPost.regionTag.eq(RegionTag.Busan));
//            put("울산", eventPost.regionTag.eq(RegionTag.Ulsan));
//            put("광주", eventPost.regionTag.eq(RegionTag.Gwangju));
//            put("대전", eventPost.regionTag.eq(RegionTag.Daejeon));
//        }};
//
//        return regionTagMap.get(tag);
//    }

    public BooleanExpression searchKeyword(String search) {
        if (search == null) {
            return null;
        } else {
            return eventPost.title.contains(search)
                    .or(eventPost.content.contains(search));
        }
    }

    public OrderSpecifier<?> listSort(String sort) {
        if ("최신순".equals(sort)) {
            return new OrderSpecifier<>(Order.DESC, eventPost.eventPostId);
        }
        return new OrderSpecifier<>(Order.DESC, eventPost.viewCount);
    }

    private PageImpl toPage(List<EventPost> eventPostList, Pageable pageable, Member member) {
        List<EventPostAllResDto> eventPostAllResDtos = new ArrayList<>();
        for (EventPost eventPost : eventPostList) {
            boolean bookmark;
            if (member != null) {
                bookmark = eventPostLikeRepository.existsByMemberAndEventPost(member, eventPost);
            } else {
                bookmark = false;
            }

            eventPostAllResDtos.add(EventPostAllResDto.toEPARD(eventPost, bookmark));
        }
        return new PageImpl<>(eventPostAllResDtos, pageable, eventPostList.size());
    }
}

package com.team03.godchoice.repository.eventpost;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team03.godchoice.domain.domainenum.RegionTag;
import com.team03.godchoice.domain.eventpost.EventPost;
import com.team03.godchoice.dto.responseDto.EventPostAllResDto;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import static com.team03.godchoice.domain.eventpost.QEventPost.eventPost;

import java.util.ArrayList;
import java.util.List;

@Repository
public class EventPostRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public EventPostRepositoryImpl(JPAQueryFactory queryFactory) {
        super(EventPost.class);
        this.queryFactory = queryFactory;
    }

    public PageImpl searchEventPost(List<String> tag, String progress, String sort, String search, Pageable pageable) {
        List<EventPost> eventPostList = queryFactory
                .selectFrom(eventPost)
                .where(listTag(tag),
                        eventPost.eventStatus.eq(progress)
                                .and(eventPost.title.contains(search)
                                        .or(eventPost.content.contains(search))
                                ))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(listSort(sort),eventPost.eventPostId.desc())
                .fetch();

        return toPage(eventPostList);
    }

    public BooleanExpression listTag(List<String> tags) {
        if (tags.size() >= 1 && tags.size()<7) {
            if(tags.get(0).equals("")){
                return toRegionTag("전국");
            }else{
                for (int i = tags.size(); i < 7; i++) {
                    tags.add(i, "없음");
                }

                return toRegionTag(tags.get(0))
                        .or(toRegionTag(tags.get(1)))
                        .or(toRegionTag(tags.get(2)))
                        .or(toRegionTag(tags.get(3)))
                        .or(toRegionTag(tags.get(4)))
                        .or(toRegionTag(tags.get(5)));
            }
        } else {
            return toRegionTag("전국");
        }
    }

    public BooleanExpression toRegionTag(String tag) {
        switch (tag) {
            case "서울":
                return eventPost.regionTag.eq(RegionTag.Seoul);
            case "경기도":
                return eventPost.regionTag.eq(RegionTag.Gyeonggi);
            case "강원도":
                return eventPost.regionTag.eq(RegionTag.Gangwon);
            case "경상도":
                return eventPost.regionTag.eq(RegionTag.Gyeongsang);
            case "전라도":
                return eventPost.regionTag.eq(RegionTag.Jeolla);
            case "충청도":
                return eventPost.regionTag.eq(RegionTag.Chungcheong);
            case "제주도":
                return eventPost.regionTag.eq(RegionTag.Jeju);
            default:
                return null;
        }
    }

    public OrderSpecifier<?> listSort(String sort) {
        if ("최신순".equals(sort)) {
            return new OrderSpecifier<>(Order.DESC, eventPost.eventPostId);
        }
        return new OrderSpecifier<>(Order.DESC, eventPost.viewCount);
    }

    private PageImpl toPage(List<EventPost> eventPostList) {
        List<EventPostAllResDto> eventPostAllResDtos = new ArrayList<>();
        for (EventPost eventPost : eventPostList) {
            eventPostAllResDtos.add(EventPostAllResDto.toEPARD(eventPost));
        }
        return new PageImpl<>(eventPostAllResDtos);
    }
}

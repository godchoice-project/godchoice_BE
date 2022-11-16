package com.team03.godchoice.repository.gatherpost;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team03.godchoice.domain.domainenum.RegionTag;
import com.team03.godchoice.domain.gatherPost.GatherPost;
import com.team03.godchoice.dto.responseDto.gatherpost.GatherPostAllResDto;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import static com.team03.godchoice.domain.askpost.QAskPost.askPost;
import static com.team03.godchoice.domain.gatherPost.QGatherPost.gatherPost;

import java.util.ArrayList;
import java.util.List;

@Repository
public class GatherPostRepositoryImpl extends QuerydslRepositorySupport {

    private final JPAQueryFactory queryFactory;

    public GatherPostRepositoryImpl(JPAQueryFactory queryFactory) {
        super(GatherPost.class);
        this.queryFactory = queryFactory;
    }

    public Object searchGatherPost(List<String> tag, String progress, String sort, String search, Pageable pageable) {
        List<GatherPost> gatherPostList = queryFactory
                .selectFrom(gatherPost)
                .where(listTag(tag),
                        gatherPost.postStatus.eq(progress)
                                .and(searchKeyword(search)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(listSort(sort),gatherPost.gatherPostId.desc())
                .fetch();

        return toPage(gatherPostList);
    }

    public BooleanExpression listTag(List<String> tags) {
        if (tags.size() >= 1 && tags.size()<7) {
            for (int i = tags.size(); i < 7; i++) {
                tags.add(i, "없음");
            }

            return toRegionTag(tags.get(0))
                    .or(toRegionTag(tags.get(1)))
                    .or(toRegionTag(tags.get(2)))
                    .or(toRegionTag(tags.get(3)))
                    .or(toRegionTag(tags.get(4)))
                    .or(toRegionTag(tags.get(5)));
        } else {
            return toRegionTag("전국");
        }
    }

    public BooleanExpression toRegionTag(String tag) {
        switch (tag) {
            case "서울":
                return gatherPost.regionTag.eq(RegionTag.Seoul);
            case "경기도":
                return gatherPost.regionTag.eq(RegionTag.Gyeonggi);
            case "강원도":
                return gatherPost.regionTag.eq(RegionTag.Gangwon);
            case "경상도":
                return gatherPost.regionTag.eq(RegionTag.Gyeongsang);
            case "전라도":
                return gatherPost.regionTag.eq(RegionTag.Jeolla);
            case "충청도":
                return gatherPost.regionTag.eq(RegionTag.Chungcheong);
            case "제주도":
                return gatherPost.regionTag.eq(RegionTag.Jeju);
            default:
                return null;
        }
    }

    public BooleanExpression searchKeyword(String search){
        if(search==null){
            return null;
        }else{
            return gatherPost.title.contains(search)
                    .or(gatherPost.content.contains(search));
        }
    }

    public OrderSpecifier<?> listSort(String sort) {
        if ("최신순".equals(sort)) {
            return new OrderSpecifier<>(Order.DESC, gatherPost.gatherPostId);
        }
        return new OrderSpecifier<>(Order.DESC, gatherPost.viewCount);
    }

    private PageImpl toPage(List<GatherPost> gatherPostList) {
        List<GatherPostAllResDto> gatherPostAllResDtos = new ArrayList<>();
        for (GatherPost gatherPost : gatherPostList) {
            gatherPostAllResDtos.add(GatherPostAllResDto.toGPARD(gatherPost));
        }
        return new PageImpl<>(gatherPostAllResDtos);
    }
}

package com.team03.godchoice.repository.gatherpost;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team03.godchoice.domain.Member;
import com.team03.godchoice.enumclass.RegionTag;
import com.team03.godchoice.domain.gatherPost.GatherPost;
import com.team03.godchoice.dto.responseDto.gatherpost.GatherPostAllResDto;
import com.team03.godchoice.interfacepackage.MakeRegionTag;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import static com.team03.godchoice.domain.gatherPost.QGatherPost.gatherPost;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class GatherPostRepositoryImpl extends QuerydslRepositorySupport implements MakeRegionTag {

    private final JPAQueryFactory queryFactory;
    private final GatherPostLikeRepository gatherPostLikeRepository;

    public GatherPostRepositoryImpl(JPAQueryFactory queryFactory, GatherPostLikeRepository gatherPostLikeRepository) {
        super(GatherPost.class);
        this.queryFactory = queryFactory;
        this.gatherPostLikeRepository = gatherPostLikeRepository;
    }

    public Object searchGatherPost(List<String> tag, String progress, String sort, String search, Pageable pageable, Member member) {
        List<GatherPost> gatherPostList = queryFactory
                .selectFrom(gatherPost)
                .where(listTag(tag),
                        gatherPost.postStatus.eq(progress)
                                .and(searchKeyword(search)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(listSort(sort), gatherPost.gatherPostId.desc())
                .fetch();

        return toPage(gatherPostList, member);
    }

    @Override
    public BooleanExpression toboolRegionTag(String tag) {
        Map<String, BooleanExpression> regionTagMap = new HashMap<>() {{
            put("??????", gatherPost.regionTag.eq(RegionTag.Seoul));
            put("?????????", gatherPost.regionTag.eq(RegionTag.Gyeonggi));
            put("?????????", gatherPost.regionTag.eq(RegionTag.Gangwon));
            put("?????????", gatherPost.regionTag.eq(RegionTag.Gyeongsang));
            put("?????????", gatherPost.regionTag.eq(RegionTag.Jeolla));
            put("?????????", gatherPost.regionTag.eq(RegionTag.Chungcheong));
            put("?????????", gatherPost.regionTag.eq(RegionTag.Jeju));
            put("??????", gatherPost.regionTag.eq(RegionTag.Incheon));
            put("??????", gatherPost.regionTag.eq(RegionTag.Sejong));
            put("??????", gatherPost.regionTag.eq(RegionTag.Daegu));
            put("??????", gatherPost.regionTag.eq(RegionTag.Busan));
            put("??????", gatherPost.regionTag.eq(RegionTag.Ulsan));
            put("??????", gatherPost.regionTag.eq(RegionTag.Gwangju));
            put("??????", gatherPost.regionTag.eq(RegionTag.Daejeon));
        }};

        return regionTagMap.get(tag);
    }

    public BooleanExpression searchKeyword(String search) {
        if (search == null) {
            return null;
        } else {
            return gatherPost.title.contains(search)
                    .or(gatherPost.content.contains(search));
        }
    }

    public OrderSpecifier<?> listSort(String sort) {
        if ("?????????".equals(sort)) {
            return new OrderSpecifier<>(Order.DESC, gatherPost.gatherPostId);
        }
        return new OrderSpecifier<>(Order.DESC, gatherPost.viewCount);
    }

    private PageImpl toPage(List<GatherPost> gatherPostList, Member member) {
        List<GatherPostAllResDto> gatherPostAllResDtos = new ArrayList<>();
        for (GatherPost gatherPost : gatherPostList) {
            boolean bookmark;
            if (member != null) {
                bookmark = gatherPostLikeRepository.existsByMemberAndGatherPost(member, gatherPost);
            } else {
                bookmark = false;
            }
            gatherPostAllResDtos.add(GatherPostAllResDto.toGPARD(gatherPost, bookmark));
        }
        return new PageImpl<>(gatherPostAllResDtos);
    }
}

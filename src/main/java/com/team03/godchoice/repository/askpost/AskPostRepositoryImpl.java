package com.team03.godchoice.repository.askpost;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team03.godchoice.domain.askpost.AskPost;
import com.team03.godchoice.dto.responseDto.askpost.AskPostAllResDto;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import static com.team03.godchoice.domain.askpost.QAskPost.askPost;

import java.util.ArrayList;
import java.util.List;

@Repository
public class AskPostRepositoryImpl extends QuerydslRepositorySupport {

    private final JPAQueryFactory queryFactory;

    public AskPostRepositoryImpl(JPAQueryFactory queryFactory) {
        super(AskPost.class);
        this.queryFactory = queryFactory;
    }

    public Object searchAskPost(String sort, String search, Pageable pageable) {

        List<AskPost> askPostList = queryFactory
                .selectFrom(askPost)
                .where(askPost.title.contains(search)
                        .or(askPost.content.contains(search)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(listSort(sort), askPost.askPostId.desc())
                .fetch();

        return toPage(askPostList);

    }

    public OrderSpecifier<?> listSort(String sort) {
        if ("최신순".equals(sort)) {
            return new OrderSpecifier<>(Order.DESC, askPost.askPostId);
        }
        return new OrderSpecifier<>(Order.DESC, askPost.viewCount);
    }

    private PageImpl toPage(List<AskPost> askPostList) {
        List<AskPostAllResDto> askPostAllResDtos = new ArrayList<>();
        for (AskPost askPost : askPostList) {
            askPostAllResDtos.add(AskPostAllResDto.toAPARD(askPost));
        }
        return new PageImpl<>(askPostAllResDtos);
    }
}

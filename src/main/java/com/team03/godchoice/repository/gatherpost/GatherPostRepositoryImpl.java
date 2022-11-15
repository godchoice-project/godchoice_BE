package com.team03.godchoice.repository.gatherpost;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team03.godchoice.domain.eventpost.EventPost;
import com.team03.godchoice.domain.gatherPost.GatherPost;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class GatherPostRepositoryImpl extends QuerydslRepositorySupport {

    private final JPAQueryFactory queryFactory;

    public GatherPostRepositoryImpl(JPAQueryFactory queryFactory) {
        super(GatherPost.class);
        this.queryFactory = queryFactory;
    }

    public Object searchGatherPost(List<String> tag, String progress, String sort, String search, Pageable pageable) {
        return null;
    }
}

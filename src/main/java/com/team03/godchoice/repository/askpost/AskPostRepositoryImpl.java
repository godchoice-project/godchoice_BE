package com.team03.godchoice.repository.askpost;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team03.godchoice.abstrctPackage.MakeRegionTagClass;
import com.team03.godchoice.domain.Member;
import com.team03.godchoice.domain.askpost.AskPost;
import com.team03.godchoice.dto.responseDto.askpost.AskPostAllResDto;
import com.team03.godchoice.interfacepackage.SearchInterface;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import static com.team03.godchoice.domain.askpost.QAskPost.askPost;

import java.util.ArrayList;
import java.util.List;

@Repository
public class AskPostRepositoryImpl extends MakeRegionTagClass implements SearchInterface {

    private final JPAQueryFactory queryFactory;
    private final AskPostLikeRepository askPostLikeRepository;

    public AskPostRepositoryImpl(JPAQueryFactory queryFactory, AskPostLikeRepository askPostLikeRepository) {
        this.queryFactory = queryFactory;
        this.askPostLikeRepository = askPostLikeRepository;
    }

    public PageImpl<?>  searchPost(List<String> tag, String progress,String sort, String search, Pageable pageable, Member member) {

        List<AskPost> askPostList = queryFactory
                .selectFrom(askPost)
                .where(searchKeyword(search))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(listSort(sort), askPost.askPostId.desc())
                .fetch();

        return toPage(askPostList,member);

    }

    public OrderSpecifier<?> listSort(String sort) {
        if ("최신순".equals(sort)) {
            return new OrderSpecifier<>(Order.DESC, askPost.askPostId);
        }
        return new OrderSpecifier<>(Order.DESC, askPost.viewCount);
    }

    public BooleanExpression searchKeyword(String search){
        if(search==null){
            return null;
        }else{
            return askPost.title.contains(search)
                    .or(askPost.content.contains(search));
        }
    }

    private PageImpl<?> toPage(List<AskPost> askPostList,Member member) {
        List<AskPostAllResDto> askPostAllResDtos = new ArrayList<>();
        for (AskPost askPost : askPostList) {
            boolean bookmark;
            if(member!=null){
                bookmark = askPostLikeRepository.existsByMemberAndAskPost(member,askPost);
            }else{
                bookmark = false;
            }
            askPostAllResDtos.add(AskPostAllResDto.toAPARD(askPost,bookmark));
        }
        return new PageImpl<>(askPostAllResDtos);
    }
}

package com.team03.godchoice.interfacepackage;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.team03.godchoice.domain.Member;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SearchInterface {

    PageImpl<?> searchPost(List<String> tag, String progress, String sort, String search, Pageable pageable, Member member);

    BooleanExpression searchKeyword(String search);

    OrderSpecifier<?> listSort(String sort);

}

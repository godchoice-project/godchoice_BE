package com.team03.godchoice.interfacepackage;

import com.team03.godchoice.domain.Member;
import com.team03.godchoice.dto.GlobalResDto;
import com.team03.godchoice.exception.CustomException;
import com.team03.godchoice.exception.ErrorCode;
import com.team03.godchoice.repository.MemberRepository;
import com.team03.godchoice.security.jwt.UserDetailsImpl;

import java.time.LocalDate;

public interface PostInterface {
    GlobalResDto<?> createPost();

    GlobalResDto<?> putPost();

    GlobalResDto<?> deletePost();

    GlobalResDto<?> getOnePost();

    default Member isPresentMember(UserDetailsImpl userDetails, MemberRepository memberRepository){
        return memberRepository.findById(userDetails.getMember().getMemberId())
                .orElseThrow(()->new CustomException(ErrorCode.NOT_FOUND_MEMBER));
    }

    default String toPostStatus(LocalDate endPeriod){
        if(endPeriod.isBefore(LocalDate.now())){
            return "마감";
        }
        return "진행중";
    }

}

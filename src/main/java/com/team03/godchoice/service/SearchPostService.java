package com.team03.godchoice.service;

import com.team03.godchoice.domain.Member;
import com.team03.godchoice.dto.GlobalResDto;
import com.team03.godchoice.exception.CustomException;
import com.team03.godchoice.exception.ErrorCode;
import com.team03.godchoice.repository.MemberRepository;
import com.team03.godchoice.repository.askpost.AskPostRepositoryImpl;
import com.team03.godchoice.repository.eventpost.EventPostRepositoryImpl;
import com.team03.godchoice.repository.gatherpost.GatherPostRepositoryImpl;
import com.team03.godchoice.security.jwt.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SearchPostService {

    private final MemberRepository memberRepository;
    private final EventPostRepositoryImpl eventPostImplRepository;
    private final GatherPostRepositoryImpl gatherPostImplRepository;
    private final AskPostRepositoryImpl askPostImplRepository;

    public GlobalResDto<?> searchPost(String main, List<String> tag, String progress, String sort, String search, Pageable pageable, UserDetailsImpl userDetails) {

        Member member;
        if(userDetails!=null){
            member = isPresentMember(userDetails);
            if (member == null) {
                throw new CustomException(ErrorCode.NOT_FOUND_MEMBER);
            }
        }else{
            member = null;
        }

        if(main.equals("event")){  //행사글 찾는 곳
            return GlobalResDto.success(eventPostImplRepository.searchEventPost(tag,progress,sort,search,pageable,member),null) ;
        }else if(main.equals("gather")){  //모집글 찾는곳
            return GlobalResDto.success(gatherPostImplRepository.searchGatherPost(tag,progress,sort,search,pageable,member),null);
        }else{  //질문글 찾는곳
            return GlobalResDto.success(askPostImplRepository.searchAskPost(sort,search,pageable,member),null);
        }
    }

    public Member isPresentMember(UserDetailsImpl userDetails) {
        Optional<Member> member = memberRepository.findById(userDetails.getAccount().getMemberId());
        return member.orElse(null);
    }


}

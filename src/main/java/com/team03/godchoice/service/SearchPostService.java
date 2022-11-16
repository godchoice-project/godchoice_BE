package com.team03.godchoice.service;

import com.team03.godchoice.dto.GlobalResDto;
import com.team03.godchoice.repository.askpost.AskPostRepositoryImpl;
import com.team03.godchoice.repository.eventpost.EventPostRepositoryImpl;
import com.team03.godchoice.repository.gatherpost.GatherPostRepositoryImpl;
import com.team03.godchoice.security.jwt.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchPostService {

    private final EventPostRepositoryImpl eventPostImplRepository;
    private final GatherPostRepositoryImpl gatherPostImplRepository;
    private final AskPostRepositoryImpl askPostImplRepository;

    public GlobalResDto<?> searchPost(String main, List<String> tag, String progress, String sort, String search, Pageable pageable, UserDetailsImpl userDetails) {
        if(userDetails!=null){
            //토큰있고 지역 저장했으면 지역으로 보여줌
            //토큰없으면 기본
        }

        if(main.equals("event")){  //행사글 찾는 곳
            return GlobalResDto.success(eventPostImplRepository.searchEventPost(tag,progress,sort,search,pageable),null) ;
        }else if(main.equals("gather")){  //모집글 찾는곳
            return GlobalResDto.success(gatherPostImplRepository.searchGatherPost(tag,progress,sort,search,pageable),null);
        }else{  //질문글 찾는곳
            return GlobalResDto.success(askPostImplRepository.searchAskPost(sort,search,pageable),null);
        }
    }


}

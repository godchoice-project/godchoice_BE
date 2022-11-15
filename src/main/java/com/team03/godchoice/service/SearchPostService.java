package com.team03.godchoice.service;

import com.team03.godchoice.domain.gatherPost.GatherPost;
import com.team03.godchoice.dto.GlobalResDto;
import com.team03.godchoice.repository.askpost.AskPostRepository;
import com.team03.godchoice.repository.eventpost.EventPostRepository;
import com.team03.godchoice.repository.eventpost.EventPostRepositoryImpl;
import com.team03.godchoice.repository.gatherpost.GatherPostRepository;
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

    public GlobalResDto<?> searchPost(String main, List<String> tag, String progress, String sort, String search, Pageable pageable, UserDetailsImpl userDetails) {
        if(main.equals("event")){  //행사글 찾는 곳
            return GlobalResDto.success(eventPostImplRepository.searchEventPost(tag,progress,sort,search,pageable),null) ;
        }else if(main.equals("gather")){  //모집글 찾는곳
            return GlobalResDto.success(gatherPostImplRepository.searchGatherPost(tag,progress,sort,search,pageable),null);
        }else{  //질문글 찾는곳

        }
        return null;
    }


}

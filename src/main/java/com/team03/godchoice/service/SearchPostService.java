package com.team03.godchoice.service;

import com.team03.godchoice.dto.GlobalResDto;
import com.team03.godchoice.repository.askpost.AskPostRepository;
import com.team03.godchoice.repository.eventpost.EventPostRepository;
import com.team03.godchoice.repository.gatherpost.GatherPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

@Service
@RequiredArgsConstructor
public class SearchPostService {

    private final EventPostRepository eventPostRepository;
    private final GatherPostRepository gatherPostRepository;
    private final AskPostRepository askPostRepository;

    public GlobalResDto<?> searchPost(String main, String tag, String progress, String sort, String search, Pageable pageable) {
        if(main.equals("event")){  //행사글 찾는 곳

        }else if(main.equals("gather")){  //모집글 찾는곳

        }else{  //질문글 찾는곳

        }
        return null;
    }


}

package com.team03.godchoice.dto.responseDto.mypageResDto;

import com.team03.godchoice.dto.responseDto.askpost.AskPostAllResDto;
import com.team03.godchoice.dto.responseDto.eventpost.EventPostAllResDto;
import com.team03.godchoice.dto.responseDto.gatherpost.GatherPostAllResDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MyPageMyPostResDto {
    private List<EventPostAllResDto> eventPost;
    private List<GatherPostAllResDto> gatherPost;
    private List<AskPostAllResDto> askPost;
}

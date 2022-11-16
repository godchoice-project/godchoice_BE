package com.team03.godchoice.dto.responseDto;

import com.team03.godchoice.domain.askpost.AskPost;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AskPostAllResDto {
    private String title;
    private String content;

    public static AskPostAllResDto toAPARD(AskPost askPost) {
        return new AskPostAllResDto(
                askPost.getTitle(),
                askPost.getContent()
        );
    }
}

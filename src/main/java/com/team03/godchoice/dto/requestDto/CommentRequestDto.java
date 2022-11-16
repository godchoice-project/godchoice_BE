package com.team03.godchoice.dto.requestDto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class CommentRequestDto {

    private String postName;

    @NotBlank(message = "댓글을 작성해주세요!")
    private String content;

    private Long parentId;

}

package com.team03.godchoice.dto.responseDto.mypageResDto;

import com.team03.godchoice.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MyPageUserResDto {
    private String email;
    private String nickName;
    private String  addressTag;
    private String userImg;

    public MyPageUserResDto(Member member) {
        this.email = member.getEmail();
        this.nickName = member.getUserName();
        this.addressTag = member.getUserAddressTag().getRegion();
        this.userImg = member.getUserImgUrl();
    }
}

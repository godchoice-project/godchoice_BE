package com.team03.godchoice.dto.responseDto.mypageResDto;

import com.team03.godchoice.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MyPageUserResDto {
    private Long userId;
    private String domain;
    private String email;
    private String nickName;
    private String  addressTag;
    private String userImg;

    public MyPageUserResDto(Member member,String[] userEmail) {
        this.userId = member.getMemberId();
        this.domain = userEmail[0];
        this.email = userEmail[1];
        this.nickName = member.getUserName();
        this.addressTag = regionTag(member);
        this.userImg = member.getUserImgUrl();
    }

    public String regionTag(Member member){
        if(member.getUserAddressTag()==null){
            return null;
        }else{
            return member.getUserAddressTag().getRegion();
        }
    }
}

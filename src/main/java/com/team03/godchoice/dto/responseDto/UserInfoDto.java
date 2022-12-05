package com.team03.godchoice.dto.responseDto;

import com.team03.godchoice.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDto {

    private Long userId;
    private String userAddressTag;
    private String userImgUrl;
    private String role;

    private Long notificationNum;

    public UserInfoDto(Member member,Long notificationNum) {
        this.userId = member.getMemberId();
        this.userAddressTag = toAddressTag(member);
        this.userImgUrl = member.getUserImgUrl();
        this.role = member.getRole().toString();
        this.notificationNum = notificationNum;
    }

    public String toAddressTag(Member member){
        if(member.getUserAddressTag()==null){
            return null;
        }else{
            return member.getUserAddressTag().getRegion();
        }
    }
}

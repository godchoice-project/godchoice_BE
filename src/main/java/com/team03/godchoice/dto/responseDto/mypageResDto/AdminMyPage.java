package com.team03.godchoice.dto.responseDto.mypageResDto;

import com.team03.godchoice.adminPage.AdminPage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AdminMyPage {
    private MyPageUserResDto myPageUserResDto;
    private List<AdminPage> adminPage;

}

package com.team03.godchoice.interfacepackage;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.team03.godchoice.domain.Member;
import com.team03.godchoice.domain.RefreshToken;
import com.team03.godchoice.dto.GlobalResDto;
import com.team03.godchoice.dto.TokenDto;
import com.team03.godchoice.dto.responseDto.UserInfoDto;
import com.team03.godchoice.dto.social.SocialUserInfoDto;
import com.team03.godchoice.exception.CustomException;
import com.team03.godchoice.exception.ErrorCode;
import com.team03.godchoice.repository.RefreshTokenRepository;
import com.team03.godchoice.security.jwt.JwtUtil;
import com.team03.godchoice.security.jwt.UserDetailsImpl;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

public interface LoginInterface {

    default GlobalResDto<?> login(String code, String  state ,HttpServletResponse response, RefreshTokenRepository refreshTokenRepository) throws JsonProcessingException {

        //인가 코드로 토큰 발급
        String accessToken = issuedAccessToken(code,state);

        //토큰을 통해 사용자 정보 가져오기
        SocialUserInfoDto socialUserInfoDto = getUserInfo(accessToken);

        //사용자정보 토대로 가입진행
        Member member = saveMember(socialUserInfoDto);

        //강제로그인
        forceLoginUser(member);

        //토큰발급후 응답
        createToken(member, response,refreshTokenRepository);

        //userInfoDto에 담기
        UserInfoDto userInfoDto = new UserInfoDto(member);

        //반환
        return GlobalResDto.success(userInfoDto, "로그인 완료");
    }

    String issuedAccessToken(String code,String state) throws JsonProcessingException;

    SocialUserInfoDto getUserInfo(String accessToken) throws JsonProcessingException ;

    Member saveMember(SocialUserInfoDto socialUserInfoDto);

    default void forceLoginUser(Member member) {
        UserDetails userDetails = new UserDetailsImpl(member);
        if (member.getIsDeleted().equals(true)) {
            throw new CustomException(ErrorCode.DELETED_USER_EXCEPTION);
        }
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    default void createToken(Member member, HttpServletResponse response,RefreshTokenRepository refreshTokenRepository) {
        TokenDto tokenDto = JwtUtil.createAllToken(member.getEmail());

        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByAccountEmail(member.getEmail());

        if (refreshToken.isPresent()) {
            refreshTokenRepository.save(refreshToken.get().updateToken(tokenDto.getRefreshToken()));
        } else {
            RefreshToken newToken = new RefreshToken(tokenDto.getRefreshToken(), member.getEmail());
            refreshTokenRepository.save(newToken);
        }

        setHeader(response, tokenDto);
    }

    default void setHeader(HttpServletResponse response, TokenDto tokenDto){
        response.addHeader(JwtUtil.ACCESS_TOKEN, tokenDto.getAccessToken());
        response.addHeader(JwtUtil.REFRESH_TOKEN, tokenDto.getRefreshToken());
    }

}

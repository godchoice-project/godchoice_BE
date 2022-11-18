package com.team03.godchoice.interfacepackage;

import com.team03.godchoice.domain.Member;
import com.team03.godchoice.dto.TokenDto;
import com.team03.godchoice.exception.CustomException;
import com.team03.godchoice.exception.ErrorCode;
import com.team03.godchoice.security.jwt.JwtUtil;
import com.team03.godchoice.security.jwt.UserDetailsImpl;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.http.HttpServletResponse;

public interface LoginInterface {

    default void forceLoginUser(Member member) {
        UserDetails userDetails = new UserDetailsImpl(member);
        if (member.getIsDeleted().equals(true)) {
            throw new CustomException(ErrorCode.DELETED_USER_EXCEPTION);
        }
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    default void createToken(Member member, HttpServletResponse response){
//        TokenDto tokenDto = jwtUtil.createAllToken(member.getEmail());
//
//        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByAccountEmail(member.getEmail());
//
//        if (refreshToken.isPresent()) {
//            refreshTokenRepository.save(refreshToken.get().updateToken(tokenDto.getRefreshToken()));
//        } else {
//            RefreshToken newToken = new RefreshToken(tokenDto.getRefreshToken(), member.getEmail());
//            refreshTokenRepository.save(newToken);
//        }
//
//        setHeader(response, tokenDto);
    }

    default void setHeader(HttpServletResponse response, TokenDto tokenDto) {
        response.addHeader(JwtUtil.ACCESS_TOKEN, tokenDto.getAccessToken());
        response.addHeader(JwtUtil.REFRESH_TOKEN, tokenDto.getRefreshToken());
    }
}

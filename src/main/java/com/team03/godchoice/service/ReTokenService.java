package com.team03.godchoice.service;

import com.team03.godchoice.domain.RefreshToken;
import com.team03.godchoice.dto.TokenDto;
import com.team03.godchoice.exception.CustomException;
import com.team03.godchoice.exception.ErrorCode;
import com.team03.godchoice.repository.RefreshTokenRepository;
import com.team03.godchoice.security.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReTokenService {

    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    public void createNewToken(String refreshToken, HttpServletResponse response) {
        Boolean valToken = jwtUtil.refreshTokenValidation(refreshToken);

        if(valToken){
            String userEmail =jwtUtil.getEmailFromToken(refreshToken);
            TokenDto tokenDto = jwtUtil.createAllToken(userEmail);

            RefreshToken oldRefreshToken = refreshTokenRepository.findByAccountEmail(userEmail)
                    .orElseThrow(()-> new CustomException(ErrorCode.ERROR));
            oldRefreshToken.change(tokenDto.getRefreshToken());

            refreshTokenRepository.save(oldRefreshToken);

            response.addHeader(JwtUtil.ACCESS_TOKEN, tokenDto.getAccessToken());
            response.addHeader(JwtUtil.REFRESH_TOKEN, tokenDto.getRefreshToken());

        }else{
            throw new CustomException(ErrorCode.ERROR_LOGIN);
        }

    }
}

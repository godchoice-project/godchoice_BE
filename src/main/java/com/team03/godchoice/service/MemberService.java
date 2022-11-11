//package com.team03.godchoice.service;
//
////import com.team03.godchoice.dto.responsedto.social.TokenFactory;
//import com.team03.godchoice.exception.CustomException;
//import com.team03.godchoice.exception.ErrorCode;
//import com.team03.godchoice.repository.RefreshTokenRepository;
//import com.team03.godchoice.security.jwt.JwtTokenProvider;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.security.sasl.AuthenticationException;
//
//
//@Service
//public class MemberService {
//
//    private JwtTokenProvider jwtTokenProvider;
//    private RefreshTokenRepository refreshTokenRepository;
//
////    private RedisService redisService;
//
//    public MemberService(JwtTokenProvider jwtTokenProvider, RefreshTokenRepository refreshTokenRepository) { //RedisService redisService
//        this.jwtTokenProvider = jwtTokenProvider;
//        this.refreshTokenRepository = refreshTokenRepository;
////        this.redisService = redisService;
//    }
//
//    /*public TokenFactory accessAndRefreshTokenProcess(String username, HttpServletResponse response) {
//        String refreshToken = jwtTokenProvider.createRefreshToken(username);
//        String token = jwtTokenProvider.createToken(username);
//
//        //        리프레시 토큰 HTTPonly
//        Cookie cookie = new Cookie("refreshToken", refreshToken);
//        cookie.setHttpOnly(true);
//        cookie.setPath("/");
//
//
//        response.addCookie(cookie);
//        response.setHeader("Authorization", token);
//        response.setHeader("Access-Token-Expire-Time", String.valueOf(30*60*1000L));
//
//        return new TokenFactory(token, refreshToken);
//    }*/
//
//    @Transactional
//    public RefreshTokenResponseDto refreshAccessToken(String refreshToken) throws AuthenticationException {
//        try {
//            String id = jwtTokenProvider.getPayload(refreshToken);
////            String compareToken = redisService.getValues(id);
//
//            /**
//             * 글로벌 예외처리 하기
//             */
////            if (!compareToken.equals(refreshToken)) {
////                throw new CustomException(ErrorCode.EXPIRE_REFRESH_TOKEN);
////            }
//
//            if (!jwtTokenProvider.validateRefreshToken(refreshToken)) {
//                throw new CustomException(ErrorCode.EXPIRE_REFRESH_TOKEN);
////                throw new AuthenticationException("refresh token이 유효하지 않습니다.111");
//            }
//
//            String newAccessToken = jwtTokenProvider.createToken(id);
//
//            return new RefreshTokenResponseDto(newAccessToken);
//        } catch (NullPointerException np) {
//            throw new CustomException(ErrorCode.NOT_EXIST_REFRESHTOKEN);
//        }
//    }
//}
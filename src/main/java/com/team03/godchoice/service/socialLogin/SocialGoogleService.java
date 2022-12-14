package com.team03.godchoice.service.socialLogin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.team03.godchoice.SSE.NotificationRepository;
import com.team03.godchoice.domain.Member;
import com.team03.godchoice.domain.RefreshToken;
import com.team03.godchoice.enumclass.Role;
import com.team03.godchoice.dto.GlobalResDto;
import com.team03.godchoice.dto.responseDto.UserInfoDto;
import com.team03.godchoice.dto.social.GoogleUserInfoDto;
import com.team03.godchoice.dto.TokenDto;
import com.team03.godchoice.interfacepackage.LoginInterface;
import com.team03.godchoice.repository.MemberRepository;
import com.team03.godchoice.repository.RefreshTokenRepository;
import com.team03.godchoice.security.jwt.JwtUtil;
import com.team03.godchoice.util.ComfortUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class SocialGoogleService implements LoginInterface {

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    String redirect_uri;
    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    String client_id;
    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    String clientSecret;

    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;

    private final RefreshTokenRepository refreshTokenRepository;
    private final ComfortUtils comfortUtils;
    private final PasswordEncoder passwordEncoder;
    private final NotificationRepository notificationRepository;

    public GlobalResDto<?> googleLogin(String code, HttpServletResponse response)
    throws JsonProcessingException {
        // 1. "????????????" ??? "????????? ??????" ??????
        String getAccessToken = getAccessToken(code);

        // 2. ???????????? ?????? API ??????
        GoogleUserInfoDto googleUserInfo = getGoogleUserInfo(getAccessToken);

        // 3.  ??????ID??? ???????????? ??????
        Member member = signupGoogleUser(googleUserInfo);

        //4. ?????? ????????? ??????
        forceLoginUser(member);

        //?????? ??????
        TokenDto tokenDto = jwtUtil.createAllToken(member.getEmail());

        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByAccountEmail(member.getEmail());

        // ??????????????? ??? ???????????? ?????? ??????????
        if(refreshToken.isPresent()) {
            RefreshToken refreshToken1 = refreshToken.get().updateToken(tokenDto.getRefreshToken());
            refreshTokenRepository.save(refreshToken1);
        } else {
            RefreshToken newToken = new RefreshToken(tokenDto.getRefreshToken(), member.getEmail());
            refreshTokenRepository.save(newToken);
        }

        //????????? header??? ????????? ????????????????????? ????????????
        setHeader(response, tokenDto);

        Long notificationNum = notificationRepository.countUnReadStateNotifications(member.getMemberId());

        UserInfoDto userInfoDto = new UserInfoDto(member,notificationNum);

        return GlobalResDto.success(userInfoDto, "Success Google login");
    }

    //header ??? Content-type ??????
    public String getAccessToken(String code) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        //HTTP Body ??????
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", client_id);
        body.add("client_secret", clientSecret);
        body.add("redirect_uri", redirect_uri);
        body.add("code", code);


        //HTTP ?????? ?????????
        HttpEntity<MultiValueMap<String, String>> googleTokenRequest =
                new HttpEntity<>(body, headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://oauth2.googleapis.com/token",
                HttpMethod.POST,
                googleTokenRequest,
                String.class
        );

        //HTTP ?????? (JSON) -> ????????? ?????? ??????
        //JSON -> JsonNode ????????? ??????
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);

        String accessToken = jsonNode.get("access_token").asText();
//        String refreshToken = jsonNode.get("refresh_token").asText();
//        String refreshToken = null;
//        return new TokenFactory(accessToken, refreshToken);
        return accessToken;
    }

    public GoogleUserInfoDto getGoogleUserInfo(String accessToken) throws JsonProcessingException {
        // HTTP Header ??????
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        // HTTP ?????? ?????????
        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://openidconnect.googleapis.com/v1/userinfo",
                HttpMethod.POST,
                kakaoUserInfoRequest,
                String.class
        );
        //HTTP ?????? (JSON)
        //JSON -> JsonNode ????????? ??????
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        String id = jsonNode.get("sub").asText();

        String userEmail = jsonNode.get("email").asText();

        String userName = jsonNode.get("name").asText();
        return new GoogleUserInfoDto(id, userName, userEmail);
    }

    private Member signupGoogleUser(GoogleUserInfoDto googleUserInfoDto) {
        // ????????? ??????
        // DB ??? ????????? Google Id ??? ????????? ??????
        Member findGoogle = memberRepository.findByEmail("g_"+googleUserInfoDto.getUserEmail()).orElse(null);


        //DB??? ????????? ????????? ????????? ???????????? ??????
        if (findGoogle == null) {

            String email = googleUserInfoDto.getUserEmail();
            String password = UUID.randomUUID().toString();

            Member googleMember = Member.builder()
                    .email("g_" + email)
                    .userName(comfortUtils.makeUserNickName())
                    .userImgUrl("https://eunibucket.s3.ap-northeast-2.amazonaws.com/testdir/normal_user_img.png")//????????????
                    .pw(passwordEncoder.encode(password))
                    .isAccepted(false)
                    .isDeleted(false)
                    .role(Role.USER)
                    .build();
            memberRepository.save(googleMember);

            return googleMember;
        }

        return findGoogle;
    }

//    public void forceLoginUser(Member googleMember) {
//        UserDetails userDetails = new UserDetailsImpl(googleMember);
//        if (googleMember.getIsDeleted().equals(true)) {
//            throw new CustomException(ErrorCode.DELETED_USER_EXCEPTION);
//        }
//        Authentication authentication =
//                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//    }
//
//    private void setHeader(HttpServletResponse response, TokenDto tokenDto) {
//        response.addHeader(JwtUtil.ACCESS_TOKEN, tokenDto.getAccessToken());
//        response.addHeader(JwtUtil.REFRESH_TOKEN, tokenDto.getRefreshToken());
//    }
}
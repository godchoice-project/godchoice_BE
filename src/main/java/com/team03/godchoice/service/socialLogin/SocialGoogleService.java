package com.team03.godchoice.service.socialLogin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.team03.godchoice.domain.Member;
import com.team03.godchoice.dto.GlobalResDto;
import com.team03.godchoice.dto.social.SocialUserInfoDto;
import com.team03.godchoice.enumclass.Role;
import com.team03.godchoice.interfacepackage.LoginInterface;
import com.team03.godchoice.repository.MemberRepository;
import com.team03.godchoice.repository.RefreshTokenRepository;
import com.team03.godchoice.util.ComfortUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
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

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final ComfortUtils comfortUtils;

    public GlobalResDto<?> loginService(String code, HttpServletResponse response) throws JsonProcessingException {
        return login(code, null, response, refreshTokenRepository);
    }

    @Override
    public GlobalResDto<?> login(String code, String state, HttpServletResponse response, RefreshTokenRepository refreshTokenRepository) throws JsonProcessingException {
        return LoginInterface.super.login(code, null, response, refreshTokenRepository);
    }

    @Override
    public String issuedAccessToken(String code, String state) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        //HTTP Body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", client_id);
        body.add("client_secret", clientSecret);
        body.add("redirect_uri", redirect_uri);
        body.add("code", code);


        //HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> googleTokenRequest =
                new HttpEntity<>(body, headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://oauth2.googleapis.com/token",
                HttpMethod.POST,
                googleTokenRequest,
                String.class
        );

        //HTTP 응답 (JSON) -> 액세스 토큰 파싱
        //JSON -> JsonNode 객체로 변환
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);

        return jsonNode.get("access_token").asText();
    }

    @Override
    public SocialUserInfoDto getUserInfo(String accessToken) throws JsonProcessingException {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://openidconnect.googleapis.com/v1/userinfo",
                HttpMethod.POST,
                kakaoUserInfoRequest,
                String.class
        );
        //HTTP 응답 (JSON)
        //JSON -> JsonNode 객체로 변환
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        String id = jsonNode.get("sub").asText();

        String userEmail = jsonNode.get("email").asText();

        String userName = jsonNode.get("name").asText();

        return new SocialUserInfoDto(id,userName,userEmail,"https://eunibucket.s3.ap-northeast-2.amazonaws.com/testdir/normal_user_img.png");
    }

    @Override
    public Member saveMember(SocialUserInfoDto socialUserInfoDto) {
        Member findGoogle = memberRepository.findByEmail("g_"+socialUserInfoDto.getEmail()).orElse(null);

        //DB에 중복된 계정이 없으면 회원가입 처리
        if (findGoogle == null) {

            Member googleMember = Member.builder()
                    .email("g_" + socialUserInfoDto.getEmail())
                    .userName(comfortUtils.makeUserNickName())
                    .userImgUrl(socialUserInfoDto.getUserImgUrl())
                    .pw(passwordEncoder.encode(UUID.randomUUID().toString()))
                    .isAccepted(false)
                    .isDeleted(false)
                    .role(Role.USER)
                    .build();
            memberRepository.save(googleMember);

            return googleMember;
        }

        return findGoogle;
    }
}

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
public class SocialGithubService implements LoginInterface {

    @Value("${spring.security.oauth2.client.registration.github.client-id}")
    private String githubClientId;
    @Value("${spring.security.oauth2.client.registration.github.client-secret}")
    private String githubCClientSecret;
    @Value("${spring.security.oauth2.client.registration.github.redirect-uri}")
    private String redirect_uri;

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    public GlobalResDto<?> loginService(String code, HttpServletResponse response) throws JsonProcessingException {
        return login(code, null, response, refreshTokenRepository);
    }

    @Override
    public GlobalResDto<?> login(String code, String state, HttpServletResponse response, RefreshTokenRepository refreshTokenRepository) throws JsonProcessingException {
        return LoginInterface.super.login(code, state, response, refreshTokenRepository);
    }

    @Override
    public String issuedAccessToken(String code, String state) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept","application/json");

        // HTTP Body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("client_id", githubClientId);
        body.add("redirect_uri",redirect_uri);
        body.add("code", code);
        body.add("client_secret",githubCClientSecret);

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
                new HttpEntity<>(body,headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://github.com/login/oauth/access_token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        // HTTP 응답 (JSON) -> 액세스 토큰 파싱
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);

        return jsonNode.get("access_token").asText();
    }

    @Override
    public SocialUserInfoDto getUserInfo(String accessToken) throws JsonProcessingException {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept","application/vnd.github+json");
        headers.add("Authorization", "Bearer " + accessToken);

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> githubUserInfoRequest = new HttpEntity<>(headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://api.github.com/user",
                HttpMethod.GET,
                githubUserInfoRequest,
                String.class
        );

        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);

        Long id = jsonNode.get("id").asLong();
        String nickname = jsonNode.get("name").asText();
        String email = jsonNode.get("email").asText();
        String userImgUrl = jsonNode.get("avatar_url").asText();

        return new SocialUserInfoDto(id.toString(), nickname, email, userImgUrl);
    }

    @Override
    public Member saveMember(SocialUserInfoDto socialUserInfoDto) {
        Member kakaoMember = memberRepository.findByEmail("git_"+socialUserInfoDto.getId()+"@git.com").orElse(null);

        //없다면 저장
        if (kakaoMember == null) {
            Member member = Member.builder().
                    email("git_"+socialUserInfoDto.getId()+"@git.com")
                    .userName(socialUserInfoDto.getNickname())
                    .userImgUrl(socialUserInfoDto.getUserImgUrl())
                    .pw(passwordEncoder.encode(UUID.randomUUID().toString()))
                    .isAccepted(false)
                    .isDeleted(false)
                    .role(Role.USER)
                    .build();

            memberRepository.save(member);
            return member;
        }

        //있다면 member 반환
        return kakaoMember;
    }
}

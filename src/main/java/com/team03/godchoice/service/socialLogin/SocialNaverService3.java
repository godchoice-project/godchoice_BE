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
public class SocialNaverService3 implements LoginInterface {

    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String client_id;
    @Value("${spring.security.oauth2.client.registration.naver.client-secret}")
    private String client_secret;

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    public GlobalResDto<?> loginService(String code, String state, HttpServletResponse response) throws JsonProcessingException {
        return login(code, state, response, refreshTokenRepository);
    }

    @Override
    public GlobalResDto<?> login(String code, String state, HttpServletResponse response, RefreshTokenRepository refreshTokenRepository) throws JsonProcessingException {
        return LoginInterface.super.login(code, state, response, refreshTokenRepository);
    }

    @Override
    public String issuedAccessToken(String code, String state) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", client_id);
        params.add("client_secret", client_secret);
        params.add("code", code);
        params.add("state", state);


        HttpEntity<MultiValueMap<String, String>> naverTokenRequest =
                new HttpEntity<>(params, headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> naverResponse = rt.exchange(
                "https://nid.naver.com/oauth2.0/token",
                HttpMethod.POST,
                naverTokenRequest,
                String.class
        );

        String responseBody = naverResponse.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        return jsonNode.get("access_token").asText();
    }

    @Override
    public SocialUserInfoDto getUserInfo(String accessToken) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> naverUserInfoRequest = new HttpEntity<>(headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://openapi.naver.com/v1/nid/me",
                HttpMethod.POST,
                naverUserInfoRequest,
                String.class
        );

        String responseBody = response.getBody();
        ObjectMapper objectMapper2 = new ObjectMapper();
        JsonNode jsonNode2 = objectMapper2.readTree(responseBody);

        Long id = jsonNode2.get("response").get("id").asLong();
        String profileImage = jsonNode2.get("response").get("profile_image").asText();
        String email = jsonNode2.get("response").get("email").asText();
        String name = jsonNode2.get("response").get("name").asText();

        return new SocialUserInfoDto(id.toString(), name, email, profileImage);
    }

    @Override
    public Member saveMember(SocialUserInfoDto socialUserInfoDto) {
        Member naverMember = memberRepository.findByEmail("n_" + socialUserInfoDto.getEmail()).orElse(null);

        if (naverMember == null) {
            Member member = Member.builder()
                    .email("n_" + socialUserInfoDto.getEmail())
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
        return naverMember;
    }
}

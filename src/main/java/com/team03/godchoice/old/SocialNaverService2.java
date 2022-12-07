//package com.team03.godchoice.service.socialLogin;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.team03.godchoice.SSE.NotificationRepository;
//import com.team03.godchoice.domain.Member;
//import com.team03.godchoice.domain.RefreshToken;
//import com.team03.godchoice.dto.GlobalResDto;
//import com.team03.godchoice.dto.TokenDto;
//import com.team03.godchoice.dto.responseDto.UserInfoDto;
//import com.team03.godchoice.dto.social.SocialUserInfoDto;
//import com.team03.godchoice.enumclass.Role;
//import com.team03.godchoice.interfacepackage.LoginInterface;
//import com.team03.godchoice.repository.MemberRepository;
//import com.team03.godchoice.repository.RefreshTokenRepository;
//import com.team03.godchoice.security.jwt.JwtUtil;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//import org.springframework.util.LinkedMultiValueMap;
//import org.springframework.util.MultiValueMap;
//import org.springframework.web.client.RestTemplate;
//
//import javax.servlet.http.HttpServletResponse;
//import java.util.Optional;
//import java.util.UUID;
//
//@RequiredArgsConstructor
//@Service
//public class SocialNaverService2 implements LoginInterface {
//
//    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
//    private String client_id;
//    @Value("${spring.security.oauth2.client.registration.naver.client-secret}")
//    private String client_secret;
//    @Value("${spring.security.oauth2.client.provider.naver.user-info-uri}")
//    private String user_info_url;
//    @Value("${spring.security.oauth2.client.registration.naver.redirect-uri}")
//    private String redirect_uri;
//    @Value("${spring.security.oauth2.client.provider.naver.token-uri}")
//    private String token_uri;
//
//    private final PasswordEncoder passwordEncoder;
//    private final MemberRepository memberRepository;
//    private final JwtUtil jwtUtil;
//    private final RefreshTokenRepository refreshTokenRepository;
//    private final NotificationRepository notificationRepository;
//
//
//    public GlobalResDto<?> naverLogin(String code, String state, HttpServletResponse response) throws JsonProcessingException {
//        //인가코드와 state를 통해 access_token발급받기
//        String accessToken = issuedAccessToken(code, state);
//
//        //access_token을 이용해 사용자 정보 가져오기(response.profile_image,response.email, response.name)
//        SocialUserInfoDto socialUserInfoDto = getNaverUserInfo(accessToken);
//
//        //사용자정보를 토대로 가입진행
//        Member member = saveMember(socialUserInfoDto);
//
//        //강제 로그인
//        forceLoginUser(member);
//
//        //토큰발급후 response
//        createToken(member,response);
//
//        Long notificationNum = notificationRepository.countUnReadStateNotifications(member.getMemberId());
//
//        UserInfoDto userInfoDto = new UserInfoDto(member);
//        return GlobalResDto.success(userInfoDto,"로그인이 완료되었습니다");
//    }
//
//    private String issuedAccessToken(String code, String state) throws JsonProcessingException {
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
//
//        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//        params.add("grant_type", "authorization_code");
//        params.add("client_id", client_id);
//        params.add("client_secret", client_secret);
//        params.add("code", code);
//        params.add("state", state);
//
//
//        HttpEntity<MultiValueMap<String, String>> naverTokenRequest =
//                new HttpEntity<>(params, headers);
//        RestTemplate rt = new RestTemplate();
//        ResponseEntity<String> naverResponse = rt.exchange(
//                token_uri,
//                HttpMethod.POST,
//                naverTokenRequest,
//                String.class
//        );
//
//        String responseBody = naverResponse.getBody();
//        ObjectMapper objectMapper = new ObjectMapper();
//        JsonNode jsonNode = objectMapper.readTree(responseBody);
//        return jsonNode.get("access_token").asText();
//    }
//
//    private SocialUserInfoDto getNaverUserInfo(String accessToken) throws JsonProcessingException {
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Authorization", "Bearer " + accessToken);
//        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
//
//        // HTTP 요청 보내기
//        HttpEntity<MultiValueMap<String, String>> naverUserInfoRequest = new HttpEntity<>(headers);
//        RestTemplate rt = new RestTemplate();
//        ResponseEntity<String> response = rt.exchange(
//                user_info_url,
//                HttpMethod.POST,
//                naverUserInfoRequest,
//                String.class
//        );
//
//        String responseBody = response.getBody();
//        ObjectMapper objectMapper2 = new ObjectMapper();
//        JsonNode jsonNode2 = objectMapper2.readTree(responseBody);
//
//        Long id = jsonNode2.get("response").get("id").asLong();
//        String profileImage = jsonNode2.get("response").get("profile_image").asText();
//        String email = jsonNode2.get("response").get("email").asText();
//        String name = jsonNode2.get("response").get("name").asText();
//
//        return new SocialUserInfoDto(id, name, email, profileImage);
//    }
//
//    private Member saveMember(SocialUserInfoDto socialUserInfoDto) {
//
//        Member naverMember = memberRepository.findByEmail("n_" + socialUserInfoDto.getEmail()).orElse(null);
//
//        if (naverMember == null) {
//            Role role;
//            if (memberRepository.findAll().isEmpty()) {
//                role = Role.ADMIN;
//            } else {
//                role = Role.USER;
//            }
//
//
//            Member member = Member.builder()
//                    .email("n_" + socialUserInfoDto.getEmail())
//                    .userName(socialUserInfoDto.getNickname())
//                    .userImgUrl(socialUserInfoDto.getUserImgUrl())
//                    .pw(passwordEncoder.encode(UUID.randomUUID().toString()))
//                    .isAccepted(false)
//                    .isDeleted(false)
//                    .role(role)
//                    .build();
//
//            memberRepository.save(member);
//            return member;
//        }
//        return naverMember;
//    }
//
//    public void createToken(Member member,HttpServletResponse response){
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
//    }
//}

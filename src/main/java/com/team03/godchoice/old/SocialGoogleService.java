//package com.team03.godchoice.service.socialLogin;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.team03.godchoice.SSE.NotificationRepository;
//import com.team03.godchoice.domain.Member;
//import com.team03.godchoice.domain.RefreshToken;
//import com.team03.godchoice.enumclass.Role;
//import com.team03.godchoice.dto.GlobalResDto;
//import com.team03.godchoice.dto.responseDto.UserInfoDto;
//import com.team03.godchoice.dto.social.GoogleUserInfoDto;
//import com.team03.godchoice.dto.TokenDto;
//import com.team03.godchoice.interfacepackage.LoginInterface;
//import com.team03.godchoice.repository.MemberRepository;
//import com.team03.godchoice.repository.RefreshTokenRepository;
//import com.team03.godchoice.security.jwt.JwtUtil;
//import com.team03.godchoice.util.ComfortUtils;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.*;
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
//public class SocialGoogleService implements LoginInterface {
//
//    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
//    String redirect_uri;
//    @Value("${spring.security.oauth2.client.registration.google.client-id}")
//    String client_id;
//    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
//    String clientSecret;
//
//    private final JwtUtil jwtUtil;
//    private final MemberRepository memberRepository;
//
//    private final RefreshTokenRepository refreshTokenRepository;
//    private final ComfortUtils comfortUtils;
//    private final PasswordEncoder passwordEncoder;
//    private final NotificationRepository notificationRepository;
//
//    public GlobalResDto<?> googleLogin(String code, HttpServletResponse response)
//    throws JsonProcessingException {
//        // 1. "인가코드" 로 "액세스 토큰" 요청
//        String getAccessToken = getAccessToken(code);
//
//        // 2. 토큰으로 구글 API 호출
//        GoogleUserInfoDto googleUserInfo = getGoogleUserInfo(getAccessToken);
//
//        // 3.  구글ID로 회원가입 처리
//        Member member = signupGoogleUser(googleUserInfo);
//
//        //4. 강제 로그인 처리
//        forceLoginUser(member);
//
//        //토큰 발급
//        TokenDto tokenDto = jwtUtil.createAllToken(member.getEmail());
//
//        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByAccountEmail(member.getEmail());
//
//        // 로그아웃한 후 로그인을 다시 하는가?
//        if(refreshToken.isPresent()) {
//            RefreshToken refreshToken1 = refreshToken.get().updateToken(tokenDto.getRefreshToken());
//            refreshTokenRepository.save(refreshToken1);
//        } else {
//            RefreshToken newToken = new RefreshToken(tokenDto.getRefreshToken(), member.getEmail());
//            refreshTokenRepository.save(newToken);
//        }
//
//        //토큰을 header에 넣어서 클라이언트에게 전달하기
//        setHeader(response, tokenDto);
//
//        Long notificationNum = notificationRepository.countUnReadStateNotifications(member.getMemberId());
//
//        UserInfoDto userInfoDto = new UserInfoDto(member);
//
//        return GlobalResDto.success(userInfoDto, "Success Google login");
//    }
//
//    //header 에 Content-type 지정
//    public String getAccessToken(String code) throws JsonProcessingException {
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
//
//        //HTTP Body 생성
//        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
//        body.add("grant_type", "authorization_code");
//        body.add("client_id", client_id);
//        body.add("client_secret", clientSecret);
//        body.add("redirect_uri", redirect_uri);
//        body.add("code", code);
//
//
//        //HTTP 요청 보내기
//        HttpEntity<MultiValueMap<String, String>> googleTokenRequest =
//                new HttpEntity<>(body, headers);
//        RestTemplate rt = new RestTemplate();
//        ResponseEntity<String> response = rt.exchange(
//                "https://oauth2.googleapis.com/token",
//                HttpMethod.POST,
//                googleTokenRequest,
//                String.class
//        );
//
//        //HTTP 응답 (JSON) -> 액세스 토큰 파싱
//        //JSON -> JsonNode 객체로 변환
//        String responseBody = response.getBody();
//        ObjectMapper objectMapper = new ObjectMapper();
//        JsonNode jsonNode = objectMapper.readTree(responseBody);
//
//        String accessToken = jsonNode.get("access_token").asText();
////        String refreshToken = jsonNode.get("refresh_token").asText();
////        String refreshToken = null;
////        return new TokenFactory(accessToken, refreshToken);
//        return accessToken;
//    }
//
//    public GoogleUserInfoDto getGoogleUserInfo(String accessToken) throws JsonProcessingException {
//        // HTTP Header 생성
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Authorization", "Bearer " + accessToken);
//        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
//        // HTTP 요청 보내기
//        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
//        RestTemplate rt = new RestTemplate();
//        ResponseEntity<String> response = rt.exchange(
//                "https://openidconnect.googleapis.com/v1/userinfo",
//                HttpMethod.POST,
//                kakaoUserInfoRequest,
//                String.class
//        );
//        //HTTP 응답 (JSON)
//        //JSON -> JsonNode 객체로 변환
//        String responseBody = response.getBody();
//        ObjectMapper objectMapper = new ObjectMapper();
//        JsonNode jsonNode = objectMapper.readTree(responseBody);
//        String id = jsonNode.get("sub").asText();
//
//        String userEmail = jsonNode.get("email").asText();
//
//        String userName = jsonNode.get("name").asText();
//        return new GoogleUserInfoDto(id, userName, userEmail);
//    }
//
//    private Member signupGoogleUser(GoogleUserInfoDto googleUserInfoDto) {
//        // 재가입 방지
//        // DB 에 중복된 Google Id 가 있는지 확인
//        Member findGoogle = memberRepository.findByEmail("g_"+googleUserInfoDto.getUserEmail()).orElse(null);
//
//
//        //DB에 중복된 계정이 없으면 회원가입 처리
//        if (findGoogle == null) {
//
//            String email = googleUserInfoDto.getUserEmail();
//            String password = UUID.randomUUID().toString();
//
//            Member googleMember = Member.builder()
//                    .email("g_" + email)
//                    .userName(comfortUtils.makeUserNickName())
//                    .userImgUrl("https://eunibucket.s3.ap-northeast-2.amazonaws.com/testdir/normal_user_img.png")//수정필요
//                    .pw(passwordEncoder.encode(password))
//                    .isAccepted(false)
//                    .isDeleted(false)
//                    .role(Role.USER)
//                    .build();
//            memberRepository.save(googleMember);
//
//            return googleMember;
//        }
//
//        return findGoogle;
//    }
//
////    public void forceLoginUser(Member googleMember) {
////        UserDetails userDetails = new UserDetailsImpl(googleMember);
////        if (googleMember.getIsDeleted().equals(true)) {
////            throw new CustomException(ErrorCode.DELETED_USER_EXCEPTION);
////        }
////        Authentication authentication =
////                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
////        SecurityContextHolder.getContext().setAuthentication(authentication);
////    }
////
////    private void setHeader(HttpServletResponse response, TokenDto tokenDto) {
////        response.addHeader(JwtUtil.ACCESS_TOKEN, tokenDto.getAccessToken());
////        response.addHeader(JwtUtil.REFRESH_TOKEN, tokenDto.getRefreshToken());
////    }
//}
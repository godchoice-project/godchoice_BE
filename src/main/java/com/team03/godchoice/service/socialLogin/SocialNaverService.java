//package com.team03.godchoice.service.socialLogin;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.google.gson.JsonElement;
//import com.google.gson.JsonParser;
//import com.team03.godchoice.domain.Member;
//import com.team03.godchoice.domain.RefreshToken;
//import com.team03.godchoice.enumclass.Role;
//import com.team03.godchoice.dto.GlobalResDto;
//import com.team03.godchoice.dto.TokenDto;
//import com.team03.godchoice.dto.social.NaverUserInfoDto;
//import com.team03.godchoice.repository.MemberRepository;
//import com.team03.godchoice.repository.RefreshTokenRepository;
//import com.team03.godchoice.security.jwt.JwtUtil;
//import com.team03.godchoice.security.jwt.UserDetailsImpl;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//import org.springframework.util.LinkedMultiValueMap;
//import org.springframework.util.MultiValueMap;
//import org.springframework.web.client.RestTemplate;
//
//import javax.servlet.http.HttpServletResponse;
//import java.io.*;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.util.Optional;
//import java.util.UUID;
//
//@RequiredArgsConstructor
//@Service
//public class SocialNaverService {
//// 네이버 로그인 수정
//    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
//    private String client_id;
//    @Value("${spring.security.oauth2.client.registration.naver.client-secret}")
//    private String client_secret;
//    @Value("${spring.security.oauth2.client.provider.naver.user-info-uri}")
//    private String user_url;
//    @Value("${spring.security.oauth2.client.registration.naver.redirect-uri}")
//    private String redirect_uri;
//
//    private final PasswordEncoder passwordEncoder;
//    private final MemberRepository memberRepository;
//    private final JwtUtil jwtUtil;
//    private final RefreshTokenRepository refreshTokenRepository;
//    public GlobalResDto<?> naverLogin(String code, String state, HttpServletResponse response) throws JsonProcessingException {
//
//        System.out.println("여기임");
//        System.out.println(code);
//        System.out.println(state);
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
//
//        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//        params.add("grant_type", "authorization_code");
//        params.add("client_id", client_id);
//        params.add("client_secret", client_secret);
//        params.add("code", code);
//        params.add("state",state);
//
//
//        HttpEntity<MultiValueMap<String, String>> naverTokenRequest =
//                new HttpEntity<>(params,headers);
//        RestTemplate rt = new RestTemplate();
//        ResponseEntity<String> naverResponse = rt.exchange(
//                "https://nid.naver.com/oauth2.0/token",
//                HttpMethod.POST,
//                naverTokenRequest,
//                String.class
//        );
//
//        String responseBody = naverResponse.getBody();
//        System.out.println(responseBody);
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        JsonNode jsonNode = objectMapper.readTree(responseBody);
//
//        String accessToken = jsonNode.get("access_token").asText();
//        System.out.println();
//        System.out.println(jsonNode.get("token_type").asText());
//
//        HttpHeaders headers2 = new HttpHeaders();
//        headers.add("Authorization", "Bearer " + accessToken);
//        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
//
//        // HTTP 요청 보내기
//        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
//        RestTemplate rt2 = new RestTemplate();
//        ResponseEntity<String> response2 = rt2.exchange(
//                "https://openapi.naver.com/v1/nid/me",
//                HttpMethod.POST,
//                kakaoUserInfoRequest,
//                String.class
//        );
//
//        String responseBodyUser = response2.getBody();
//        System.out.println(responseBodyUser);
//        ObjectMapper objectMapper2 = new ObjectMapper();
//        JsonNode jsonNode2 = objectMapper2.readTree(responseBodyUser);
//        Long id = jsonNode2.get("response").get("id") .asLong();
//        String profileImage = jsonNode2.get("response").get("profile_image").asText();
//        String email = jsonNode2.get("response").get("email").asText();
//        String name = jsonNode2.get("response").get("name").asText();
//
//
////        body.add("redirect_uri", "http://localhost:8080/member/signup/kakao");
////        body.add("redirect_uri", "http://54.180.201.200/member/signup/kakao");
////        body.add("redirect_uri", "http://localhost:3000/member/signup/kakao");
////        body.add("redirect_uri", "https://godchoice.shop/member/signup/kakao");
//
//
//
////        try {
////            // 네이버에서 가져온 유저 정보 + 임의 비밀번호 생성
////            NaverUserInfoDto naverUser = getNaverUserInfo(code,state);
////            String provider = "naver";
////
////            // 재가입 방지
////            // 네이버 ID로 유저 정보 DB에서 조회
////            Member naverMember = memberRepository.findByEmail("n_" + naverUser.getUserEmail()).orElse(null);
////
////            // 없다면 회원가입
////            if(naverMember == null) {
////                Role role;
////                if (memberRepository.findAll().isEmpty()) {
////                    role = Role.ADMIN;
////                } else {
////                    role = Role.USER;
////                }
////
////                naverMember  = Member.builder()
////                        .email("n_" + naverUser.getUserEmail())
////                        .userName(naverUser.getNickName())
////                        .userImgUrl(naverUser.getUserImgUrl())
////                        .pw(passwordEncoder.encode(UUID.randomUUID().toString()))
////                        .isAccepted(false)
////                        .isDeleted(false)
////                        .role(role)
////                        .build();
////                memberRepository.save(naverMember);
////            } else {
////                // 강제 로그인
////                UserDetailsImpl userDetails = new UserDetailsImpl(naverMember);
////                Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
////                SecurityContextHolder.getContext().setAuthentication(authentication);
////            }
////
////            // 토큰 관리
////            TokenDto tokenDto = jwtUtil.createAllToken(naverMember.getEmail());
////            Optional<RefreshToken> refreshToken = refreshTokenRepository.findByAccountEmail(naverMember.getEmail());
////            if (refreshToken.isPresent()) {
////                refreshTokenRepository.save(refreshToken.get().updateToken(tokenDto.getRefreshToken()));
////            } else {
////                RefreshToken newToken = new RefreshToken(tokenDto.getRefreshToken(), naverMember.getEmail());
////                refreshTokenRepository.save(newToken);
////            }
////
////            //토큰을 header에 넣어서 클라이언트에게 전달하기
////            setHeader(response, tokenDto);
////
////            return GlobalResDto.success(null, "Success Naver Login");
////
////        } catch (IOException e) {
////            throw new RuntimeException(e);
////        }
//        return null;
//    }
//
//    // 네이버에 요청해서 데이터 전달 받는 메소드
//    private JsonElement jsonElement(String reqURL, String token, String code, String state) throws IOException {
//
//        // 요청하는 URL 설정
//        URL url = new URL(reqURL);
//        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//
//        // POST 요청을 위해 기본값이 false인 setDoOutput을 true로
//        connection.setRequestMethod("POST");
//        connection.setDoOutput(true);
//
//        // POST 요청에 필요한 데이터 저장 후 전송
//        if (token == null) {
//            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
//            String sb = "grant_type=authorization_code" +
//                    "&client_id=" + client_id +
//                    "&client_secret=" + client_secret +
//                    "&redirect_uri=" + redirect_uri +
//                    "&code=" + code +
//                    "&state=" + state;
//            bw.write(sb);
//            bw.flush();
//            bw.close();
//        } else {
//            connection.setRequestProperty("Autorization", "Bearer" + token);
//        }
//
//        // 요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
//        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//        String line;
//        StringBuilder result = new StringBuilder();
//
//        while ((line = br.readLine()) != null) {
//            result.append(line);
//        }
//        br.close();
//
//        // Gson 라이브러리에 포함된 클래스로 JSON 파싱
//        return JsonParser.parseString(result.toString());
//    }
//
//    // 네이버에 요청해서 회원정보 받는 메소드
//    private NaverUserInfoDto getNaverUserInfo(String code, String state) throws IOException {
//        String codeReqURL = redirect_uri;
//        String tokenReqURL = user_url;
//
//        // 코드를 네이ㅣ버에 전달하여 엑세스 토큰 가져옴
//        JsonElement tokenElement = jsonElement(codeReqURL, null, code, state);
//        String access_token = tokenElement.getAsJsonObject().get("access_token").getAsString();
//        String refresh_token = tokenElement.getAsJsonObject().get("refresh_token").getAsString();
//
//        // 엑세스 토큰 네이버에 전달하여 유저정보 가져옴
//        JsonElement userInfoElement = jsonElement(tokenReqURL, access_token, null, null);
////        String SocialId = String.valueOf(userInfoElement.getAsJsonObject().get("response").getAsJsonObject()
////                .getAsJsonObject().get("id"));
//        String nickName = String.valueOf(userInfoElement.getAsJsonObject().get("response")
//                .getAsJsonObject().get("name"));
//        String userEmail = String.valueOf(userInfoElement.getAsJsonObject().get("response")
//                .getAsJsonObject().get("email"));
//        String userImgUrl = String.valueOf(userInfoElement.getAsJsonObject().get("response")
//                .getAsJsonObject().get("img"));
//
//        return new NaverUserInfoDto(nickName, userEmail, userImgUrl, access_token, refresh_token);
//    }
//
//    private void setHeader(HttpServletResponse response, TokenDto tokenDto) {
//        response.addHeader(JwtUtil.ACCESS_TOKEN, tokenDto.getAccessToken());
//        response.addHeader(JwtUtil.REFRESH_TOKEN, tokenDto.getRefreshToken());
//    }
//}
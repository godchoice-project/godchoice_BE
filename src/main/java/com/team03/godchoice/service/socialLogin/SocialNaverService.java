package com.team03.godchoice.service.socialLogin;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.team03.godchoice.domain.Member;
import com.team03.godchoice.domain.RefreshToken;
import com.team03.godchoice.domain.domainenum.Role;
import com.team03.godchoice.dto.GlobalResDto;
import com.team03.godchoice.dto.TokenDto;
import com.team03.godchoice.dto.social.NaverUserInfoDto;
import com.team03.godchoice.repository.MemberRepository;
import com.team03.godchoice.repository.RefreshTokenRepository;
import com.team03.godchoice.security.jwt.JwtUtil;
import com.team03.godchoice.security.jwt.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class SocialNaverService {

    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String client_id;
    @Value("${spring.security.oauth2.client.registration.naver.client-secret}")
    private String client_secret;

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    public GlobalResDto<?> naverLogin(String code, String state, HttpServletResponse response) {

        try {
            // 네이버에서 가져온 유저 정보 + 임의 비밀번호 생성
            NaverUserInfoDto naverUser = getNaverUserInfo(code,state);
            String password = UUID.randomUUID().toString();
            String encodePassword = passwordEncoder.encode(password);
            String provider = "naver";

            // 재가입 방지
            // 네이버 ID로 유저 정보 DB에서 조회
            Member naverMember = memberRepository.findByEmail("n_" + naverUser.getUserEmail()).orElse(null);

            // 없다면 회원가입
            if(naverMember == null) {
                Role role;
                if (memberRepository.findAll().isEmpty()) {
                    role = Role.ADMIN;
                } else {
                    role = Role.USER;
                }

                naverMember  = Member.builder()
                        .email("n_" + naverUser.getUserEmail())
                        .userName(naverUser.getNickName())
                        .userImgUrl(naverUser.getUserImgUrl())
                        .pw(passwordEncoder.encode(UUID.randomUUID().toString()))
                        .provider(provider)
                        .isAccepted(false) // ?!?!??!?
                        .isDeleted(false) // ?!?!?!?!
                        .role(role)
                        .build();
                memberRepository.save(naverMember);
            } else {
                // 강제 로그인
                UserDetailsImpl userDetails = new UserDetailsImpl(naverMember);
                Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

            // 토큰 관리
            TokenDto tokenDto = jwtUtil.createAllToken(naverMember.getEmail());
            Optional<RefreshToken> refreshToken = refreshTokenRepository.findByAccountEmail(naverMember.getEmail());

            if (refreshToken.isPresent()) {
                refreshTokenRepository.save(refreshToken.get().updateToken(tokenDto.getRefreshToken()));
            } else {
                RefreshToken newToken = new RefreshToken(tokenDto.getRefreshToken(), naverMember.getEmail());
                refreshTokenRepository.save(newToken);
            }

            //토큰을 header에 넣어서 클라이언트에게 전달하기
            setHeader(response, tokenDto);

            return GlobalResDto.success(null, "Success Naver Login");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 네이버에 요청해서 데이터 전달 받는 메소드
    private JsonElement jsonElement(String reqURL, String token, String code, String state) throws IOException {

        // 요청하는 URL 설정
        URL url = new URL(reqURL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        // POST 요청을 위해 기본값이 false인 setDoOutput을 true로
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

        // POST 요청에 필요한 데이터 저장 후 전송
        if (token == null) {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
            String sb = "grant_type=authorization_code" +
                    "&client_id=" + client_id +
                    "&client_secret=" + client_secret +
                    "&redirect_uri=http://localhost:8080/member/signup/naver" +
//                    "&redirect_uri=http://localhost:3000/member/signup/naver" +
//                    "&redirect_uri=https://우리도매인/member/signup/naver" +
                    "&code=" + code +
                    "&state=" + state;
            bw.write(sb);
            bw.flush();
            bw.close();
        } else {
            connection.setRequestProperty("Autorization", "Bearer" + token);
        }

        // 요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
        StringBuilder result = new StringBuilder();

        while ((line = br.readLine()) != null) {
            result.append(line);
        }
        br.close();

        // Gson 라이브러리에 포함된 클래스로 JSON 파싱
        return JsonParser.parseString(result.toString());
    }

    // 네이버에 요청해서 회원정보 받는 메소드
    private NaverUserInfoDto getNaverUserInfo(String code, String state) throws IOException {
        String codeReqURL = "https://nid.naver.com/oauth2.0/token";
        String tokenReqURL = "https://openapi.naver.com/v1/nid/me";

        // 코드를 네이ㅣ버에 전달하여 엑세스 토큰 가져옴
        JsonElement tokenElement = jsonElement(codeReqURL, null, code, state);
        String access_token = tokenElement.getAsJsonObject().get("access_token").getAsString();
        String refresh_token = tokenElement.getAsJsonObject().get("refresh_token").getAsString();

        // 엑세스 토큰 네이버에 전달하여 유저정보 가져옴
        JsonElement userInfoElement = jsonElement(tokenReqURL, access_token, null, null);
        String SocialId = String.valueOf(userInfoElement.getAsJsonObject().get("response").getAsJsonObject()
                .getAsJsonObject().get("id"));
        String nickName = String.valueOf(userInfoElement.getAsJsonObject().get("response")
                .getAsJsonObject().get("name"));
        String userEmail = String.valueOf(userInfoElement.getAsJsonObject().get("response")
                .getAsJsonObject().get("email"));
        String userImgUrl = String.valueOf(userInfoElement.getAsJsonObject().get("response")
                .getAsJsonObject().get("img"));

        return new NaverUserInfoDto(SocialId, nickName, userEmail, userImgUrl, access_token, refresh_token);
    }

    private void setHeader(HttpServletResponse response, TokenDto tokenDto) {
        response.addHeader(JwtUtil.ACCESS_TOKEN, tokenDto.getAccessToken());
        response.addHeader(JwtUtil.REFRESH_TOKEN, tokenDto.getRefreshToken());
    }
}






    /*private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final ComfortUtils comfortUtils;
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public GlobalResDto<?> naverLogin(String code, String state, HttpServletResponse response) throws JsonProcessingException {
        try {
            //네이버에서 가져온 유저정보 + 임의 비밀번호 생성
            NaverUserInfoDto naverUserInfoDto = getNaverUserInfo(code, state);
            String password = UUID.randomUUID().toString();
            String encodePassword = passwordEncoder.encode(password);
            String provider = "naver";
            String naverUserEmail = naverUserInfoDto.getUserEmail().substring(1, naverUserInfoDto.getUserEmail().length() - 1);
            String realEmail = "n_" + naverUserEmail;
            // 재가입 방지
            // 네이버 ID로 유저 정보 DB에서 조회
            Member member = memberRepository.findByEmail(realEmail).orElse(null);
            // 없으면 회원가입
            if (member == null) {
                member = Member.builder()
                        .userName(comfortUtils.makeUserNickName())
                        .pw(encodePassword)
                        .email("n_" + naverUserEmail)
//                        .userRealEmail(naverUserInfoDto.getUserEmail())
                        .provider(provider)
                        .isAccepted(false)
                        .isDeleted(false)
                        .build();
                memberRepository.save(member);
            } else {
                // 강제 로그인
                // 탈퇴 회원 처리
                UserDetailsImpl userDetails = new UserDetailsImpl(member);
                if (userDetails.getAccount().getIsDeleted().equals(true)) {
                    throw new CustomException(ErrorCode.DELETED_USER_EXCEPTION);
                }

                Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
*//*            TokenDto tokenDto = jwtUtil.createAllToken(member.getUserRealEmail());
            Optional<RefreshToken> refreshToken = refreshTokenRepository.findByAccountEmail(member.getUserRealEmail());
            if (refreshToken.isPresent()) {
                refreshTokenRepository.save(refreshToken.get().updateToken(tokenDto.getRefreshToken()));
            } else {
                RefreshToken newToken = new RefreshToken(tokenDto.getRefreshToken(), member.getUserRealEmail());
                refreshTokenRepository.save(newToken);
            }
            setHeader(response,tokenDto);*//*

            *//*SocialResponseDto socialResponseDto = SocialResponseDto.builder()
                    .userName(member.getUserName())
                    .email(member.getEmail())
                    .userImgUrl(member.getUserImgUrl())
                    .build();*//*

            // 토큰 발급
            TokenDto tokenDto = jwtUtil.createAllToken(naverUserInfoDto.getUserEmail());
            Optional<RefreshToken> refreshToken = refreshTokenRepository.findByAccountEmail(naverUserInfoDto.getUserEmail());

            if (refreshToken.isPresent()) {
                refreshTokenRepository.save(refreshToken.get().updateToken(tokenDto.getRefreshToken()));
            } else {
//                RefreshToken newToken = new RefreshToken(tokenDto.getRefreshToken(), member.getEmail());
                RefreshToken newToke = new RefreshToken(tokenDto.getRefreshToken(), naverUserInfoDto.getUserEmail());
                refreshTokenRepository.save(newToke);
            }
            setHeader(response,tokenDto);

////            토큰 관리
//            TokenFactory tokenFactory = memberService.accessAndRefreshTokenProcess(member.getEmail(), response);
//
//            String refreshToken =  tokenFactory.getRefreshToken();
//
//
//            SocialResponseDto socialResponseDto = SocialResponseDto.builder()
//                    .userName(member.getUserName())
//                    .email(member.getEmail())
//                    .userImgUrl(member.getUserImgUrl())
//                    .accessToken(tokenFactory.getAccessToken())
//                    .refreshToken(tokenFactory.getRefreshToken())
//                    .build();

            return GlobalResDto.success(null, "로그인이 완료되었습니다");

        } catch (IOException e) {
            return GlobalResDto.fail("로그인실패");
        }
    }

    // 네이버에 요청해서 데이터 전달 받는 메소드
    private JsonElement jsonElement(String codeReqURL, String token, String code, String state) throws IOException {

        // 요청하는 URL 설정
        URL url = new URL(codeReqURL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        // POST 요청을 위해 기본값이 false인 serDoOutput을 true로
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

        // POST 요청에 필요한 데이터 저장 후 전송
        if (token == null) {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
            String sb = "grant_type=authorization_code" +
                    "&client_id" + client_id +
                    "&client_secret=" + client_secret +
                    "&redirect_uri=http://localhost:8080/member/signup/naver" +
//                    "&redirect_uri=http://localhost:3030/member/signup/naver" +
//                    "&redirect_uri=https://추가우리도매인/member/signup/naver" +
                    "&code=" + code +
                    "&state=" + state;
            bw.write(sb);
            bw.flush();
            bw.close();
        } else {
            connection.setRequestProperty("Authorization", "Bearer" + token);
        }

        // 요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
        StringBuilder result = new StringBuilder();

        while ((line = br.readLine()) != null) {
            result.append(line);
        }
        br.close();

        // Gson 라이브러리에 포함된 클래스로 JSON 파싱
        return JsonParser.parseString(result.toString());
    }

    //네이버에 요청해서 회원정보 받는 메소드
    private NaverUserInfoDto getNaverUserInfo(String code, String state) throws IOException {

        String codeReqURL = "https://nid.naver.com/oauth2.0/token";
        String tokenReqURL = "https://openapi.naver.com/v1/nid/me";

        //코드를 네이버를 전달하여 엑세스 토큰 가져옴
        JsonElement tokenElement = jsonElement(codeReqURL, null, code, state);
        String access_token = tokenElement.getAsJsonObject().get("access_token").getAsString();
        String refresh_token = tokenElement.getAsJsonObject().get("refresh_token").getAsString();

        // 엑세스 토큰을 네이버에 전달하여 유저 정보를 가져옴
        JsonElement userInfoElement = jsonElement(tokenReqURL, access_token, null, null);
        String naverId = String.valueOf(userInfoElement.getAsJsonObject().get("response")
                .getAsJsonObject().get("id"));
        String userEmail = String.valueOf(userInfoElement.getAsJsonObject().get("response")
                .getAsJsonObject().get("email;"));
        String nickName = String.valueOf(userInfoElement.getAsJsonObject().get("response")
                .getAsJsonObject().get("name"));
        return new NaverUserInfoDto(naverId, nickName, userEmail, access_token, refresh_token);
    }
    private void setHeader(HttpServletResponse response, TokenDto tokenDto) {
        response.addHeader(JwtUtil.ACCESS_TOKEN, tokenDto.getAccessToken());
        response.addHeader(JwtUtil.REFRESH_TOKEN, tokenDto.getRefreshToken());
    }


}*/




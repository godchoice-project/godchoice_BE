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
//import com.team03.godchoice.dto.TokenDto;
//import com.team03.godchoice.dto.responseDto.UserInfoDto;
//import com.team03.godchoice.dto.social.SocialUserInfoDto;
//import com.team03.godchoice.dto.social.responseDto.GithubResDto;
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
//public class SocialGithubService implements LoginInterface {
//
//    @Value("${spring.security.oauth2.client.registration.github.client-id}")
//    private String githubClientId;
//    @Value("${spring.security.oauth2.client.registration.github.client-secret}")
//    private String githubCClientSecret;
//
//    public final MemberRepository memberRepository;
//    public final RefreshTokenRepository refreshTokenRepository;
//    private final PasswordEncoder passwordEncoder;
//    private final JwtUtil jwtUtil;
//    private final NotificationRepository notificationRepository;
//
//    public GlobalResDto<?> githubLogin(String code, HttpServletResponse response) throws JsonProcessingException {
//        //인가코드를 통해 accesstoken받기
//        String accessToken = issuedAccessToken(code);
//
//        //accesstoken을 이용해 사용자 정보 가져오기
//        SocialUserInfoDto socialUserInfoDto = getGithubUserInfo(accessToken);
//
//        //사용자 정보를 토대로 가입진행
//        Member member = saveMember(socialUserInfoDto);
//
//        //강제로그인처리
//        forceLoginUser(member);
//
//        //리프레쉬, 액세스 토큰 만든후 반환
//        createToken(member,response);
//
//        Long notificationNum = notificationRepository.countUnReadStateNotifications(member.getMemberId());
//
//        UserInfoDto userInfoDto = new UserInfoDto(member);
//
//        return GlobalResDto.success(userInfoDto, "로그인이 완료되었습니다");
//    }
//
//    private String issuedAccessToken(String code) throws JsonProcessingException {
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Accept","application/json");
//
//        // HTTP Body 생성
//        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
//        body.add("client_id", githubClientId);
//        body.add("redirect_uri","http://localhost:8080/member/signup/github");
////        body.add("redirect_uri","http://localhost:3000/member/signup/github");
//        body.add("code", code);
//        body.add("client_secret",githubCClientSecret);
//
//        // HTTP 요청 보내기
//        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
//                new HttpEntity<>(body,headers);
//        RestTemplate rt = new RestTemplate();
//        ResponseEntity<String> response = rt.exchange(
//                "https://github.com/login/oauth/access_token",
//                HttpMethod.POST,
//                kakaoTokenRequest,
//                String.class
//        );
//
//        // HTTP 응답 (JSON) -> 액세스 토큰 파싱
//        String responseBody = response.getBody();
//        System.out.println("responseBody"+responseBody);
//        ObjectMapper objectMapper = new ObjectMapper();
//        JsonNode jsonNode = objectMapper.readTree(responseBody);
//
//        return jsonNode.get("access_token").asText();
//    }
//
//    private SocialUserInfoDto getGithubUserInfo(String accessToken) throws JsonProcessingException {
//
//        // HTTP Header 생성
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Accept","application/vnd.github+json");
//        headers.add("Authorization", "Bearer " + accessToken);
//
//        // HTTP 요청 보내기
//        HttpEntity<MultiValueMap<String, String>> githubUserInfoRequest = new HttpEntity<>(headers);
//        RestTemplate rt = new RestTemplate();
//        ResponseEntity<String> response = rt.exchange(
//                "https://api.github.com/user",
//                HttpMethod.GET,
//                githubUserInfoRequest,
//                String.class
//        );
//
//        String responseBody = response.getBody();
//        ObjectMapper objectMapper = new ObjectMapper();
//        JsonNode jsonNode = objectMapper.readTree(responseBody);
//
//        Long id = jsonNode.get("id").asLong();
//        String nickname = jsonNode.get("name").asText();
//        String email = jsonNode.get("email").asText();
//        String userImgUrl = jsonNode.get("avatar_url").asText();
//
//        return new SocialUserInfoDto(id.toString(), nickname, email, userImgUrl);
//    }
//
//    public Member saveMember(SocialUserInfoDto socialUserInfoDto) {
//        Member kakaoMember = memberRepository.findByEmail("git_"+socialUserInfoDto.getEmail()).orElse(null);
//
//        //없다면 저장
//        if (kakaoMember == null) {
//            Role role;
//            if (memberRepository.findAll().isEmpty()) {
//                role = Role.ADMIN;
//            } else {
//                role = Role.USER;
//            }
//
//            String email;
//            if(socialUserInfoDto.getEmail().isBlank()){
//                email = "git_"+socialUserInfoDto.getEmail();
//            }else{
//                do {
//                    email = "git_" + UUID.randomUUID().toString().substring(0, 7) + "@git.com";
//                } while (memberRepository.findByEmail(email).isPresent());
//            }
//
//            Member member = Member.builder().
//                    email(email)
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
//
//        //있다면 member 반환
//        return kakaoMember;
//    }
//
//    @Override
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

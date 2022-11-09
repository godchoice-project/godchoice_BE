package com.team03.godchoice.security;

import com.team03.godchoice.domain.Member;
import lombok.Data;

import java.util.Map;

@Data
public class OAuthAttributes {
    private Map<String, Object> attributes;
    private String userNameAttributeKey;
    private String userName;
    private String email;
    private String userImgUrl;

    public OAuthAttributes(Map<String, Object> attributes, String userNameAttributeKey, String userName, String email, String picture) {
        this.attributes = attributes;
        this.userNameAttributeKey = userNameAttributeKey;
        this.userName = userName;
        this.email = email;
        this.userImgUrl = picture;
    }

    public OAuthAttributes() {
    }

    // 해당 로그인인 서비스가 kakao인지 google인지 구분하여, 알맞게 매핑을 해주도록 합니다.
    // 여기서 registrationId는 OAuth2 로그인을 처리한 서비스 명("google","kakao","naver"..)이 되고,
    // userNameAttributeuserName은 해당 서비스의 map의 키값이 되는 값이됩니다. {google="sub", kakao="id", naver="response"}
    public static OAuthAttributes of(String registrationId, String usernameAttributename, Map<String, Object> attributes) {
        if (registrationId.equals("kakao")) {
            return ofKakao(usernameAttributename, attributes);
        } else if (registrationId.equals("naver")) {
            return ofNaver(usernameAttributename,attributes);
        }
        return ofGoogle(usernameAttributename, attributes);
    }

    private static OAuthAttributes ofKakao(String usernameAttributename, Map<String, Object> attributes) {
        Map<String, Object> kakao_account = (Map<String, Object>) attributes.get("kakao_account");  // 카카오로 받은 데이터에서 계정 정보가 담긴 kakao_account 값을 꺼낸다.
        Map<String, Object> profile = (Map<String, Object>) kakao_account.get("profile");   // 마찬가지로 profile(nickuserName, image_url.. 등) 정보가 담긴 값을 꺼낸다.

        return new OAuthAttributes(attributes,
                usernameAttributename,
                (String) profile.get("nickname"),
                (String) kakao_account.get("email"),
                (String) profile.get("profile_image_url"));
    }

    private static OAuthAttributes ofNaver(String usernameAttributename, Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");    // 네이버에서 받은 데이터에서 프로필 정보다 담긴 response 값을 꺼낸다.

        return new OAuthAttributes(attributes,
                usernameAttributename,
                (String) response.get("userName"),
                (String) response.get("email"),
                (String) response.get("profile_image"));
    }

    private static OAuthAttributes ofGoogle(String usernameAttributename, Map<String, Object> attributes) {

        return new OAuthAttributes(attributes,
                usernameAttributename,
                (String) attributes.get("userName"),
                (String) attributes.get("email"),
                (String) attributes.get("picture"));
    }

    // .. getter/setter 생략

    // 현재 OAuthAttributes 객체에서 요청하면 -> Member 객체로 변환
    public Member toEntity() {
        return new Member(userName, email, userImgUrl);
    }
}
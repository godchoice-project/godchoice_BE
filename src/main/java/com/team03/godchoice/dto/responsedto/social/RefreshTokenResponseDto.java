package com.team03.godchoice.dto.responsedto.social;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RefreshTokenResponseDto {
    /**
     * 만료된 access token 갱신을 위한 refresh token
     */
    private String newAccessToken;

    public RefreshTokenResponseDto(String newAccessToken) {
        this.newAccessToken = newAccessToken;
    }
}
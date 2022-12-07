package com.team03.godchoice.repository;

import com.team03.godchoice.domain.SocialAccessToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SocialAccessTokenRepository extends JpaRepository<SocialAccessToken,Long> {
    Optional<SocialAccessToken> findByAccountEmail(String email);

    List<SocialAccessToken> findAllByAccountEmailOrderByAccessIdDesc(String email);
}

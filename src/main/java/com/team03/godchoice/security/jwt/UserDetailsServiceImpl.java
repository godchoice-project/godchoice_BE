package com.team03.godchoice.security.jwt;

import com.team03.godchoice.domain.Member;
import com.team03.godchoice.exception.CustomException;
import com.team03.godchoice.exception.ErrorCode;
import com.team03.godchoice.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Member member = memberRepository.findByEmail(email).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_MEMBER) //커스텀 예외처리 해야함
        );

        UserDetailsImpl userDetails = new UserDetailsImpl();
        userDetails.setAccount(member);

        return userDetails;
    }
}

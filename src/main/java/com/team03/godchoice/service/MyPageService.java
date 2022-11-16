package com.team03.godchoice.service;

import com.team03.godchoice.domain.Member;
import com.team03.godchoice.domain.askpost.AskPost;
import com.team03.godchoice.domain.domainenum.RegionTag;
import com.team03.godchoice.domain.eventpost.EventPost;
import com.team03.godchoice.domain.gatherPost.GatherPost;
import com.team03.godchoice.dto.GlobalResDto;
import com.team03.godchoice.dto.requestDto.MyPageReqDto;
import com.team03.godchoice.dto.responseDto.mypageResDto.MyPageMyPostResDto;
import com.team03.godchoice.dto.responseDto.mypageResDto.MyPagePostResDto;
import com.team03.godchoice.dto.responseDto.mypageResDto.MyPageResDto;
import com.team03.godchoice.dto.responseDto.mypageResDto.MyPageUserResDto;
import com.team03.godchoice.exception.CustomException;
import com.team03.godchoice.exception.ErrorCode;
import com.team03.godchoice.repository.MemberRepository;
import com.team03.godchoice.repository.askpost.AskPostRepository;
import com.team03.godchoice.repository.eventpost.EventPostRepository;
import com.team03.godchoice.repository.gatherpost.GatherPostRepository;
import com.team03.godchoice.s3.S3Uploader;
import com.team03.godchoice.security.jwt.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MyPageService {

    private final MemberRepository memberRepository;
    private final EventPostRepository eventPostRepository;
    private final GatherPostRepository gatherPostRepository;
    private final AskPostRepository askPostRepository;
    private final S3Uploader s3Uploader;
    private final EventPostService eventPostService;

    @Transactional
    public GlobalResDto<?> changeUserInfo(MyPageReqDto user, MultipartFile multipartFile, UserDetailsImpl userDetails) throws IOException {
        Member member = isPresentMember(userDetails);
        if(member==null){
            throw new CustomException(ErrorCode.NOT_FOUND_MEMBER);
        }

//        Member member1 =memberRepository.findByUserName(user.getUserName());
//        if(member1!=null){
//            throw new CustomException(ErrorCode.DUPLICATION_NICKNAME);
//        }

        String userImgUrl;
        if(multipartFile!=null){
            userImgUrl = s3Uploader.uploadFiles(multipartFile,"testdir");
        }else{
            userImgUrl = member.getUserImgUrl();
        }

        RegionTag regionTag = eventPostService.toRegionTag(user.getUserAddress());

        member.update(user,regionTag,userImgUrl);
        memberRepository.save(member);
        return GlobalResDto.success(null,"수정이 완료되었습니다");
    }

    public GlobalResDto<?> getMyPage(UserDetailsImpl userDetails) {
        Member member = isPresentMember(userDetails);
        if(member==null){
            throw new CustomException(ErrorCode.NOT_FOUND_MEMBER);
        }

        //유저정보 가져와서 DTO에 저장
        MyPageUserResDto myPageUserResDto = new MyPageUserResDto(member);

        //내가 쓴 행사글 가져와서 Dto 저장
        List<EventPost> eventPostList = eventPostRepository.findAllByMember(member);
        List<MyPagePostResDto> myEventPost = new ArrayList<>();
        for(EventPost eventPost : eventPostList){
            MyPagePostResDto myPagePostResDto = new MyPagePostResDto(eventPost);
            myEventPost.add(myPagePostResDto);
        }

        //내가 쓴 모집글 가져와서 Dto 저장
        List<GatherPost> gatherPostList = gatherPostRepository.findAllByMember(member);
        List<MyPagePostResDto> myGatherPost = new ArrayList<>();
        for(GatherPost gatherPost : gatherPostList){
            MyPagePostResDto myPagePostResDto = new MyPagePostResDto(gatherPost);
            myGatherPost.add(myPagePostResDto);
        }

        //내가 쓴 질문들 가져와서 DTo 저장
        List<AskPost> askPostList = askPostRepository.findAllByMember(member);
        List<MyPagePostResDto> myAskPost = new ArrayList<>();
        for(AskPost askPost : askPostList){
            MyPagePostResDto myPagePostResDto = new MyPagePostResDto(askPost);
            myAskPost.add(myPagePostResDto);
        }

        MyPageMyPostResDto myPageMyPostResDto = new MyPageMyPostResDto(myEventPost,myGatherPost,myAskPost);

        MyPageResDto myPageResDto = new MyPageResDto(myPageUserResDto,myPageMyPostResDto);
        return GlobalResDto.success(myPageResDto,"마이페이지가 조회되었습니다.");
    }

    public Member isPresentMember(UserDetailsImpl userDetails) {
        Optional<Member> member = memberRepository.findById(userDetails.getAccount().getMemberId());
        return member.orElse(null);
    }
}

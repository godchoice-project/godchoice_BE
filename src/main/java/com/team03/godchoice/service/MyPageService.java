package com.team03.godchoice.service;

import com.team03.godchoice.publicClass.MakeRegionTagClass;
import com.team03.godchoice.adminPage.AdminPage;
import com.team03.godchoice.adminPage.AdminPageRepository;
import com.team03.godchoice.domain.Member;
import com.team03.godchoice.domain.askpost.AskPost;
import com.team03.godchoice.domain.askpost.AskPostComment;
import com.team03.godchoice.domain.askpost.AskPostLike;
import com.team03.godchoice.enumclass.RegionTag;
import com.team03.godchoice.domain.eventpost.EventPost;
import com.team03.godchoice.domain.eventpost.EventPostComment;
import com.team03.godchoice.domain.eventpost.EventPostLike;
import com.team03.godchoice.domain.gatherPost.GatherPost;
import com.team03.godchoice.domain.gatherPost.GatherPostComment;
import com.team03.godchoice.domain.gatherPost.GatherPostLike;
import com.team03.godchoice.dto.GlobalResDto;
import com.team03.godchoice.dto.requestDto.MyPageReqDto;
import com.team03.godchoice.dto.responseDto.askpost.AskPostAllResDto;
import com.team03.godchoice.dto.responseDto.eventpost.EventPostAllResDto;
import com.team03.godchoice.dto.responseDto.gatherpost.GatherPostAllResDto;
import com.team03.godchoice.dto.responseDto.mypageResDto.MyPageMyPostResDto;
import com.team03.godchoice.dto.responseDto.mypageResDto.MyPageUserResDto;
import com.team03.godchoice.enumclass.Role;
import com.team03.godchoice.exception.CustomException;
import com.team03.godchoice.exception.ErrorCode;
import com.team03.godchoice.repository.MemberRepository;
import com.team03.godchoice.repository.askpost.AskPostCommentRepository;
import com.team03.godchoice.repository.askpost.AskPostLikeRepository;
import com.team03.godchoice.repository.askpost.AskPostRepository;
import com.team03.godchoice.repository.eventpost.EventPostCommentRepository;
import com.team03.godchoice.repository.eventpost.EventPostLikeRepository;
import com.team03.godchoice.repository.eventpost.EventPostRepository;
import com.team03.godchoice.repository.gatherpost.GatherPostCommentRepository;
import com.team03.godchoice.repository.gatherpost.GatherPostLikeRepository;
import com.team03.godchoice.repository.gatherpost.GatherPostRepository;
import com.team03.godchoice.s3.S3Uploader;
import com.team03.godchoice.security.jwt.UserDetailsImpl;
import com.team03.godchoice.util.ComfortUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MyPageService extends MakeRegionTagClass {

    private final MemberRepository memberRepository;

    private final EventPostRepository eventPostRepository;
    private final EventPostCommentRepository eventPostCommentRepository;
    private final EventPostLikeRepository eventPostLikeRepository;

    private final GatherPostRepository gatherPostRepository;
    private final GatherPostCommentRepository gatherPostCommentRepository;
    private final GatherPostLikeRepository gatherPostLikeRepository;

    private final AskPostRepository askPostRepository;
    private final AskPostCommentRepository askPostCommentRepository;
    private final AskPostLikeRepository askPostLikeRepository;
    private final AdminPageRepository adminPageRepository;
    private final S3Uploader s3Uploader;
    private final ComfortUtils comfortUtils;

    @Transactional
    public GlobalResDto<?> changeUserInfo(MyPageReqDto user, MultipartFile multipartFile, UserDetailsImpl userDetails) throws IOException {
        Member member = isPresentMember(userDetails);

        String userImgUrl;
        if (multipartFile != null) {
            userImgUrl = s3Uploader.uploadFiles(multipartFile, "testdir");
        } else {
            userImgUrl = member.getUserImgUrl();
        }

        RegionTag regionTag;
        if(user.getUserAddress()==null || user.getUserAddress().isBlank()){
            regionTag = member.getUserAddressTag();
        }else{
            regionTag= toRegionTag(user.getUserAddress());
        }

        String username;
        if(user.getUserName().isBlank() || user.getUserName()==null){
            username = member.getUserName();
        }else{
            username = user.getUserName();
        }

        member.update(regionTag, userImgUrl,username);
        memberRepository.save(member);
        return GlobalResDto.success(getUser(userDetails).getData(), "수정이 완료되었습니다");
    }

    public GlobalResDto<?> getUser(UserDetailsImpl userDetails) {
        Member member = isPresentMember(userDetails);

        String[] userEmail = toEmail(member);

        if(member.getRole().equals(Role.ADMIN)){
            List<AdminPage> adminPageList = adminPageRepository.findAll();
            MyPageUserResDto myPageUserResDto = new MyPageUserResDto(member, userEmail,adminPageList);
            return GlobalResDto.success(myPageUserResDto,"배너 정보와 유저정보를 가져왔습니다");
        }else {
            MyPageUserResDto myPageUserResDto = new MyPageUserResDto(member, userEmail,null);
            return GlobalResDto.success(myPageUserResDto, "유저정보를 가져왔습니다");
        }
    }

    public GlobalResDto<?> getMyPost(UserDetailsImpl userDetails) {
        Member member = isPresentMember(userDetails);

        //내가 쓴 행사글 가져와서 Dto 저장
        List<EventPost> eventPostList = eventPostRepository.findAllByMemberOrderByEventPostIdDesc(member);
        List<EventPostAllResDto> myEventPost = new ArrayList<>();
        for (EventPost eventPost : eventPostList) {
            boolean bookmark = eventPostLikeRepository.existsByMemberAndEventPost(member, eventPost);
            myEventPost.add(EventPostAllResDto.toEPARD(eventPost, bookmark));
        }

        //내가 쓴 모집글 가져와서 Dto 저장
        List<GatherPost> gatherPostList = gatherPostRepository.findAllByMemberOrderByGatherPostIdDesc(member);
        List<GatherPostAllResDto> myGatherPost = new ArrayList<>();
        for (GatherPost gatherPost : gatherPostList) {
            boolean bookmark = gatherPostLikeRepository.existsByMemberAndGatherPost(member, gatherPost);
            myGatherPost.add(GatherPostAllResDto.toGPARD(gatherPost, bookmark));
        }

        //내가 쓴 질문들 가져와서 DTo 저장
        List<AskPost> askPostList = askPostRepository.findAllByMemberOrderByAskPostIdDesc(member);
        List<AskPostAllResDto> myAskPost = new ArrayList<>();
        for (AskPost askPost : askPostList) {
            boolean bookmark = askPostLikeRepository.existsByMemberAndAskPost(member, askPost);
            myAskPost.add(AskPostAllResDto.toAPARD(askPost, bookmark));
        }

        MyPageMyPostResDto myPageMyPostResDto = new MyPageMyPostResDto(myEventPost, myGatherPost, myAskPost);
        return GlobalResDto.success(myPageMyPostResDto, "내가쓴글을 가져왔습니다");
    }

    public GlobalResDto<?> getMyComment(UserDetailsImpl userDetails) {
        Member member = isPresentMember(userDetails);

        List<EventPostComment> eventPostCommentList = eventPostCommentRepository.findAllByMemberOrderByCommentIdDesc(member);
        List<EventPost> eventPostList = new ArrayList<>();
        for (EventPostComment eventPostComment : eventPostCommentList) {
            if (!eventPostList.contains(eventPostComment.getEventPost())) {
                eventPostList.add(eventPostComment.getEventPost());
            }
        }
        List<EventPostAllResDto> myEventPost = toMyEventPost(member, eventPostList);

        List<GatherPostComment> gatherPostCommentList = gatherPostCommentRepository.findAllByMemberOrderByCommentIdDesc(member);
        List<GatherPost> gatherPostList = new ArrayList<>();
        for (GatherPostComment gatherPostComment : gatherPostCommentList) {
            if (!gatherPostList.contains(gatherPostComment.getGatherPost())) {
                gatherPostList.add(gatherPostComment.getGatherPost());
            }
        }
        List<GatherPostAllResDto> myGatherPost = toMyGatherPost(member, gatherPostList);

        List<AskPostComment> askPostCommentList = askPostCommentRepository.findAllByMemberOrderByCommentIdDesc(member);
        List<AskPost> askPostList = new ArrayList<>();
        for (AskPostComment askPostComment : askPostCommentList) {
            if (!askPostList.contains(askPostComment.getAskPost())) {
                askPostList.add(askPostComment.getAskPost());
            }
        }
        List<AskPostAllResDto> myAskPost = toMyAskPost(member, askPostList);

        MyPageMyPostResDto myPageMyPostResDto = new MyPageMyPostResDto(myEventPost, myGatherPost, myAskPost);
        return GlobalResDto.success(myPageMyPostResDto, "댓글 단 글을 가져왔습니다");
    }


    public GlobalResDto<?> getMyScrap(UserDetailsImpl userDetails) {
        Member member = isPresentMember(userDetails);

        List<EventPostLike> eventPostLikeList = eventPostLikeRepository.findAllByMemberOrderByPostLikeIdDesc(member);
        List<EventPost> eventPostList = new ArrayList<>();
        for (EventPostLike eventPostLike : eventPostLikeList) {
            eventPostList.add(eventPostLike.getEventPost());
        }
        List<EventPostAllResDto> myEventPost = toMyEventPost(member, eventPostList);

        List<GatherPostLike> gatherPostLikeList = gatherPostLikeRepository.findAllByMemberOrderByPostLikeIdDesc(member);
        List<GatherPost> gatherPostList = new ArrayList<>();
        for (GatherPostLike gatherPostLike : gatherPostLikeList) {
            gatherPostList.add(gatherPostLike.getGatherPost());
        }
        List<GatherPostAllResDto> myGatherPost = toMyGatherPost(member, gatherPostList);

        List<AskPostLike> askPostLikeList = askPostLikeRepository.findAllByMemberOrderByPostLikeIdDesc(member);
        List<AskPost> askPostList = new ArrayList<>();
        for (AskPostLike askPostLike : askPostLikeList) {
            askPostList.add(askPostLike.getAskPost());
        }
        List<AskPostAllResDto> myAskPost = toMyAskPost(member, askPostList);

        MyPageMyPostResDto myPageMyPostResDto = new MyPageMyPostResDto(myEventPost, myGatherPost, myAskPost);
        return GlobalResDto.success(myPageMyPostResDto, "스크랩한 글을 가져왔습니다");
    }

    public Member isPresentMember(UserDetailsImpl userDetails) {
        return memberRepository.findById(userDetails.getAccount().getMemberId())
                .orElseThrow(()-> new CustomException(ErrorCode.NOT_FOUND_MEMBER));
    }

    public String[] toEmail(Member member) {
        String[] result = new String[2];
        if (member.getEmail().startsWith("k_")) {
            result[0] = "kakao";
            result[1] = member.getEmail().replace("k_", "");
            return result;
        } else if (member.getEmail().startsWith("g_")) {
            result[0] = "google";
            result[1] = member.getEmail().replace("g_", "");
            return result;
        } else if (member.getEmail().startsWith("n_")) {
            result[0] = "naver";
            result[1] = member.getEmail().replace("n_", "");
            return result;
        } else {
            result[0] = "github";
            result[1] = member.getEmail().replace("git_", "");
            return result;
        }
    }

    public List<EventPostAllResDto> toMyEventPost(Member member, List<EventPost> eventPostList) {
        List<EventPostAllResDto> myEventPost = new ArrayList<>();
        for (EventPost eventPost : eventPostList) {
            boolean bookmark = eventPostLikeRepository.existsByMemberAndEventPost(member, eventPost);
            myEventPost.add(EventPostAllResDto.toEPARD(eventPost, bookmark));
        }
        return myEventPost;
    }

    public List<GatherPostAllResDto> toMyGatherPost(Member member, List<GatherPost> gatherPostList) {
        List<GatherPostAllResDto> myGatherPost = new ArrayList<>();
        for (GatherPost gatherPost : gatherPostList) {
            boolean bookmark = gatherPostLikeRepository.existsByMemberAndGatherPost(member, gatherPost);
            myGatherPost.add(GatherPostAllResDto.toGPARD(gatherPost, bookmark));
        }
        return myGatherPost;
    }

    public List<AskPostAllResDto> toMyAskPost(Member member, List<AskPost> askPostList) {
        List<AskPostAllResDto> myAskPost = new ArrayList<>();
        for (AskPost askPost : askPostList) {
            boolean bookmark = askPostLikeRepository.existsByMemberAndAskPost(member, askPost);
            myAskPost.add(AskPostAllResDto.toAPARD(askPost, bookmark));
        }
        return myAskPost;
    }

    public String createNickName() {
        return comfortUtils.makeUserNickName();
    }
}

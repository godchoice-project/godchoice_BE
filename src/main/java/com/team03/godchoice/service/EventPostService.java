package com.team03.godchoice.service;

import com.team03.godchoice.domain.Member;
import com.team03.godchoice.domain.domainenum.DeleteStatus;
import com.team03.godchoice.domain.domainenum.RegionTag;
import com.team03.godchoice.domain.eventpost.EventPost;
import com.team03.godchoice.domain.eventpost.EventPostComment;
import com.team03.godchoice.domain.eventpost.EventPostImg;
import com.team03.godchoice.dto.GlobalResDto;
import com.team03.godchoice.dto.requestDto.eventpostDto.EventPostPutReqDto;
import com.team03.godchoice.dto.requestDto.eventpostDto.EventPostReqDto;
import com.team03.godchoice.dto.responseDto.CommentDto;
import com.team03.godchoice.dto.responseDto.PostImgResDto;
import com.team03.godchoice.dto.responseDto.eventpost.EventPostResDto;
import com.team03.godchoice.exception.CustomException;
import com.team03.godchoice.exception.ErrorCode;
import com.team03.godchoice.interfacepackage.MakeRegionTag;
import com.team03.godchoice.repository.MemberRepository;
import com.team03.godchoice.repository.eventpost.EventPostImgRepository;
import com.team03.godchoice.repository.eventpost.EventPostRepository;
import com.team03.godchoice.s3.S3Uploader;
import com.team03.godchoice.security.jwt.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class EventPostService implements MakeRegionTag {

    private final MemberRepository memberRepository;
    private final EventPostRepository eventPostRepository;
    private final EventPostImgRepository eventPostImgRepository;
    private final S3Uploader s3Uploader;

    @Transactional
    public GlobalResDto<?> createEventPost(UserDetailsImpl userDetails, EventPostReqDto eventPostReqDto, List<MultipartFile> multipartFiles) throws IOException {
        Member member = isPresentMember(userDetails);
        if (member == null) {
            throw new CustomException(ErrorCode.NOT_FOUND_MEMBER);
        }

        //시작시간,만료시간 localDate로 바꾸고 주소 태그만들고
        LocalDate startPeriod = LocalDate.parse(eventPostReqDto.getStartPeriod(), DateTimeFormatter.ISO_DATE);
        LocalDate endPeriod = LocalDate.parse(eventPostReqDto.getEndPeriod(), DateTimeFormatter.ISO_DATE);
        RegionTag regionTag = toRegionTag(eventPostReqDto.getPostAddress());
        String eventStatus = toEventStatus(endPeriod);

        EventPost eventPost = new EventPost(eventPostReqDto, member, startPeriod, endPeriod, regionTag, eventStatus);
        eventPostRepository.save(eventPost);

        saveImg(multipartFiles, eventPost);

        return GlobalResDto.success(null, "작성완료");
    }

    @Transactional
    public GlobalResDto<?> putEventPost(UserDetailsImpl userDetails, Long postId, EventPostPutReqDto eventPostPutReqDto, List<MultipartFile> multipartFiles) throws IOException {

        Member member = isPresentMember(userDetails);
        if (member == null) {
            throw new CustomException(ErrorCode.NOT_FOUND_MEMBER);
        }

        EventPost eventPost = isPresentPost(postId);
        if (eventPost == null) {
            throw new CustomException(ErrorCode.NOT_FOUND_POST);
        }

        if (!eventPost.getMember().getEmail().equals(member.getEmail())) {
            throw new CustomException(ErrorCode.NO_PERMISSION_CHANGE);
        }

        LocalDate startPeriod = LocalDate.parse(eventPostPutReqDto.getStartPeriod(), DateTimeFormatter.ISO_DATE);
        LocalDate endPeriod = LocalDate.parse(eventPostPutReqDto.getEndPeriod(), DateTimeFormatter.ISO_DATE);
        RegionTag regionTag = toRegionTag(eventPostPutReqDto.getPostAddress());
        String eventStatus = toEventStatus(endPeriod);

        eventPost.update(eventPostPutReqDto, member, startPeriod, endPeriod, regionTag, eventStatus);

        String[] imgIdList;

        if (eventPostPutReqDto.getImgId() != null || !eventPostPutReqDto.getImgId().trim().isEmpty() || !eventPostPutReqDto.getImgId().trim().isBlank()) {
            imgIdList = eventPostPutReqDto.getImgId().split(",");
            for (String imgUrl : imgIdList) {
                Long imgId = Long.valueOf(imgUrl);
                EventPostImg eventPostImg = eventPostImgRepository.findById(imgId).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_IMG));
                String s3Path = toImgPath(eventPostImg);
                s3Uploader.delImg(s3Path);
                eventPostImgRepository.deleteById(imgId);
            }

        }

        saveImg(multipartFiles, eventPost);

        return GlobalResDto.success(null, "수정이 완료되었습니다");
    }

    @Transactional
    public GlobalResDto<?> deleteEventPost(UserDetailsImpl userDetails, Long postId) {

        Member member = isPresentMember(userDetails);
        if (member == null) {
            throw new CustomException(ErrorCode.NOT_FOUND_MEMBER);
        }

        EventPost eventPost = isPresentPost(postId);
        if (eventPost == null) {
            throw new CustomException(ErrorCode.NOT_FOUND_POST);
        }

        if (!eventPost.getMember().getEmail().equals(member.getEmail())) {
            throw new CustomException(ErrorCode.NO_PERMISSION_DELETE);
        }

        List<EventPostImg> eventPostImgs = eventPost.getPostImgUrl();
        if (eventPostImgs.size() != 0) {
            for (EventPostImg eventPostImg : eventPostImgs) {
                String imgUrl = toImgPath(eventPostImg);
                s3Uploader.delImg(imgUrl);
            }
        }

        eventPostRepository.deleteById(postId);

        return GlobalResDto.success(null, "삭제가 완료되었습니다");
    }

    public String toImgPath(EventPostImg eventPostImg) {
        List<String> list = List.of(eventPostImg.getImgUrl().split("/"));
        return list.get(3) + "/" + list.get(4);
    }

    public GlobalResDto<?> getOneEventPost(UserDetailsImpl userDetails, Long postId,
                                           HttpServletRequest req, HttpServletResponse res) {
        viewCountUp(postId, req, res);

        Member member = isPresentMember(userDetails);
        if (member == null) {
            throw new CustomException(ErrorCode.NOT_FOUND_MEMBER);
        }

        EventPost eventPost = eventPostRepository.findByEventPostId(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_POST));

        List<EventPostImg> eventPostImgs = new ArrayList<>(eventPost.getPostImgUrl());
        List<PostImgResDto> postImgResDtos = new ArrayList<>();
        if (eventPostImgs.size() == 0) {
            postImgResDtos.add(new PostImgResDto("https://eunibucket.s3.ap-northeast-2.amazonaws.com/testdir/normal_profile.jpg", null));
        } else {
            for (EventPostImg eventPostImg : eventPostImgs) {
                postImgResDtos.add(new PostImgResDto(eventPostImg.getImgUrl(), eventPostImg.getEventPostImgId().toString()));
            }
        }

        List<CommentDto> commentDtoList = new ArrayList<>();
        for (EventPostComment comment : eventPost.getComments()) {
            if (comment.getParent() == null) {
                commentDtoList.add(0,new CommentDto(comment));
            }
        }

        return GlobalResDto.success(new EventPostResDto(eventPost, postImgResDtos, commentDtoList), null);

    }

    public Member isPresentMember(UserDetailsImpl userDetails) {
        Optional<Member> member = memberRepository.findById(userDetails.getAccount().getMemberId());
        return member.orElse(null);
    }

    public EventPost isPresentPost(Long postId) {
        Optional<EventPost> post = eventPostRepository.findById(postId);
        return post.orElse(null);
    }

    //행사가 진행중인 종료되었는지 판별하는 메서드
    public String toEventStatus(LocalDate endPeriod) {
        LocalDate now = LocalDate.now();
        if (endPeriod.isBefore(now)) {
            return "마감";
        }
        return "진행중";
    }

    //DB와 S3에 이미지를 저장하는 메서드
    public void saveImg(List<MultipartFile> multipartFiles, EventPost eventPost) throws IOException {
        String eventPostUrl;
        if (multipartFiles != null) {
            for (MultipartFile file : multipartFiles) {
                eventPostUrl = s3Uploader.uploadFiles(file, "testdir");
                EventPostImg eventPostImg = new EventPostImg(eventPostUrl, eventPost);
                eventPostImgRepository.save(eventPostImg);
            }
        }
    }

    public void viewCountUp(Long id, HttpServletRequest req, HttpServletResponse res) {

        Cookie oldCookie = null;

        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("postView")) {
                    oldCookie = cookie;
                }
            }
        }

        if (oldCookie != null) {
            if (!oldCookie.getValue().contains("[" + id.toString() + "]")) {
                viewCountUp(id);
                oldCookie.setValue(oldCookie.getValue() + "_[" + id + "]");
                oldCookie.setPath("/");
                oldCookie.setMaxAge(60 * 60 * 24);
                res.addCookie(oldCookie);
            }
        } else {
            viewCountUp(id);
            Cookie newCookie = new Cookie("postView", "[" + id + "]");
            newCookie.setPath("/");
            newCookie.setMaxAge(60 * 60 * 24);
            res.addCookie(newCookie);
        }
    }

    @Transactional
    public void viewCountUp(Long eventPostId) {
        EventPost eventPost = eventPostRepository.findByEventPostId(eventPostId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_POST));
        eventPost.viewCountUp();
        eventPostRepository.save(eventPost);
    }
}

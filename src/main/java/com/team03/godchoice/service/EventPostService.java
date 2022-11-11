package com.team03.godchoice.service;

import com.team03.godchoice.domain.Member;
import com.team03.godchoice.domain.domainenum.RegionTag;
import com.team03.godchoice.domain.eventpost.EventPost;
import com.team03.godchoice.domain.eventpost.EventPostImg;
import com.team03.godchoice.dto.GlobalResDto;
import com.team03.godchoice.dto.requestDto.EventPostPutReqDto;
import com.team03.godchoice.dto.requestDto.EventPostReqDto;
import com.team03.godchoice.exception.CustomException;
import com.team03.godchoice.exception.ErrorCode;
import com.team03.godchoice.repository.MemberRepository;
import com.team03.godchoice.repository.eventpost.EventPostImgRepository;
import com.team03.godchoice.repository.eventpost.EventPostRepository;
import com.team03.godchoice.s3.S3Uploader;
import com.team03.godchoice.security.jwt.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventPostService {

    private final MemberRepository memberRepository;
    private final EventPostRepository eventPostRepository;
    private final EventPostImgRepository eventPostImgRepository;
    private final S3Uploader s3Uploader;

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

        saveImg(multipartFiles,eventPost);

        return GlobalResDto.success(null, "작성완료");
    }

    public GlobalResDto<?> putEventPost(UserDetailsImpl userDetails, Long postId, EventPostPutReqDto eventPostPutReqDto, List<MultipartFile> multipartFiles) throws IOException {

        Member member = isPresentMember(userDetails);
        if (member == null) {
            throw new CustomException(ErrorCode.NOT_FOUND_MEMBER);
        }

        EventPost eventPost = isPresentPost(postId);
        if (eventPost == null) {
            throw new CustomException(ErrorCode.NOT_FOUND_EVENTPOST);
        }

        if(!eventPost.getMember().getEmail().equals(member.getEmail())){
            throw new CustomException(ErrorCode.NO_PERMISSION_CHANGE);
        }

        LocalDate startPeriod = LocalDate.parse(eventPostPutReqDto.getStartPeriod(), DateTimeFormatter.ISO_DATE);
        LocalDate endPeriod = LocalDate.parse(eventPostPutReqDto.getEndPeriod(), DateTimeFormatter.ISO_DATE);
        RegionTag regionTag = toRegionTag(eventPostPutReqDto.getPostAddress());
        String eventStatus = toEventStatus(endPeriod);

        eventPost.update(eventPostPutReqDto, member, startPeriod, endPeriod, regionTag, eventStatus);

        String[] imgIdList = eventPostPutReqDto.getImgId().split(",");
        if (imgIdList.length==eventPost.getPostImgUrl().size()) {  //저장되어있는 사진 리스트 크기와 받아온 숫자 리스트 크기가 같다면 올린 사진을 모두 삭제하는것이므로 기본이미지 넣기
            List<EventPostImg> eventPostImgs = eventPostImgRepository.findAllByEventPost(eventPost);
            for(EventPostImg eventPostImg : eventPostImgs){
                String imgUrl = eventPostImg.getImgUrl().substring(50);
                s3Uploader.delImg(imgUrl);
            }

            eventPostImgRepository.deleteAllByEventPost(eventPost);

            String eventPostUrl = "https://eunibucket.s3.ap-northeast-2.amazonaws.com/testdir/normal_profile.jpg";
            EventPostImg eventPostImg = new EventPostImg(eventPostUrl, eventPost);
            eventPostImgRepository.save(eventPostImg);

        }else if(imgIdList.length != 0){  //숫자리스트가 0이 아니라면 삭제할 사진이 존재하므로 사진 삭제
            for(String imgId : imgIdList){
                Long eventPostId = Long.valueOf(imgId);
                EventPostImg eventPostImg = eventPostImgRepository.findByEventPostImgId(eventPostId);
                String path = eventPostImg.getImgUrl().substring(50);
                s3Uploader.delImg(path);
                eventPostImgRepository.deleteById(eventPostId);
            }
        }

        saveImg(multipartFiles,eventPost);
        return GlobalResDto.success(null,"수정이 완료되었습니다");
    }

    public GlobalResDto<?> deleteEventPost(UserDetailsImpl userDetails, Long postId) {

        Member member = isPresentMember(userDetails);
        if (member == null) {
            throw new CustomException(ErrorCode.NOT_FOUND_MEMBER);
        }

        EventPost eventPost = isPresentPost(postId);
        if (eventPost == null) {
            throw new CustomException(ErrorCode.NOT_FOUND_EVENTPOST);
        }

        if(!eventPost.getMember().getEmail().equals(member.getEmail())){
            throw new CustomException(ErrorCode.NO_PERMISSION_DELETE);
        }

        List<EventPostImg> eventPostImgs = eventPost.getPostImgUrl();
        for(EventPostImg eventPostImg : eventPostImgs){
            String imgUrl = eventPostImg.getImgUrl().substring(50);
            s3Uploader.delImg(imgUrl);
        }

        eventPostRepository.deleteById(postId);

        return GlobalResDto.success(null,"삭제가 완료되었습니다");
    }

    public Member isPresentMember(UserDetailsImpl userDetails) {
        Optional<Member> member = memberRepository.findById(userDetails.getAccount().getMemberId());
        return member.orElse(null);
    }

    public EventPost isPresentPost(Long postId) {
        Optional<EventPost> post = eventPostRepository.findById(postId);
        return post.orElse(null);
    }

    //지역태그 만드는 메서드
    public RegionTag toRegionTag(String region) {
        if (region.startsWith("서")) {
            return RegionTag.서울;
        } else if (region.startsWith("경기")) {
            return RegionTag.경기도;
        } else if (region.startsWith("강")) {
            return RegionTag.강원도;
        } else if (region.startsWith("경")) {
            return RegionTag.경상도;
        } else if (region.startsWith("전")) {
            return RegionTag.전라도;
        } else if (region.startsWith("충")) {
            return RegionTag.충청도;
        } else {
            return RegionTag.제주도;
        }
    }

    //행사가 진행중인 종료되었는지 판별하는 메서드
    public String toEventStatus(LocalDate endPeriod) {
        LocalDate now = LocalDate.now();
        if (endPeriod.isBefore(now)) {
            return "종료";
        }
        return "진행중";
    }

    //DB와 S3에 이미지를 저장하는 메서드
    public void saveImg(List<MultipartFile> multipartFiles,EventPost eventPost) throws IOException {
        String eventPostUrl;
        if (multipartFiles.size() != 0) {
            for (MultipartFile file : multipartFiles) {
                eventPostUrl = s3Uploader.uploadFiles(file, "testdir");
                EventPostImg eventPostImg = new EventPostImg(eventPostUrl, eventPost);
                eventPostImgRepository.save(eventPostImg);
            }
        } else {
            eventPostUrl = "https://eunibucket.s3.ap-northeast-2.amazonaws.com/testdir/normal_profile.jpg";
            EventPostImg eventPostImg = new EventPostImg(eventPostUrl, eventPost);
            eventPostImgRepository.save(eventPostImg);
        }
    }
}

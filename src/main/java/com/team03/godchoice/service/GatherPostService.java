package com.team03.godchoice.service;

import com.team03.godchoice.domain.Member;
import com.team03.godchoice.domain.domainenum.RegionTag;
import com.team03.godchoice.domain.gatherPost.GatherPost;
import com.team03.godchoice.domain.gatherPost.GatherPostImg;
import com.team03.godchoice.dto.GlobalResDto;
import com.team03.godchoice.dto.requestDto.GatherPostRequestDto;
import com.team03.godchoice.dto.requestDto.GatherPostUpdateDto;
import com.team03.godchoice.exception.CustomException;
import com.team03.godchoice.exception.ErrorCode;
import com.team03.godchoice.repository.gatherpost.GatherPostImgRepository;
import com.team03.godchoice.repository.gatherpost.GatherPostRepository;
import com.team03.godchoice.repository.MemberRepository;
import com.team03.godchoice.s3.S3Uploader;
import com.team03.godchoice.security.jwt.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GatherPostService {
    private final MemberRepository memberRepository;
    private final GatherPostRepository gatherPostRepository;
    private final GatherPostImgRepository gatherPostImgRepository;
    private final EventPostService eventPostService;
    private final S3Uploader s3Uploader;

    public GlobalResDto<?> createGather(GatherPostRequestDto gatherPostDto, List<MultipartFile> multipartFile, UserDetailsImpl userDetails) throws IOException {

        Member member = memberRepository.findById(userDetails.getMember().getMemberId()).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_MEMBER)
        );
        // 만남시간, lacalDate로 바꾸고 주소 태그만들고
        LocalDate date = LocalDate.parse(gatherPostDto.getDate(), DateTimeFormatter.ISO_DATE);
        RegionTag regionTag = eventPostService.toRegionTag(gatherPostDto.getPostAddress());
        String eventStatus = eventPostService.toEventStatus(date);

        // dto내용과 사용자 저장
        GatherPost gatherPost = new GatherPost(gatherPostDto,member,date,regionTag,eventStatus);
        gatherPostRepository.save(gatherPost);

        // List로 image받은후 저장
        String gatherPostUrl;
        if (multipartFile.size() != 0) {
            for (MultipartFile file : multipartFile) {
                gatherPostUrl = s3Uploader.uploadFiles(file, "testdir");
                GatherPostImg gatherPostImg = new GatherPostImg(gatherPostUrl, gatherPost);
                gatherPostImgRepository.save(gatherPostImg);
            }
        } else {
            gatherPostUrl = "https://kimbiibucket.s3.ap-northeast-2.amazonaws.com/normal_profile.jpg";
            GatherPostImg gatherPostImg = new GatherPostImg(gatherPostUrl, gatherPost);
            gatherPostImgRepository.save(gatherPostImg);
        }

        return GlobalResDto.success(null, "success create gatherPost");
    }

    public GlobalResDto<?> updateGatherPost(Long postId, GatherPostUpdateDto gatherPostDto, List<MultipartFile> multipartFiles) {

        GatherPost gatherPost = gatherPostRepository.findById(postId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_POST)
        );
        LocalDate date = LocalDate.parse(gatherPostDto.getDate(), DateTimeFormatter.ISO_DATE);
        RegionTag regionTag = eventPostService.toRegionTag(gatherPostDto.getPostAddress());
        String eventStatus = eventPostService.toEventStatus(date);
        gatherPost.update(gatherPostDto,date,regionTag,eventStatus);

        String[] imgIdlist = gatherPostDto.getImgId().split(",");
        //저장되어있는 사진 리스트 크기와 받아온 숫자 리스트 크기가 같다면 올린 사진을 모두 삭제하는것이므로 기본이미지 넣기
//        if (imgIdlist.length==gatherPost.getGatherPostImgs().size()) {
//
//        }

        return GlobalResDto.success(null,"수정이 완료되었습니다.");
    }
}

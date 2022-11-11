package com.team03.godchoice.service;

import com.team03.godchoice.domain.Comment;
import com.team03.godchoice.domain.Member;
import com.team03.godchoice.domain.askpost.AskPost;
import com.team03.godchoice.domain.askpost.AskPostImg;
import com.team03.godchoice.dto.GlobalResDto;
import com.team03.godchoice.dto.requestDto.AskPostPutRequestDto;
import com.team03.godchoice.dto.requestDto.AskPostRequestDto;
import com.team03.godchoice.dto.responseDto.AskPostDetailResponseDto;
import com.team03.godchoice.dto.responseDto.AskPostResponseDto;
import com.team03.godchoice.dto.responseDto.CommentDto;
import com.team03.godchoice.exception.CustomException;
import com.team03.godchoice.exception.ErrorCode;
import com.team03.godchoice.repository.CommentRepository;
import com.team03.godchoice.repository.askpost.AskPostRepository;
import com.team03.godchoice.repository.askpost.AskPostImgRepository;
import com.team03.godchoice.s3.S3Uploader;
import com.team03.godchoice.security.jwt.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AskPostService {

    private final AskPostRepository askPostRepository;
    private final S3Uploader s3Uploader;
    private final AskPostImgRepository askPostImgRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public GlobalResDto<?> createAskPost(
            AskPostRequestDto askPostRequestDto, List<MultipartFile> multipartFile, UserDetailsImpl userDetails) throws IOException {

        // 유저 정보로 Post객체 생성
        AskPost askPost = new AskPost(askPostRequestDto, userDetails.getMember());
        askPostRepository.save(askPost);

        // List로 image받은후 저장
        if(!(multipartFile.size()==0)) {

            for (MultipartFile file : multipartFile) {
                String img = s3Uploader.uploadFiles(file, "testdir");

                AskPostImg image = new AskPostImg(img, askPost);
                askPostImgRepository.save(image);
            }
        }

        return GlobalResDto.success(null,"success create askPost");
    }

    @Transactional
    public GlobalResDto<?> updateAskPost(
            Long postId, AskPostPutRequestDto askPostPutRequestDto, List<MultipartFile> multipartFile, UserDetailsImpl userDetails) throws IOException {

        Member member = userDetails.getMember();

        AskPost askPost=askPostRepository.findById(postId)
                .orElseThrow(()->new CustomException(ErrorCode.NOT_FOUND_POST));

        // 작성자만 수정할수 있게 예외처리
        if(!member.getMemberId().equals(askPost.getMember().getMemberId())){
            throw new CustomException(ErrorCode.NOT_MATCH_MEMBER);
        }

        // 게시글 수정
        askPost.updateAskPost(askPostPutRequestDto);

        String[] imgIdList = {};

        if (askPostPutRequestDto.getImgId().length() == 1)  {
            imgIdList = new String[]{askPostPutRequestDto.getImgId()};
        }else {
            imgIdList = askPostPutRequestDto.getImgId().split(",");
        }

        // 기존에 있는 Image삭제
        for(String imgId : imgIdList) {
            Long imageId = Long.parseLong(imgId);
            askPostImgRepository.deleteById(imageId);
        }

        // List로 image받은후 저장
        if(!(multipartFile.size()==0)) {

            for (MultipartFile file : multipartFile) {
                String img = s3Uploader.uploadFiles(file, "testdir");

                AskPostImg image = new AskPostImg(img, askPost);
                askPostImgRepository.save(image);
            }
        }

        return GlobalResDto.success(null,"success update askPost");

    }

    @Transactional
    public GlobalResDto<?> deletePost(Long postId, UserDetailsImpl userDetails) {

        Member member = userDetails.getMember();

        AskPost askPost=askPostRepository.findById(postId)
                .orElseThrow(()->new CustomException(ErrorCode.NOT_FOUND_POST));

        // 작성자만 삭제할수 있게 예외처리
        if(!member.getMemberId().equals(askPost.getMember().getMemberId())){
            throw new CustomException(ErrorCode.NOT_MATCH_MEMBER);
        }

        // 삭제
        askPostRepository.deleteById(postId);


        return GlobalResDto.success(null,"success delete askPost");

    }

    @Transactional
    public List<AskPostResponseDto> getAllAskPost() {

        List<AskPost> askPostList = askPostRepository.findAllByOrderByCreatedAtDesc();

        List<AskPostResponseDto> askPostResponseDtoList = new ArrayList<>();
        for(AskPost askPost : askPostList){
            askPostResponseDtoList.add(new AskPostResponseDto(askPost));
        }

        return askPostResponseDtoList;
    }

    @Transactional
    public AskPostDetailResponseDto getOneAskPost(Long postId) {
        AskPost askPost=askPostRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_POST));

        // 이미지 개수만큼 리스트에 추가
        List<AskPostImg> askPostImgList = new ArrayList<>();
        for (AskPostImg askPostImg : askPost.getAskPostImg()) {
            askPostImgList.add(askPostImg);
        }

        List<CommentDto> commentDtoList = new ArrayList<>();
        for(Comment comment : askPost.getComments()){
            if(comment.getParent() == null){
                commentDtoList.add(new CommentDto(comment));
            }
        }

        return new AskPostDetailResponseDto(askPost, askPostImgList, commentDtoList);
    }
}

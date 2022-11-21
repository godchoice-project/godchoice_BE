package com.team03.godchoice.service;

import com.team03.godchoice.domain.askpost.AskPostComment;
import com.team03.godchoice.domain.Member;
import com.team03.godchoice.domain.askpost.AskPost;
import com.team03.godchoice.domain.askpost.AskPostImg;
import com.team03.godchoice.dto.GlobalResDto;
import com.team03.godchoice.dto.requestDto.askpostDto.AskPostPutRequestDto;
import com.team03.godchoice.dto.requestDto.askpostDto.AskPostRequestDto;
import com.team03.godchoice.dto.responseDto.PostImgResDto;
import com.team03.godchoice.dto.responseDto.askpost.AskPostResponseDto;
import com.team03.godchoice.dto.responseDto.CommentDto;
import com.team03.godchoice.exception.CustomException;
import com.team03.godchoice.exception.ErrorCode;
import com.team03.godchoice.repository.MemberRepository;
import com.team03.godchoice.repository.askpost.AskPostImgRepository;
import com.team03.godchoice.repository.askpost.AskPostRepository;
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
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AskPostService {

    private final AskPostRepository askPostRepository;
    private final S3Uploader s3Uploader;
    private final AskPostImgRepository askPostImgRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public GlobalResDto<?> createAskPost(
            AskPostRequestDto askPostRequestDto, List<MultipartFile> multipartFile, UserDetailsImpl userDetails) throws IOException {

        // 유저 정보로 Post객체 생성
        AskPost askPost = new AskPost(askPostRequestDto, userDetails.getMember());
        askPostRepository.save(askPost);

        // List로 image받은후 저장
        if(multipartFile!=null) {

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

        String[] imgIdList;

        if(askPostPutRequestDto.getImgId().length()>0){
            imgIdList = askPostPutRequestDto.getImgId().split(",");
            for(String imgId : imgIdList) {
                Long imageId = Long.parseLong(imgId);
                AskPostImg askPostImg = askPostImgRepository.findById(imageId).orElseThrow(()-> new CustomException(ErrorCode.NOT_FOUND_IMG));
                String s3Path = toImgPath(askPostImg);
                s3Uploader.delImg(s3Path);
                askPostImgRepository.deleteById(imageId);
            }
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

        List<AskPostImg> askPostImgList = askPost.getAskPostImg();
        if(askPostImgList.size()!=0){
            for(AskPostImg askPostImg : askPostImgList){
                String imgUrl = toImgPath(askPostImg);
                s3Uploader.delImg(imgUrl);
            }
        }

        // 삭제
        askPostRepository.deleteById(postId);


        return GlobalResDto.success(null,"success delete askPost");

    }

    @Transactional
    public GlobalResDto<?> getOneAskPost(Long postId,UserDetailsImpl userDetails) {
        viewCountUp(postId,userDetails.getAccount());

        AskPost askPost=askPostRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_POST));

        // 이미지 개수만큼 리스트에 추가
        List<AskPostImg> askPostImgList = new ArrayList<>(askPost.getAskPostImg());
        List<PostImgResDto> postImgResDtos = new ArrayList<>();
        if(askPostImgList.size()==0){
            postImgResDtos.add(new PostImgResDto("https://eunibucket.s3.ap-northeast-2.amazonaws.com/testdir/normal_profile.jpg",null));
        }else{
            for(AskPostImg askPostImg : askPostImgList){
                postImgResDtos.add(new PostImgResDto(askPostImg.getImage(),askPostImg.getImageId().toString()));
            }
        }

        List<CommentDto> commentDtoList = new ArrayList<>();
        for(AskPostComment comment : askPost.getComments()){
            if(comment.getParent() == null){
                commentDtoList.add(0,new CommentDto(comment));
            }
        }

        return GlobalResDto.success(new AskPostResponseDto(askPost, askPostImgList, commentDtoList),null);
    }

    public String toImgPath(AskPostImg askPostImg){
        List<String> list = List.of(askPostImg.getImage().split("/"));
        return list.get(3)+"/"+list.get(4);
    }

    public void viewCountUp(Long postId,Member member) {
        if(member.getPostView()==null || !member.getPostView().contains("[a_" + postId.toString() + "]")){
            member.updatePostView("[a_" + postId+ "],");
            memberRepository.save(member);
            viewCountUp(postId);
        }
    }

    @javax.transaction.Transactional
    public void viewCountUp(Long askPostId) {
        AskPost askPost = askPostRepository.findByAskPostId(askPostId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_POST));
        askPost.viewCountUp();
        askPostRepository.save(askPost);
    }
}

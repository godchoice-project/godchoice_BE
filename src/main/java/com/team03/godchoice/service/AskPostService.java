package com.team03.godchoice.service;

import com.team03.godchoice.domain.AskPost;
import com.team03.godchoice.domain.AskPostImg;
import com.team03.godchoice.dto.GlobalResDto;
import com.team03.godchoice.dto.requestDto.AskPostRequestDto;
import com.team03.godchoice.repository.AskPostRepository;
import com.team03.godchoice.repository.AskPostImgRepository;
import com.team03.godchoice.s3.S3Uploader;
import com.team03.godchoice.security.jwt.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AskPostService {

    private final AskPostRepository askPostRepository;
    private final S3Uploader s3Uploader;
    private final AskPostImgRepository askPostImgRepository;


    public GlobalResDto<?> createAskPost(
            AskPostRequestDto askPostRequestDto, List<MultipartFile> multipartFile, UserDetailsImpl userDetails) throws IOException {

        // 유저 정보로 Post객체 생성
        AskPost askPost = new AskPost(askPostRequestDto, userDetails.getMember());
        askPostRepository.save(askPost);

        // List로 image받은후 저장
        if(!(multipartFile.size()==0)) {

            System.out.println(multipartFile.get(0).getOriginalFilename());

            for (MultipartFile file : multipartFile) {
                String img = s3Uploader.uploadFiles(file, "testdir");

                AskPostImg askPostImg = new AskPostImg(img, askPost);
                askPostImgRepository.save(askPostImg);
            }
        }

        return GlobalResDto.success(null,"success create askPost");
    }
}

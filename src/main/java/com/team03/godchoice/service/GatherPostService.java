package com.team03.godchoice.service;

import com.team03.godchoice.domain.GatherPost;
import com.team03.godchoice.domain.GatherPostImg;
import com.team03.godchoice.dto.GlobalResDto;
import com.team03.godchoice.dto.requestDto.GatherPostRequestDto;
import com.team03.godchoice.exception.CustomException;
import com.team03.godchoice.exception.ErrorCode;
import com.team03.godchoice.repository.GatherPostImgRepository;
import com.team03.godchoice.repository.GatherPostRepository;
import com.team03.godchoice.s3.S3Uploader;
import com.team03.godchoice.security.jwt.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GatherPostService {

    private final GatherPostRepository gatherPostRepository;
    private final S3Uploader s3Uploader;
    private final GatherPostImgRepository gatherPostImgRepository;
    public GlobalResDto<?> createGather(GatherPostRequestDto gatherPostDto, List<MultipartFile> multipartFile, UserDetailsImpl userDetails) throws IOException {

        GatherPost gatherPost = new GatherPost(gatherPostDto, userDetails.getMember());

        // List로 image받은후 저장
        if(!(multipartFile.size()==0)) {

            System.out.println(multipartFile.get(0).getOriginalFilename());

            for (MultipartFile file : multipartFile) {
                String img = s3Uploader.uploadFiles(file, "testdir");

                GatherPostImg gatherPostImg = new GatherPostImg(img, gatherPost);
                gatherPostImgRepository.save(gatherPostImg);
            }
        }
        gatherPostRepository.save(gatherPost);
        return GlobalResDto.success(null, "success create gatherPost");
    }

    public GlobalResDto<?> getGather(Long gatherPostId, UserDetailsImpl userDetails) {

        GatherPost gatherPost = gatherPostRepository.findById(gatherPostId).orElseThrow(
                () -> new CustomException(ErrorCode.GATHERPOST_NOT_FOUND)
        );
        if (gatherPost.getMember().getMemberId().equals(userDetails.getMember().getMemberId())) {

        } else {
            throw new CustomException(ErrorCode.NOT_FOUND_MEMBER);
        }



        return GlobalResDto.success(null,"");
    }
}

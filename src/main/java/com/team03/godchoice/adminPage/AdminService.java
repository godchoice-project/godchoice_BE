package com.team03.godchoice.adminPage;

import com.team03.godchoice.domain.Member;
import com.team03.godchoice.dto.GlobalResDto;
import com.team03.godchoice.enumclass.Role;
import com.team03.godchoice.exception.CustomException;
import com.team03.godchoice.exception.ErrorCode;
import com.team03.godchoice.repository.MemberRepository;
import com.team03.godchoice.s3.S3Uploader;
import com.team03.godchoice.security.jwt.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final MemberRepository memberRepository;
    private final S3Uploader s3Uploader;
    private final AdminPageRepository adminPageRepository;

    public GlobalResDto<?> createAdminPost(AdminPostReqDto adminPostReqDto, MultipartFile multipartFile, UserDetailsImpl userDetails) throws IOException {
        Member member = isPresentMember(userDetails);

        if(!member.getRole().equals(Role.ADMIN)){
            throw  new CustomException(ErrorCode.USER_ERROR);
        }

        String postLink = s3Uploader.uploadFiles(multipartFile,"testdir");
        AdminPage adminPage = new AdminPage(adminPostReqDto,postLink);
        adminPageRepository.save(adminPage);
        return GlobalResDto.success(null,"작성완료");
    }

    public GlobalResDto<?> deleteAdminPost(Long id, UserDetailsImpl userDetails) {
        Member member = isPresentMember(userDetails);

        if(!member.getRole().equals(Role.ADMIN)){
            throw  new CustomException(ErrorCode.USER_ERROR);
        }

        AdminPage adminPage =adminPageRepository.findById(id).orElseThrow(()-> new CustomException(ErrorCode.NOT_FOUND_POST));

        String imgPath = toImgPath(adminPage.getImgLink());
        s3Uploader.delImg(imgPath);

        adminPageRepository.deleteById(id);
        return GlobalResDto.success(null,"삭제완료");
    }

    public GlobalResDto<Object> getAdminPost() {
        List<AdminPage> adminPageList = adminPageRepository.findAll();
        return GlobalResDto.success(adminPageList,null);
    }

    public Member isPresentMember(UserDetailsImpl userDetails) {
        return memberRepository.findById(userDetails.getAccount().getMemberId())
                .orElseThrow(()-> new CustomException(ErrorCode.NOT_FOUND_MEMBER));
    }

    public String toImgPath(String imgUrl) {
        List<String> list = List.of(imgUrl.split("/"));
        return list.get(3) + "/" + list.get(4);
    }
}

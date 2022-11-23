package com.team03.godchoice.adminPage;

import com.team03.godchoice.dto.GlobalResDto;
import com.team03.godchoice.security.jwt.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@RestController
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/adminpost")
    public GlobalResDto<?> createAdminPost(@RequestPart AdminPostReqDto adminPostReqDto,
                                           @RequestPart(required = false)MultipartFile multipartFile,
                                           @ApiIgnore @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        return adminService.createAdminPost(adminPostReqDto, multipartFile,userDetails);
    }

    @DeleteMapping("/adminpost/{id}")
    public GlobalResDto<?> deleteAdminPost(@PathVariable Long id,
                                           @ApiIgnore @AuthenticationPrincipal UserDetailsImpl userDetails){
        return adminService.deleteAdminPost(id, userDetails);
    }

    @GetMapping("/getadminpost")
    public GlobalResDto<?> getAdminPost(){
        return adminService.getAdminPost();
    }
}

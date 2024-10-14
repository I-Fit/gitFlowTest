package kr.co.ifit.api.controller;

import kr.co.ifit.api.response.ProfileDtoRes;
import kr.co.ifit.api.service.ProfileService;
import kr.co.ifit.common.util.UserContextUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.Map;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;
    private final UserContextUtil userContextUtil;

    //   프로필 이미지 업로드
    @PostMapping("/upload")
    public ResponseEntity<?> uploadProfileImage(@RequestParam("image") MultipartFile imageFile) {
        Long userId = userContextUtil.getAuthenticatedUserId();
        if (userId == null) {
            //  인증되지 않은 경우
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            byte[] imageBytes = imageFile.getBytes();
            String base64Image = Base64.getEncoder().encodeToString(imageBytes);

            String imageUrl = profileService.saveProfileImage(userId, base64Image);
            return ResponseEntity.ok().body(Map.of("profileUrl", imageUrl));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("이미지 업로드 중 오류가 발생했습니다.");
        }
    }

    @GetMapping
    public ResponseEntity<ProfileDtoRes> getUserProfile() {
        Long userId = userContextUtil.getAuthenticatedUserId();
        if (userId == null) {
            //  인증되지 않은 경우
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        ProfileDtoRes profile = profileService.getUserProfile(userId);
        return ResponseEntity.ok(profile);
    }
}
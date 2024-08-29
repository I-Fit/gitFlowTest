package kr.co.ifit.api.controller;

import kr.co.ifit.db.entity.LikedGroup;
import kr.co.ifit.api.service.LikedGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;


@RestController
@RequestMapping("/api")
public class LikedGroupController {

    private LikedGroupService likedGroupService;

    @Autowired
    public void setLikedGroupService(LikedGroupService likedGroupService) {
        this.likedGroupService = likedGroupService;
    }
    
    //  사용자가 좋아요한 모든 모임 데이터를 반환
    @GetMapping("/liked-groups")
    public ResponseEntity<List<LikedGroup>> getLikedGroups(@RequestParam Integer userId) {
        List<LikedGroup> likedGroups = likedGroupService.getLikedGroupsByUserId(userId);
        return ResponseEntity.ok(likedGroups);
    }
}

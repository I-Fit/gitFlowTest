package kr.co.ifit.controller;

import kr.co.ifit.domain.dto.JoinedGroupDTO;
import kr.co.ifit.domain.dto.LikedGroupDTO;
import kr.co.ifit.domain.entity.Group;
import kr.co.ifit.service.HomeGroupService;
import kr.co.ifit.service.JoinedGroupService;
import kr.co.ifit.service.LikedGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class HomeGroupController {

    @Autowired
    public HomeGroupController(HomeGroupService homeGroupService, JoinedGroupService joinedGroupService, LikedGroupService likedGroupService) {
        this.homeGroupService = homeGroupService;
        this.joinedGroupService = joinedGroupService;
        this.likedGroupService = likedGroupService;
    }

    private final HomeGroupService homeGroupService;
    private final JoinedGroupService joinedGroupService;
    private final LikedGroupService likedGroupService;

    //    Home 페이지에 모든 모임 데이터를 보낸다
    @GetMapping("/group-list")
    public ResponseEntity<List<Group>> getGroupDetails() {
        List<Group> groups = homeGroupService.getAllGroups();
        return ResponseEntity.ok(groups);
    }

    //  Home 페이지에서 모임 참석을 누르면 userId, communityId를 서버에 보낸다
    @PostMapping("/join-group")
    public ResponseEntity<String> joinGroup(@RequestBody JoinedGroupDTO joinedGroupDTO) {
        try {
            joinedGroupService.joinGroup(joinedGroupDTO.getUserId(), joinedGroupDTO.getCommunityId());
            return ResponseEntity.ok("Successfully joined group.");
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    //  Home 페이지에서 모임의 하트를 클릭 했을 때 찜이 되면서 userId, communityId를 서버에서 응답 후 Liked 테이블에 추가
    @PostMapping("/like-group")
    public ResponseEntity<String> likeGroup(@RequestBody LikedGroupDTO likedGroupDTO) {
        try {
            likedGroupService.likedGroup(likedGroupDTO.getUserId(), likedGroupDTO.getCommunityId());
            return ResponseEntity.ok("Successfully liked group.");
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }
}
    
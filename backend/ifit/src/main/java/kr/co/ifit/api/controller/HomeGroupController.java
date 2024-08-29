package kr.co.ifit.api.controller;

import kr.co.ifit.api.request.JoinedGroupRequestDTO;
import kr.co.ifit.api.response.GroupResponseDTO;
import kr.co.ifit.api.service.HomeGroupService;
import kr.co.ifit.api.service.JoinedGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class HomeGroupController {

    @Autowired
    public HomeGroupController(HomeGroupService homeGroupService, JoinedGroupService joinedGroupService) {
        this.homeGroupService = homeGroupService;
        this.joinedGroupService = joinedGroupService;
    }

    private final HomeGroupService homeGroupService;
    private final JoinedGroupService joinedGroupService;

    //    Home 페이지에 모든 모임 데이터를 보낸다
    @GetMapping("/group-list")
    public ResponseEntity<List<GroupResponseDTO>> getGroupDetails() {
        List<GroupResponseDTO> groups = homeGroupService.getAllGroups();
        return ResponseEntity.ok(groups);
    }

    //  Home 페이지에서 모임 참석을 누르면 userId, communityId를 서버에 보낸다
    @PostMapping("/join-group")
    public ResponseEntity<String> joinGroup(@RequestBody JoinedGroupRequestDTO joinedGroupRequestDTO) {
        try {
            joinedGroupService.joinGroup(joinedGroupRequestDTO.getUserId(), joinedGroupRequestDTO.getCommunityId());
            return ResponseEntity.ok("Successfully joined group.");
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    //  좋아요 추가 및 삭제
    @PostMapping("/like-group")
    public void toggleLike(@RequestParam Integer communityId, @RequestParam Integer userId, @RequestParam boolean isHeartFilled) {
        homeGroupService.toggleLike(communityId, userId, isHeartFilled);
    }

}
    
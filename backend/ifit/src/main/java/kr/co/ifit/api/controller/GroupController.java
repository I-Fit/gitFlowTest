package kr.co.ifit.api.controller;

import kr.co.ifit.api.request.AddGroupRequestDTO;
import kr.co.ifit.api.request.JoinedGroupRequestDTO;
import kr.co.ifit.api.response.AddGroupResponseDTO;
import kr.co.ifit.api.response.GroupResponseDTO;
import kr.co.ifit.api.service.*;
import kr.co.ifit.db.entity.Group;
import kr.co.ifit.db.entity.LikedGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class GroupController {

    @Autowired
    public GroupController(AddGroupService addGroupService, HomeGroupService homeGroupService, JoinedGroupService joinedGroupService, CreatedGroupService createdGroupService, LikedGroupService likedGroupService) {
        this.addGroupService = addGroupService;
        this.homeGroupService = homeGroupService;
        this.joinedGroupService = joinedGroupService;
        this.createdGroupService = createdGroupService;
        this.likedGroupService = likedGroupService;
    }

    private final AddGroupService addGroupService;
    private final HomeGroupService homeGroupService;
    private final JoinedGroupService joinedGroupService;
    private final CreatedGroupService createdGroupService;
    private final LikedGroupService likedGroupService;

    //    생성된 모임 데이터를 받아오고 모임 ID를 생성해서 보낸다
    @PostMapping("/create-group")
    public ResponseEntity<AddGroupResponseDTO> createGroup(@RequestBody AddGroupRequestDTO addGroupRequestDTO) {
        try {
            Group group = addGroupService.createAddGroup(addGroupRequestDTO);
            AddGroupResponseDTO responseDTO = new AddGroupResponseDTO(
                    group.getCommunityId(),
                    "모임이 등록되었습니다."
            );
            return ResponseEntity.ok(responseDTO);
        } catch (RuntimeException e) {
            AddGroupResponseDTO responseDTO = new AddGroupResponseDTO(
                    null,
                    "모임 등록에 실패했습니다: " + e.getMessage()
            );
            return ResponseEntity.status(500).body(responseDTO);
        }
    }

    // Home 페이지에 보여지는 그룹 부분

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
//    @PostMapping("/like-group")
//    public void toggleLike(@RequestParam Integer communityId, @RequestParam Integer userId, @RequestParam boolean isHeartFilled) {
//        homeGroupService.toggleLike(communityId, userId, isHeartFilled);
//    }

    // 사용자가 만든 모임 내역 부분
    @PostMapping("/created-groups")
    //  userId로 만든 모임 리스트를 조회한 후 Group 엔티티 리스트를 반환하는데 GroupResponseDTO로 변환한다.
    public ResponseEntity<List<GroupResponseDTO>> getGroupDetails(@RequestBody AddGroupRequestDTO addGroupRequestDTO) {
        List<Group> groups = addGroupService.getGroupsByUserId(addGroupRequestDTO.getUserId());

        List<GroupResponseDTO> responseDTOs = groups.stream().map(group -> new GroupResponseDTO(
                group.getCommunityId(),
                group.getTitle(),
                group.getTopboxContent(),
                group.getSport(),
                group.getLocation(),
                group.getPerson(),
                group.getDate())).toList();
        return ResponseEntity.ok(responseDTOs);
    }

//    @PostMapping("/")
//    public void toggleLike(@RequestParam Integer communityId, @RequestParam Integer userId, @RequestParam boolean isHeartFilled) {
//        createdGroupService.toggleLike(communityId, userId, isHeartFilled);
//    }

    //  참여한 모임 내역에서 사용자가 참여한 모임 목록을 반환하고 참여한 모임 내역에 보여줌
    @GetMapping("/details")
    //  userId를 쿼리 파라미터로 받아서 요청을 처리
    public ResponseEntity<?> getJoinedGroupsDetails(@RequestParam Integer userId) {
        //  사용자가 참여한 모임 목록을 조회
        List<Group> groups = joinedGroupService.getGroupsByUserId(userId);
        //  조회된 group 반환
        return ResponseEntity.ok(groups);
    }

    // 참여한 모임 내역에서 사용자가 참석 취소를 누른 모임에 대한 코드
    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteJoinedGroups(@RequestBody Map<String, Integer> payload) {
        Integer userId = payload.get("userId");
        Integer communityId = payload.get("communityId");

        //  사용자ID와 모임ID를 기반으로 삭제 처리
        joinedGroupService.deleteGroupForUser(userId, communityId);

        //  삭제가 성공적으로 되었음을 나타낸다.
        return ResponseEntity.noContent().build();
    }

//    @PostMapping("/")
//    public void toggleLike(@RequestParam Integer communityId, @RequestParam Integer userId, @RequestParam boolean isHeartFilled) {
//        joinedGroupService.toggleLike(communityId, userId, isHeartFilled);
//    }

    //  사용자가 좋아요한 모든 모임 데이터를 반환
    @GetMapping("/liked-groups")
    public ResponseEntity<List<LikedGroup>> getLikedGroups(@RequestParam Integer userId) {
        List<LikedGroup> likedGroups = likedGroupService.getLikedGroupsByUserId(userId);
        return ResponseEntity.ok(likedGroups);
    }
}
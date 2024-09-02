package kr.co.ifit.api.controller;

import kr.co.ifit.api.request.AddGroupRequestDTO;
import kr.co.ifit.api.request.CreatedGroupRequestDTO;
import kr.co.ifit.api.request.JoinedGroupRequestDTO;
import kr.co.ifit.api.request.LikedGroupRequestDTO;
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

    // createdGroup - 내가 만든 모임 내역
    // 사용자가 만든 모임 내역 부분
    @PostMapping("/created-groups")
    //  userId로 만든 모임을 Group 엔티티에서 조회 후 응답
    public ResponseEntity<List<GroupResponseDTO>> getCreatedGroups(@RequestBody CreatedGroupRequestDTO createdGroupRequestDTO) {
        Long userId = createdGroupRequestDTO.getUserId();
        List<GroupResponseDTO> groups = createdGroupService.getCreatedGroupsByUserId(userId);
        return ResponseEntity.ok(groups);
    }

    //  내가 만든 모임에서 모임 삭제 요청
    @DeleteMapping("/delete-groups")
    public ResponseEntity<String> deleteCreatedGroup(@RequestBody CreatedGroupRequestDTO createdGroupRequestDTO) {
        boolean deleted = createdGroupService.deleteGroup(createdGroupRequestDTO.getUserId(), createdGroupRequestDTO.getCommunityId());
        if (deleted) {
            return ResponseEntity.ok("삭제 완료");
        } else {
            return ResponseEntity.status(400).body("모임을 찾을 수 없습니다.");
        }
    }

    // JoinedGroup - 참여한 모임 내역
    //  참여한 모임 내역에서 사용자가 참여한 모임 목록을 반환하고 참여한 모임 내역에 보여줌
    @GetMapping("/details")
    //  userId를 쿼리 파라미터로 받아서 요청을 처리
    public ResponseEntity<?> getJoinedGroupsDetails(@RequestParam Long userId) {
        //  사용자가 참여한 모임 목록을 조회
        List<Group> groups = joinedGroupService.getGroupsByUserId(userId);
        //  조회된 group 반환
        return ResponseEntity.ok(groups);
    }

    // 참여한 모임 내역에서 사용자가 참석 취소를 누른 모임에 대한 코드
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteJoinedGroups(@RequestBody JoinedGroupRequestDTO joinedGroupRequestDTO) {
        boolean deleted = joinedGroupService.deleteGroup(joinedGroupRequestDTO.getUserId(), joinedGroupRequestDTO.getCommunityId());
        if (deleted) {
            return ResponseEntity.ok("삭제 완료");
        } else {
            return ResponseEntity.status(400).body("모임을 찾을 수 없습니다.");
        }
    }

    //  찜한 모임 내역
    //  사용자가 좋아요한 모든 모임 데이터를 반환
    @GetMapping("/liked-groups")
    public ResponseEntity<List<GroupResponseDTO>> getLikedGroups(@RequestParam Long userId) {
        List<GroupResponseDTO> likedGroups = likedGroupService.getLikedGroupsByUserId(userId);
        return ResponseEntity.ok(likedGroups);
    }

    //  좋아요 추가 및 삭제
    @PostMapping("/like-group")
    public ResponseEntity<String> toggleLike(@RequestBody LikedGroupRequestDTO likedGroupRequestDTO) {
        try {
            homeGroupService.toggleLike(likedGroupRequestDTO);
            createdGroupService.toggleLike(likedGroupRequestDTO);
            joinedGroupService.toggleLike(likedGroupRequestDTO);

            return ResponseEntity.ok("Successfully liked group.");
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }
}
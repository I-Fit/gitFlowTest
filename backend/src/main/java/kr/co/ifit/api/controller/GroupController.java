package kr.co.ifit.api.controller;

import kr.co.ifit.api.request.AddGroupDtoReq;
import kr.co.ifit.api.request.CreatedGroupDtoReq;
import kr.co.ifit.api.request.JoinedGroupDtoReq;
import kr.co.ifit.api.request.LikedGroupDtoReq;
import kr.co.ifit.api.response.AddGroupDtoRes;
import kr.co.ifit.api.response.GroupDtoRes;
import kr.co.ifit.api.service.*;
import kr.co.ifit.common.util.UserContextUtil;
import kr.co.ifit.db.entity.Group;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class GroupController {

    private final AddGroupService addGroupService;
    private final HomeGroupService homeGroupService;
    private final JoinedGroupService joinedGroupService;
    private final CreatedGroupService createdGroupService;
    private final LikedGroupService likedGroupService;

    private final UserContextUtil userContextUtil;

    //    생성된 모임 데이터를 받아오고 모임 ID를 생성해서 보낸다
    @PostMapping("/create-group")
    public ResponseEntity<AddGroupDtoRes> createGroup(@RequestBody AddGroupDtoReq addGroupDtoReq) {
        try {
            // 로그인 Id로 사용자 식별 Id 조회
            Long userId = userContextUtil.getAuthenticatedUserId();
            if (userId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new AddGroupDtoRes(null, "사용자를 찾을 수 없습니다."));
            }

            Group group = addGroupService.createAddGroup(addGroupDtoReq, userId);

            AddGroupDtoRes responseDTO = new AddGroupDtoRes(
                    group.getCommunityId(),
                    "모임이 등록되었습니다."
            );
            return ResponseEntity.ok(responseDTO);
        } catch (RuntimeException e) {
            AddGroupDtoRes responseDTO = new AddGroupDtoRes(
                    null,
                    "모임 등록에 실패했습니다: " + e.getMessage()
            );
            return ResponseEntity.status(500).body(responseDTO);
        }
    }

    // Home 페이지에 보여지는 그룹 부분
//    // 캐러셀
//    @GetMapping("/carusel/{imageId}")
//    public ResponseEntity<List<GroupDtoRes>> getGroupsByImageId(@PathVariable Long imageId) {
//        List<GroupDtoRes> groups = homeGroupService.findGroupsByImageId(imageId);
//        return ResponseEntity.ok(groups);
//    }


    // 홈 페이지에 있는 필터 부분 - 운동 종목
    @GetMapping("/filter/exercises")
    public ResponseEntity<List<String>> getAllExercises() {
        List<String> exercises = homeGroupService.getAllExercises();
        return ResponseEntity.ok(exercises);
    }

    //  전달한 검색어로 모임을 검색하여 결과를 반환
    @PostMapping("/search")
    public ResponseEntity<List<GroupDtoRes>> searchGroups(@RequestParam String searchTerm) {
        List<GroupDtoRes> groups = homeGroupService.searchGroups(searchTerm);
        return ResponseEntity.ok(groups);
    }

    //  정렬순
    @PostMapping("/sort")
    public ResponseEntity<List<GroupDtoRes>> getSortedGroups(@RequestParam int value) {
        List<GroupDtoRes> groups = homeGroupService.getSortedGroups(value);
        return ResponseEntity.ok(groups);
    }

    //    Home 페이지에 모든 모임 데이터를 보낸다
    @GetMapping("/group-list")
    public ResponseEntity<List<GroupDtoRes>> getGroupDetails() {
        List<GroupDtoRes> groups = homeGroupService.getAllGroups();
        return ResponseEntity.ok(groups);
    }

    //  Home 페이지에서 모임 참석을 누르면 userId, communityId를 서버에 보낸다
    @PostMapping("/join-group")
    public ResponseEntity<String> joinGroup(@RequestBody JoinedGroupDtoReq joinedGroupDtoReq) {
        try {
            joinedGroupService.joinGroup(joinedGroupDtoReq.getUserId(), joinedGroupDtoReq.getCommunityId());
            return ResponseEntity.ok("모임 참여 완료");
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    //  좋아요 추가 및 삭제
    @PostMapping("home/like-group")
    public ResponseEntity<String> toggleLike(@RequestBody LikedGroupDtoReq likedGroupDtoReq) {
        try {
            homeGroupService.toggleLike(likedGroupDtoReq);
            if (likedGroupDtoReq.isHeartFilled()) {
                return ResponseEntity.ok("좋아요 추가 완료");
            } else {
                return ResponseEntity.ok("좋아요 취소 완료");
            }
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }
    // -------------------------------------------------------------------------------------------------------------------------------
    // createdGroup - 내가 만든 모임 내역
    // 사용자가 만든 모임 내역 부분
    @PostMapping("/created-groups")
    //  userId로 만든 모임을 Group 엔티티에서 조회 후 응답
    public ResponseEntity<List<GroupDtoRes>> getCreatedGroups(@RequestBody CreatedGroupDtoReq createdGroupDtoReq) {
        Long userId = createdGroupDtoReq.getUserId();
        List<GroupDtoRes> groups = createdGroupService.getCreatedGroupsByUserId(userId);
        return ResponseEntity.ok(groups);
    }
    //  내가 만든 모임에서 모임 삭제 요청
    @DeleteMapping("/delete-groups")
    public ResponseEntity<String> deleteCreatedGroup(@RequestBody CreatedGroupDtoReq createdGroupDtoReq) {
        boolean deleted = createdGroupService.deleteGroup(createdGroupDtoReq.getUserId(), createdGroupDtoReq.getCommunityId());
        if (deleted) {
            return ResponseEntity.ok("삭제 완료");
        } else {
            return ResponseEntity.status(400).body("모임을 찾을 수 없습니다.");
        }
    }
    //  좋아요 추가 및 삭제
    @PostMapping("created/like-group/")
    public ResponseEntity<String> toggleCreatedGroupLike(@RequestBody LikedGroupDtoReq likedGroupDtoReq) {
        try {
            createdGroupService.toggleLike(likedGroupDtoReq);
            if (likedGroupDtoReq.isHeartFilled()) {
                return ResponseEntity.ok("좋아요 추가 완료");
            } else {
                return ResponseEntity.ok("좋아요 취소 완료");
            }
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }
    // --------------------------------------------------------------------------------------------------------------------------------------------
    // JoinedGroup - 참여한 모임 내역
    //  참여한 모임 내역에서 사용자가 참여한 모임 목록을 반환하고 참여한 모임 내역에 보여줌
    @PostMapping("/details")
    //  userId를 쿼리 파라미터로 받아서 요청을 처리
    public ResponseEntity<List<GroupDtoRes>> getJoinedGroupsDetails(@RequestParam Long userId) {
        try {
            //  사용자가 참여한 모임 목록을 조회
            List<GroupDtoRes> groups = joinedGroupService.getGroupsByUserId(userId);
            //  조회된 group 반환
            return ResponseEntity.ok(groups);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(Collections.emptyList());
        }
    }
    // 참여한 모임 내역에서 사용자가 참석 취소를 누른 모임에 대한 코드
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteJoinedGroups(@RequestBody JoinedGroupDtoReq joinedGroupDtoReq) {
        boolean deleted = joinedGroupService.deleteGroup(joinedGroupDtoReq.getUserId(), joinedGroupDtoReq.getCommunityId());
        if (deleted) {
            return ResponseEntity.ok("삭제 완료");
        } else {
            return ResponseEntity.status(400).body("모임을 찾을 수 없습니다.");
        }
    }
    //  좋아요 추가 및 삭제
    @PostMapping("joined/like-group/")
    public ResponseEntity<String> toggleJoinedGroupLike(@RequestBody LikedGroupDtoReq likedGroupDtoReq) {
        try {
            joinedGroupService.toggleLike(likedGroupDtoReq);
            if (likedGroupDtoReq.isHeartFilled()) {
                return ResponseEntity.ok("좋아요 추가 완료");
            } else {
                return ResponseEntity.ok("좋아요 취소 완료");
            }
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }
    // -----------------------------------------------------------------------------------------------------------------------------------------
    //  찜한 모임 내역
    //  사용자가 좋아요한 모든 모임 데이터를 반환
    @GetMapping("/liked-groups")
    public ResponseEntity<List<GroupDtoRes>> getLikedGroups(@RequestParam Long userId) {
        List<GroupDtoRes> likedGroups = likedGroupService.getLikedGroupsByUserId(userId);
        return ResponseEntity.ok(likedGroups);
    }
}
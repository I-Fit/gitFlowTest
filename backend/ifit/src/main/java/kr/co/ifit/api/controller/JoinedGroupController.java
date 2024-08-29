package kr.co.ifit.api.controller;

import kr.co.ifit.db.entity.Group;
import kr.co.ifit.api.service.JoinedGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/joined-groups")
public class JoinedGroupController {

    private final JoinedGroupService joinedGroupService;

    @Autowired
    public JoinedGroupController(JoinedGroupService joinedGroupService) {
        this.joinedGroupService = joinedGroupService;
    }

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

    @PostMapping("/")
    public void toggleLike(@RequestParam Integer communityId, @RequestParam Integer userId, @RequestParam boolean isHeartFilled) {
        joinedGroupService.toggleLike(communityId, userId, isHeartFilled);
    }



}

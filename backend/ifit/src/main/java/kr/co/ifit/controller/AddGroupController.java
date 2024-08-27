package kr.co.ifit.controller;

import kr.co.ifit.domain.dto.GroupDTO;
import kr.co.ifit.domain.dto.JoinGroupDTO;
import kr.co.ifit.domain.entity.Group;
import kr.co.ifit.service.AddGroupService;
import kr.co.ifit.service.HomeGroupService;
import kr.co.ifit.service.JoinGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AddGroupController {

    @Autowired
    public AddGroupController(AddGroupService addGroupService, HomeGroupService homeGroupService, JoinGroupService joinGroupService) {
        this.addGroupService = addGroupService;
        this.homeGroupService = homeGroupService;
        this.joinGroupService = joinGroupService;
    }

    private final AddGroupService addGroupService;

    //    생성된 모임 데이터를 받아오고 모임 ID를 생성해서 보낸다
    @PostMapping("/create-group")
    public ResponseEntity<Object> createGroup(@RequestBody GroupDTO groupDTO) {
        try {
            Group group = addGroupService.createAddGroup(groupDTO);
            return ResponseEntity.ok("모임이 등록되었습니다. ID: " + group.getCommunityId());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("모임 등록에 실패했습니다." + e.getMessage());
        }
    }

    private final HomeGroupService homeGroupService;

    //    Home 페이지에 모든 모임 데이터를 보낸다
    @GetMapping("/group-details")
    public ResponseEntity<List<Group>> getGroupDetails() {
        List<Group> groups = homeGroupService.getAllGroups();
        return ResponseEntity.ok(groups);
    }

    private final JoinGroupService joinGroupService;

    //  Home 페이지에서 모임 참석을 누르면 userId, communityId를 서버에 보낸다
    @PostMapping("/join-group")
    public ResponseEntity<String> joinGroup(@RequestBody JoinGroupDTO joinGroupDTO) {
        try {
            joinGroupService.joinGroup(joinGroupDTO.getUserId(), joinGroupDTO.getCommunityId());
            return ResponseEntity.ok("Successfully joined group.");
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

}

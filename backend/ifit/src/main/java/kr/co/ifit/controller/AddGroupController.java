package kr.co.ifit.controller;

import kr.co.ifit.domain.dto.GroupDTO;
import kr.co.ifit.domain.entity.Group;
import kr.co.ifit.service.AddGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AddGroupController {

    @Autowired
    public AddGroupController(AddGroupService addGroupService) {
        this.addGroupService = addGroupService;

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
}
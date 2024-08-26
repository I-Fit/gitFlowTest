package kr.co.ifit.controller;

import kr.co.ifit.domain.dto.AddGroupDTO;
import kr.co.ifit.domain.entity.AddGroup;
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

    private final AddGroupService addGroupService;

    @Autowired
    public AddGroupController(AddGroupService addGroupService) {
        this.addGroupService = addGroupService;
    }

    @PostMapping("/create-group")
    public ResponseEntity<Object> createGroup(@RequestBody AddGroupDTO addGroupDTO) {
        try {
            AddGroup addGroup = addGroupService.createAddGroup(addGroupDTO);
            return ResponseEntity.ok("모임이 등록되었습니다. ID: " + addGroup.getCommunityId());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("모임 등록에 실패했습니다." + e.getMessage());
        }
    }
}

package kr.co.ifit.controller;

import kr.co.ifit.domain.dto.GroupDTO;
import kr.co.ifit.domain.entity.Group;
import kr.co.ifit.service.AddGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CreatedGroupController {

    //  비즈니스 로직을 처리할 서비스 객체 주입
    private final AddGroupService addGroupService;

    @Autowired
    public CreatedGroupController(AddGroupService addGroupService) {
        this.addGroupService = addGroupService;
    }

    @PostMapping("/created-groups")
    //  @RequestBody는 요청 본문을 GroupDTO 객체로 변환
    public ResponseEntity<List<Group>> getGroupDetails(@RequestBody GroupDTO groupDTO) {
        //  GroupDTO에서 userId를 추출하여 getGroupsByUserId 메서드를 호출하고, 모임 리스트를 가져온다
        List<Group> groups = addGroupService.getGroupsByUserId(groupDTO.getUserId());
        return ResponseEntity.ok(groups);
    }
}

package kr.co.ifit.api.controller;

import kr.co.ifit.api.request.AddGroupRequestDTO;
import kr.co.ifit.api.response.AddGroupResponseDTO;
import kr.co.ifit.db.entity.Group;
import kr.co.ifit.api.service.AddGroupService;
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
}
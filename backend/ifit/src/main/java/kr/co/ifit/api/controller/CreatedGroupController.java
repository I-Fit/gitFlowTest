package kr.co.ifit.api.controller;

import kr.co.ifit.api.request.AddGroupRequestDTO;
import kr.co.ifit.api.response.GroupResponseDTO;
import kr.co.ifit.db.entity.Group;
import kr.co.ifit.api.service.AddGroupService;
import kr.co.ifit.api.service.CreatedGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class CreatedGroupController {

    //  비즈니스 로직을 처리할 서비스 객체 주입
    private final AddGroupService addGroupService;
    private final CreatedGroupService createdGroupService;

    @Autowired
    public CreatedGroupController(AddGroupService addGroupService, CreatedGroupService createdGroupService) {
        this.addGroupService = addGroupService;
        this.createdGroupService = createdGroupService;
    }

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

    @PostMapping("/")
    public void toggleLike(@RequestParam Integer communityId, @RequestParam Integer userId, @RequestParam boolean isHeartFilled) {
        createdGroupService.toggleLike(communityId, userId, isHeartFilled);
    }
}

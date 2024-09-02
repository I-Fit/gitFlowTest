package kr.co.ifit.api.service;

import kr.co.ifit.api.request.LikedGroupRequestDTO;
import kr.co.ifit.api.response.GroupResponseDTO;
import kr.co.ifit.db.entity.Group;
import kr.co.ifit.db.repository.GroupRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HomeGroupService {

    private final GroupRepository groupRepository;
    private final LikedGroupService likedGroupService;

    public HomeGroupService(GroupRepository groupRepository, LikedGroupService likedGroupService) {
        this.groupRepository = groupRepository;
        this.likedGroupService = likedGroupService;
    }

    //  Group 엔티티를 HomeGroupResponseDTO 객체로 변환
    public List<GroupResponseDTO> getAllGroups() {
        List<Group> groups = groupRepository.findAll();
        return groups.stream().map(group -> new GroupResponseDTO(
                group.getCommunityId(),
                group.getTitle(),
                group.getTopboxContent(),
                group.getSport(),
                group.getLocation(),
                group.getPerson(),
                group.getDate())).collect(Collectors.toList());         // 변환된 DTO 객체를 리스트로 수집
    }
    
    //  LikedGroupService에 좋아요 기능 로직을 가져다 씀
    public void toggleLike(Long userId, Long communityId, boolean isHeartFilled) {
        likedGroupService.toggleLike(new LikedGroupRequestDTO(communityId, userId, isHeartFilled));
    }
}

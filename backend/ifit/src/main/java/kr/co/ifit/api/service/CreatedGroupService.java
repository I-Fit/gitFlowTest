package kr.co.ifit.api.service;

import kr.co.ifit.api.request.LikedGroupRequestDTO;
import kr.co.ifit.api.response.GroupResponseDTO;
import kr.co.ifit.db.entity.Group;
import kr.co.ifit.db.entity.User;
import kr.co.ifit.db.repository.GroupRepository;
import kr.co.ifit.db.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CreatedGroupService {

    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final LikedGroupService likedGroupService;

    @Autowired
    public CreatedGroupService(UserRepository userRepository, GroupRepository groupRepository, LikedGroupService likedGroupService) {
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
        this.likedGroupService = likedGroupService;
    }
    //  GroupRepository에서 userId로 조회 후 GroupresponseDTO로 변환 후 groups 리스트 응답
    public List<GroupResponseDTO> getCreatedGroupsByUserId(Long userId) {
        List<Group> groups = groupRepository.findByUserId(userId);

        return groups.stream()
                .map(group -> new GroupResponseDTO(
                group.getCommunityId(),
                group.getTitle(),
                group.getTopboxContent(),
                group.getSport(),
                group.getLocation(),
                group.getPerson(),
                group.getDate()
        )).toList();
    }

    //  내가 만든 모임에서 모임 삭제를 눌렀을 때 찾아서 없앤다
    public boolean deleteGroup(Long userId, Long communityId) {
        List<Group> groups = groupRepository.findByUserId(userId);

        Group groupDelete = groups.stream().filter(group -> group.getCommunityId().equals(communityId)).findFirst().orElse(null);
        if (groupDelete != null) {
            groupRepository.delete(groupDelete);
            return true;
        }
        return false;
    }

    public void toggleLike(Long userId, Long communityId, boolean isHeartFilled) {
        likedGroupService.toggleLike(new LikedGroupRequestDTO(userId, communityId, isHeartFilled));
    }
}

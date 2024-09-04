package kr.co.ifit.api.service;

import kr.co.ifit.api.request.LikedGroupRequestDTO;
import kr.co.ifit.api.response.GroupResponseDTO;
import kr.co.ifit.db.entity.Group;
import kr.co.ifit.db.entity.LikedGroup;
import kr.co.ifit.db.entity.User;
import kr.co.ifit.db.repository.GroupRepository;
import kr.co.ifit.db.repository.LikedGroupRepository;
import kr.co.ifit.db.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LikedGroupService {

    private final LikedGroupRepository likedGroupRepository;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;

    @Autowired
    public LikedGroupService(LikedGroupRepository likedGroupRepository, GroupRepository groupRepository, UserRepository userRepository) {
        this.likedGroupRepository = likedGroupRepository;
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
    }

    //  받아온 userId를 조회한다
    @Transactional
    public List<GroupResponseDTO> getLikedGroupsByUserId(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("사용자 없습니다"));
        List<LikedGroup> likedGroups = likedGroupRepository.findByUser(user);
        return likedGroups.stream()
                .map(likedGroup -> {
                    Group group = likedGroup.getGroup();
                    return new GroupResponseDTO(
                        group.getCommunityId(),
                        group.getTitle(),
                        group.getTopboxContent(),
                        group.getSport(),
                        group.getLocation(),
                        group.getPerson(),
                        group.getPeopleParticipation(),
                        group.getDate()
                    );
                }).toList();
    }
    
    //  좋아요 기능 추가 및 삭제
    @Transactional
    public void toggleLike(LikedGroupRequestDTO dto) {
        User user = userRepository.findById(dto.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));
        Group group = groupRepository.findById(dto.getCommunityId()).orElseThrow(() -> new RuntimeException("Group not found"));

        if (dto.isHeartFilled()) {
            //  좋아요 추가
            LikedGroup likedGroup = new LikedGroup();
            likedGroup.setUser(user);
            likedGroup.setGroup(group);
            likedGroup.setCreatedAt(LocalDateTime.now());
            likedGroup.setUpdatedAt(LocalDateTime.now());

            likedGroupRepository.save(likedGroup);
        }
    }
    @Transactional
    public void removeLike(LikedGroupRequestDTO dto) {
        User user = userRepository.findById(dto.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));
        Group group = groupRepository.findById(dto.getCommunityId()).orElseThrow(() -> new RuntimeException("Group not found"));

        LikedGroup likedGroup = likedGroupRepository.findByUserAndGroup(user, group);
        if (likedGroup != null) {
            likedGroupRepository.delete(likedGroup);
        }
    }

}
